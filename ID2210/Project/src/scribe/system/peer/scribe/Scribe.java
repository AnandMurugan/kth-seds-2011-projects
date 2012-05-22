package scribe.system.peer.scribe;

import common.peer.PeerAddress;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;

import scribe.simulator.snapshot.Snapshot;
import se.sics.kompics.timer.ScheduleTimeout;
import tman.system.peer.tman.TManPartnersPort;
import tman.system.peer.tman.TManPartnersRequest;
import tman.system.peer.tman.TManPartnersResponse;

public final class Scribe extends ComponentDefinition {
    Negative<ScribePort> scribePort = negative(ScribePort.class);
    Positive<TManPartnersPort> tmanPartnersPort = positive(TManPartnersPort.class);
    Positive<Network> networkPort = positive(Network.class);
    Positive<Timer> timerPort = positive(Timer.class);
    private long period;
    private PeerAddress self;
    private ArrayList<BigInteger> topics;
    private ArrayList<PeerAddress> tmanPartners;
    private HashMap<BigInteger, ArrayList<PeerAddress>> children;
    private long stabilizationPeriod = 10000;

//-------------------------------------------------------------------	
    public Scribe() {
        topics = new ArrayList<BigInteger>();
        tmanPartners = new ArrayList<PeerAddress>();
        children = new HashMap<BigInteger, ArrayList<PeerAddress>>();

        subscribe(handleInit, control);
        subscribe(handleRequestTManPartners, timerPort);
        subscribe(handleRecvTopicEvent, networkPort);
        subscribe(handleRecvTManPartners, tmanPartnersPort);
        subscribe(handlePublish, scribePort);
        subscribe(handleStabilizationTimeout, timerPort);
        subscribe(handleFwdEvent, networkPort);
    }
//-------------------------------------------------------------------	
    Handler<ScribeInit> handleInit = new Handler<ScribeInit>() {
        public void handle(ScribeInit init) {
            self = init.getSelf();
            topics = init.getTopics();
            period = init.getPeriod();

            SchedulePeriodicTimeout rst = new SchedulePeriodicTimeout(period, period);
            rst.setTimeoutEvent(new ScribeSchedule(rst));
            trigger(rst, timerPort);

            // Set timer and join the groups.
            //ScheduleTimeout stbTimer = new ScheduleTimeout(stabilizationPeriod);
            //stbTimer.setTimeoutEvent(new StabilizationTimeout(stbTimer));
            //trigger(stbTimer, timerPort);
        }
    };
//-------------------------------------------------------------------	
    Handler<PublishEvent> handlePublish = new Handler<PublishEvent>() {
        public void handle(PublishEvent event) {
            BigInteger topicId = event.getTopicId();

            

            Snapshot.publishTopicEvent(topicId);
        }
    };
//-------------------------------------------------------------------	
    Handler<TopicEvent> handleRecvTopicEvent = new Handler<TopicEvent>() {
        public void handle(TopicEvent event) {
            BigInteger topicId = event.getTopicId();

            // check if receiver

            // check if forwarder


            Snapshot.updateRecvTopicEvent(self, topicId);
        }
    };
//-------------------------------------------------------------------	
    Handler<ScribeSchedule> handleRequestTManPartners = new Handler<ScribeSchedule>() {
        public void handle(ScribeSchedule event) {
            TManPartnersRequest request = new TManPartnersRequest();
            trigger(request, tmanPartnersPort);
        }
    };
//-------------------------------------------------------------------	
    Handler<TManPartnersResponse> handleRecvTManPartners = new Handler<TManPartnersResponse>() {
        public void handle(TManPartnersResponse event) {
            tmanPartners = event.getPartners();

            // resubscribe
            for (BigInteger topicId : topics) {
                subscribe(topicId);
            }

            Snapshot.updateTManPartners(self, tmanPartners);
        }
    };
    Handler<ScribeForwardEvent> handleFwdEvent = new Handler<ScribeForwardEvent>() {
        public void handle(ScribeForwardEvent event) {
            // create the topic group if doesnt exist
            if (!children.containsKey(event.getTopicId())) {
                children.put(event.getTopicId(), new ArrayList<PeerAddress>());
            }

            //PeerAddress nextPeer = getCloserFingerNode(event.getTopicId());
            
            PeerAddress nextPeer = getNextNode(event.getTopicId());
            if (nextPeer != self) {
                    // continue forwarding
                    ScribeForwardEvent forwardEvent = new ScribeForwardEvent(event.getTopicId(), event.getPeerSource(), nextPeer, ScribeMessageType.JOIN);
                    trigger(forwardEvent, networkPort);
                
            }

            // Add to children
            if (!children.get(event.getTopicId()).contains(event.getPeerSource())) {
                children.get(event.getTopicId()).add(event.getPeerSource());
            }
        }
    };
    Handler<ScribeDeliverEvent> handleDeliverEvent = new Handler<ScribeDeliverEvent>() {
        public void handle(ScribeDeliverEvent event) {
        }
    };
    Handler<StabilizationTimeout> handleStabilizationTimeout = new Handler<StabilizationTimeout>() {
        public void handle(StabilizationTimeout event) {
            // join all topics
            for (BigInteger topicId : topics) {
                PeerAddress closerPeer = getCloserFingerNode(topicId);

                ScribeForwardEvent forwardEvent = new ScribeForwardEvent(topicId, self, closerPeer, ScribeMessageType.JOIN);
                trigger(forwardEvent, networkPort);
            }
        }
    };

    private PeerAddress getCloserFingerNode(BigInteger topicId) {
        BigInteger rvpId = null;
        PeerAddress rvpAddr = null;
        int tailIndex = 0;

        if (tmanPartners.isEmpty()) {
            return rvpAddr;
        }

        for (int i = 0; i < tmanPartners.size(); i++) {

            if (tmanPartners.get(i).getPeerId().compareTo(topicId) < 0) {
                rvpAddr = tmanPartners.get(i);
            }

            if ((i < tmanPartners.size() - 1) && (tmanPartners.get(i).getPeerId().compareTo(tmanPartners.get(i + 1).getPeerId()) > 0)) {
                tailIndex = i;
            }
        }

        if (rvpAddr == null) {
            rvpAddr = tmanPartners.get(tailIndex);
        }

        return rvpAddr;
    }

    // Returns self in case tmanpartners is null or self is closer to topicId
    private PeerAddress getNextNode(BigInteger topicId) {

        PeerAddress finger = getCloserFingerNode(topicId);

        if (finger == null) {
            return self;
        }

        // self is tail and is closer
        if (self.getPeerId().compareTo(topicId) < 0 && self.getPeerId().compareTo(finger.getPeerId()) > 0) {
            return self;
        }

        // self is closer
        if (finger.getPeerId().compareTo(topicId) > 0 && self.getPeerId().compareTo(topicId) < 0) {
            return self;
        }

        return finger;
    }

    private void subscribe(BigInteger topicId) {
        PeerAddress closerPeer = getNextNode(topicId);

        if (closerPeer != self) {
            ScribeForwardEvent forwardEvent = new ScribeForwardEvent(topicId, self, closerPeer, ScribeMessageType.JOIN);
            trigger(forwardEvent, networkPort);

        }
    }
}
