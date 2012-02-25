/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.leader.eld.failnoisy;

import java.util.HashSet;
import java.util.Set;
import se.kth.ict.id2203.detectors.failure.HeartbeatMessage;
import se.kth.ict.id2203.detectors.failure.HeartbeatTimeout;
import se.kth.ict.id2203.detectors.leader.eld.EventualLeaderDetector;
import se.kth.ict.id2203.detectors.leader.eld.Trust;
import se.kth.ict.id2203.links.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.links.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author Igor
 */
public class FailNoisyEventualLeaderDetector extends ComponentDefinition {
    //ports
    Negative<EventualLeaderDetector> eld = provides(EventualLeaderDetector.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    //local variables
    private Address self;
    private Set<Address> all;
    private long period;
    private long delta;
    private Address leader;
    private Set<Address> candidateSet;

    public FailNoisyEventualLeaderDetector() {
        subscribe(initHandler, control);
        subscribe(heartbeatMessageHandler, pp2p);
        subscribe(heartbeatTimeoutHandler, timer);
    }
    //handlers
    Handler<FailNoisyEventualLeaderDetectorInit> initHandler = new Handler<FailNoisyEventualLeaderDetectorInit>() {
        @Override
        public void handle(FailNoisyEventualLeaderDetectorInit event) {
            self = event.getSelf();
            all = event.getAll();
            period = event.getTimeDelay();
            delta = event.getDelta();

            leader = select(all);
            trigger(new Trust(leader), eld);
            HeartbeatMessage heartbeat = new HeartbeatMessage(self);
            for (Address s : all) {
                trigger(new Pp2pSend(s, heartbeat), pp2p);
            }
            candidateSet = new HashSet<Address>();

            ScheduleTimeout to = new ScheduleTimeout(period);
            to.setTimeoutEvent(new HeartbeatTimeout(to));
            trigger(to, timer);
        }
    };
    Handler<HeartbeatTimeout> heartbeatTimeoutHandler = new Handler<HeartbeatTimeout>() {
        @Override
        public void handle(HeartbeatTimeout event) {
            Address newleader = select(candidateSet);
            if (!leader.equals(newleader) && newleader != null) {
                period += delta;
                leader = newleader;
                trigger(new Trust(leader), eld);
            }

            HeartbeatMessage heartbeat = new HeartbeatMessage(self);
            for (Address s : all) {
                trigger(new Pp2pSend(s, heartbeat), pp2p);
            }
            candidateSet.clear();

            ScheduleTimeout to = new ScheduleTimeout(period);
            to.setTimeoutEvent(new HeartbeatTimeout(to));
            trigger(to, timer);
        }
    };
    Handler<HeartbeatMessage> heartbeatMessageHandler = new Handler<HeartbeatMessage>() {
        @Override
        public void handle(HeartbeatMessage event) {
            candidateSet.add(event.getSource());
        }
    };

    //procedures and functions
    private Address select(Set<Address> candidates) {
        Address leader = null;
        int rank = Integer.MAX_VALUE;
        for (Address candidate : candidates) {
            if (rank(candidate) < rank) {
                leader = candidate;
                rank = rank(candidate);
            }
        }
        return leader;
    }

    private int rank(Address node) {
        return node.getId();
    }
}
