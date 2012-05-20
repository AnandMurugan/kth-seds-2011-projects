package tman.system.peer.tman;

import common.configuration.TManConfiguration;
import common.peer.PeerAddress;
import java.util.ArrayList;

import cyclon.system.peer.cyclon.CyclonPartnersPort;
import cyclon.system.peer.cyclon.CyclonPartnersRequest;
import cyclon.system.peer.cyclon.CyclonPartnersResponse;
import java.util.AbstractList;
import java.util.List;
import java.util.Random;

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
    private PeerAddress self;
    private PeerAddress succ;
    private PeerAddress pred;
    private ArrayList<PeerAddress> tmanPartners;
    private ArrayList<PeerAddress> cyclonPartners;
    private TManConfiguration tmanConfiguration;
    private Random rand = new Random(System.nanoTime());

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
            succ = self;
            pred = self;

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
            CyclonPartnersRequest response = new CyclonPartnersRequest();
            trigger(response, cyclonPartnersPort);

        }
    };
//-------------------------------------------------------------------	
    Handler<CyclonPartnersResponse> handleCyclonPartnersResponse = new Handler<CyclonPartnersResponse>() {
        @Override
        public void handle(CyclonPartnersResponse event) {
            cyclonPartners = event.getPartners();

            PeerAddress peer;
            //TODO
            if (tmanPartners.isEmpty()) {
                peer = cyclonPartners.get(rand.nextInt(cyclonPartners.size()));
            } else {
                peer = tmanPartners.get(rand.nextInt(tmanPartners.size()));
            }

            List< PeerAddress> buf = new ArrayList<PeerAddress>();
            buf.addAll(tmanPartners);
            buf.add(self);
            buf.addAll(cyclonPartners);

            trigger(new TManUpdateRequest(buf, self, peer), networkPort);
        }
    };
//-------------------------------------------------------------------	
    Handler<TManUpdateRequest> handleTManUpdateRequest = new Handler<TManUpdateRequest>() {
        @Override
        public void handle(TManUpdateRequest event) {
            List<PeerAddress> bufPeer = event.getBuffer();
            PeerAddress peer = event.getPeerSource();

            List< PeerAddress> buf = new ArrayList<PeerAddress>();
            buf.addAll(tmanPartners);
            buf.add(self);
            buf.addAll(cyclonPartners);

            trigger(new TManUpdateResponse(buf, self, peer), networkPort);

            buf.clear();
            buf.addAll(tmanPartners);
            buf.addAll(bufPeer);

            //TODO
            
            Snapshot.updateTManPartners(self, tmanPartners);
            Snapshot.updateSuccPred(self, succ, pred);
        }
    };
//-------------------------------------------------------------------	
    Handler<TManUpdateResponse> handleTManUpdateResponse = new Handler<TManUpdateResponse>() {
        @Override
        public void handle(TManUpdateResponse event) {
            List<PeerAddress> bufPeer = event.getBuffer();

            List<PeerAddress> buf = new ArrayList<PeerAddress>();
            buf.addAll(tmanPartners);
            buf.addAll(bufPeer);

            //TODO
            
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

    private int ranking(PeerAddress a, PeerAddress b) {
        return 0;
    }
}
