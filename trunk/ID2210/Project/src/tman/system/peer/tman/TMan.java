package tman.system.peer.tman;

import common.configuration.TManConfiguration;
import common.peer.PeerAddress;
import java.util.ArrayList;

import cyclon.system.peer.cyclon.CyclonPartnersPort;
import cyclon.system.peer.cyclon.CyclonPartnersRequest;
import cyclon.system.peer.cyclon.CyclonPartnersResponse;
import java.math.BigInteger;
import java.util.*;
import se.sics.asdistances.ASDistances;
import se.sics.asdistances.DistanceCalculator;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;

import tman.simulator.snapshot.Snapshot;

public final class TMan extends ComponentDefinition {
    Negative<TManPartnersPort> tmanPartnersPort = negative(TManPartnersPort.class);
    Positive<CyclonPartnersPort> cyclonPartnersPort = positive(CyclonPartnersPort.class);
    Positive<Network> networkPort = positive(Network.class);
    Positive<Timer> timerPort = positive(Timer.class);
    private long period;
    private BigInteger identifierSpaceSize;
    private PeerAddress self;
    private PeerAddress succ;
    private PeerAddress pred;
    private ArrayList<PeerAddress> tmanPartners;
    private ArrayList<PeerAddress> cyclonPartners;
    private TManConfiguration tmanConfiguration;
    private Random rand = new Random(System.nanoTime());
    private boolean active;
    private List<PeerAddress> tmanPeerBuf;
    private PeerAddress tmanPeer;
    private ASDistances distances = ASDistances.getInstance();
    private BigInteger k;

//-------------------------------------------------------------------	
    public TMan() {
        tmanPartners = new ArrayList<PeerAddress>();
        cyclonPartners = new ArrayList<PeerAddress>();

        subscribe(handleInit, control);
        subscribe(handleRound, timerPort);
        subscribe(handleCyclonPartnersResponse, cyclonPartnersPort);
        subscribe(handleTManPartnersRequest, tmanPartnersPort);
        subscribe(handleTManUpdateRequest, networkPort);
        subscribe(handleTManUpdateResponse, networkPort);
    }
//-------------------------------------------------------------------	
    Handler<TManInit> handleInit = new Handler<TManInit>() {
        @Override
        public void handle(TManInit init) {
            self = init.getSelf();
            tmanConfiguration = init.getConfiguration();
            period = tmanConfiguration.getPeriod();
            identifierSpaceSize = tmanConfiguration.getIdentifierSpaceSize();
            succ = self;
            pred = self;
            k = identifierSpaceSize.divide(new BigInteger("128"));

            SchedulePeriodicTimeout rst = new SchedulePeriodicTimeout(period, period);
            rst.setTimeoutEvent(new TManSchedule(rst));
            trigger(rst, timerPort);

            Snapshot.updateTManPartners(self, tmanPartners);
            Snapshot.updateSuccPred(self, succ, pred);
        }
    };
//-------------------------------------------------------------------	
    Handler<TManSchedule> handleRound = new Handler<TManSchedule>() {
        @Override
        public void handle(TManSchedule event) {
            CyclonPartnersRequest request = new CyclonPartnersRequest();
            active = true;
            trigger(request, cyclonPartnersPort);
        }
    };
//-------------------------------------------------------------------	
    Handler<CyclonPartnersResponse> handleCyclonPartnersResponse = new Handler<CyclonPartnersResponse>() {
        @Override
        public void handle(CyclonPartnersResponse event) {
            cyclonPartners = event.getPartners();

            if (active) {
                PeerAddress peer;
                if (!tmanPartners.isEmpty()) {
                    peer = selectPeer(succ, pred, tmanPartners);
                } else if (!cyclonPartners.isEmpty()) {
                    peer = selectPeer(succ, pred, cyclonPartners);
                } else {
                    active = false;
                    return;
                }

                List<PeerAddress> buf = new ArrayList<PeerAddress>();
                buf.add(succ);
                buf.add(pred);
                buf.addAll(tmanPartners);
                buf.add(self);
                buf.addAll(cyclonPartners);

                active = false;
                trigger(new TManUpdateRequest(buf, self, peer), networkPort);
            } else {
                List<PeerAddress> buf = new ArrayList<PeerAddress>();
                buf.add(succ);
                buf.add(pred);
                buf.addAll(tmanPartners);
                buf.add(self);
                buf.addAll(cyclonPartners);

                trigger(new TManUpdateResponse(buf, self, tmanPeer), networkPort);

                buf.clear();
                buf.add(succ);
                buf.add(pred);
                buf.addAll(tmanPartners);
                buf.addAll(tmanPeerBuf);

                selectView(buf);

                Snapshot.updateTManPartners(self, tmanPartners);
                Snapshot.updateSuccPred(self, succ, pred);
            }
        }
    };
//-------------------------------------------------------------------	
    Handler<TManUpdateRequest> handleTManUpdateRequest = new Handler<TManUpdateRequest>() {
        @Override
        public void handle(TManUpdateRequest event) {
            tmanPeerBuf = event.getBuffer();
            tmanPeer = event.getPeerSource();

            CyclonPartnersRequest request = new CyclonPartnersRequest();
            trigger(request, cyclonPartnersPort);
        }
    };
//-------------------------------------------------------------------	
    Handler<TManUpdateResponse> handleTManUpdateResponse = new Handler<TManUpdateResponse>() {
        @Override
        public void handle(TManUpdateResponse event) {
            List<PeerAddress> bufPeer = event.getBuffer();

            List<PeerAddress> buf = new ArrayList<PeerAddress>();
            buf.add(succ);
            buf.add(pred);
            buf.addAll(tmanPartners);
            buf.addAll(bufPeer);

            selectView(buf);

            Snapshot.updateTManPartners(self, tmanPartners);
            Snapshot.updateSuccPred(self, succ, pred);
        }
    };
//-------------------------------------------------------------------	
    Handler<TManPartnersRequest> handleTManPartnersRequest = new Handler<TManPartnersRequest>() {
        @Override
        public void handle(TManPartnersRequest event) {
            TManPartnersResponse response = new TManPartnersResponse(tmanPartners);
            trigger(response, tmanPartnersPort);
        }
    };
//-------------------------------------------------------------------	

    private int compareSucc(BigInteger a, BigInteger b) {
//        if (a.equals(b)) {
//            return identifierSpaceSize.intValue();
//        }
//        BigInteger first = identifierSpaceSize.subtract(a.subtract(b).abs());
//        BigInteger second = a.subtract(b).abs();
//        return first.min(second).intValue();
        BigInteger res = b.subtract(a);
        if (res.signum() <= 0) {
            res = identifierSpaceSize.add(res);
        }
        return res.intValue();
    }

    private int comparePred(BigInteger a, BigInteger b) {
        BigInteger res = a.subtract(b);
        if (res.signum() <= 0) {
            res = identifierSpaceSize.add(res);
        }
        return res.intValue();
    }

    private int compareFinger(BigInteger a, BigInteger b, PeerAddress peer) {
        BigInteger res = b.subtract(a);
        if (res.signum() < 0) {
            res = identifierSpaceSize.add(res);
        }

        //ASDistances
        byte interASPenalty = distances.getDistance(
                self.getPeerAddress().getIp().getHostAddress(),
                peer.getPeerAddress().getIp().getHostAddress());

        return res.add(k.multiply(new BigInteger(Byte.toString(interASPenalty)))).intValue();
    }

    private PeerAddress selectPeer(PeerAddress succ, PeerAddress pred, List<PeerAddress> fingers) {
        Pair[] pairs = new Pair[fingers.size() + 2];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = new Pair();
        }

        pairs[0].ranking = compareSucc(self.getPeerId(), succ.getPeerId());
        pairs[0].peer = succ;
        pairs[1].ranking = comparePred(self.getPeerId(), pred.getPeerId());
        pairs[1].peer = pred;
        for (int i = 0; i < fingers.size(); i++) {
            pairs[i + 2].ranking = compareFinger(self.getPeerId().add(new BigInteger("2").pow(i)).mod(identifierSpaceSize),
                    fingers.get(i).getPeerId(), fingers.get(i));
            pairs[i + 2].peer = fingers.get(i);
        }

        Arrays.sort(pairs, c);

        return pairs[rand.nextInt((int) Math.ceil(pairs.length / 2.0))].peer;
    }

    private synchronized void selectView(List<PeerAddress> buf) {
        Pair[] pairs;

        //succ
        pairs = new Pair[buf.size()];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = new Pair();
        }
        for (int i = 0; i < buf.size(); i++) {
            pairs[i].ranking = compareSucc(self.getPeerId(), buf.get(i).getPeerId());
            pairs[i].peer = buf.get(i);
        }
        Arrays.sort(pairs, c);
        succ = pairs[0].peer;

        //pred
        pairs = new Pair[buf.size()];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = new Pair();
        }
        for (int i = 0; i < buf.size(); i++) {
            pairs[i].ranking = comparePred(self.getPeerId(), buf.get(i).getPeerId());
            pairs[i].peer = buf.get(i);
        }
        Arrays.sort(pairs, c);
        pred = pairs[0].peer;

        //fingers
        int n = identifierSpaceSize.subtract(BigInteger.ONE).bitLength();
        tmanPartners = new ArrayList<PeerAddress>(n);
        for (int j = 0; j < n; j++) {
            pairs = new Pair[buf.size()];
            for (int i = 0; i < pairs.length; i++) {
                pairs[i] = new Pair();
            }
            BigInteger fingerId = self.getPeerId().add(new BigInteger("2").pow(j)).mod(identifierSpaceSize);
            for (int i = 0; i < buf.size(); i++) {
                pairs[i].ranking = compareFinger(fingerId, buf.get(i).getPeerId(), buf.get(i));
                pairs[i].peer = buf.get(i);
            }
            Arrays.sort(pairs, c);
            tmanPartners.add(pairs[0].peer);
        }
    }

    private class Pair {
        PeerAddress peer;
        int ranking;

        @Override
        public String toString() {
            return "{" + peer + "=" + ranking + "}";
        }
    }
    private final Comparator<Pair> c = new Comparator<Pair>() {
        @Override
        public int compare(Pair o1, Pair o2) {
            return Integer.compare(o1.ranking, o2.ranking);
        }
    };
}
