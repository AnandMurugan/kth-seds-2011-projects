/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure.pfd.simple;

import java.util.HashSet;
import java.util.Set;
import se.kth.ict.id2203.detectors.failure.CheckTimeout;
import se.kth.ict.id2203.detectors.failure.pfd.Crash;
import se.kth.ict.id2203.detectors.failure.HeartbeatMessage;
import se.kth.ict.id2203.detectors.failure.HeartbeatTimeout;
import se.kth.ict.id2203.detectors.failure.pfd.PerfectFailureDetector;
import se.kth.ict.id2203.link.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.link.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author julio
 */
public class SimplePerfectFailureDetector extends ComponentDefinition {
    Negative<PerfectFailureDetector> pfd = provides(PerfectFailureDetector.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    private Set<Address> neighborNodes;
    private Set<Address> aliveNodes;
    private Set<Address> detectedNodes;
    private int heartbeatInterval;
    private int checkInterval;
    private Address self;

    public SimplePerfectFailureDetector() {
        subscribe(handleInit, control);
        subscribe(handleHeartbeatTimeout, timer);
        subscribe(handleCheckTimeout, timer);
        subscribe(handleHeartbeatDeliver, pp2p);
        detectedNodes = new HashSet<Address>();
        aliveNodes = new HashSet<Address>();
    }
    Handler<SimplePerfectFailureDetectorInit> handleInit = new Handler<SimplePerfectFailureDetectorInit>() {
        @Override
        public void handle(SimplePerfectFailureDetectorInit e) {
            neighborNodes = e.getNeighborSet();
            self = e.getSelf();
            heartbeatInterval = e.getHeartbeatInterval();
            checkInterval = e.getCheckInterval();

            aliveNodes.addAll(neighborNodes);

            ScheduleTimeout hbt = new ScheduleTimeout(heartbeatInterval);
            hbt.setTimeoutEvent(new HeartbeatTimeout(hbt));
            trigger(hbt, timer);

            ScheduleTimeout ct = new ScheduleTimeout(heartbeatInterval + checkInterval);
            ct.setTimeoutEvent(new CheckTimeout(ct));
            trigger(ct, timer);
        }
    };
    Handler<HeartbeatTimeout> handleHeartbeatTimeout = new Handler<HeartbeatTimeout>() {
        @Override
        public void handle(HeartbeatTimeout e) {
            for (Address addr : neighborNodes) {
                trigger(new Pp2pSend(addr, new HeartbeatMessage(self)), pp2p);
            }

            ScheduleTimeout hbt = new ScheduleTimeout(heartbeatInterval);
            hbt.setTimeoutEvent(new HeartbeatTimeout(hbt));
            trigger(hbt, timer);
        }
    };
    Handler<CheckTimeout> handleCheckTimeout = new Handler<CheckTimeout>() {
        @Override
        public void handle(CheckTimeout e) {
            for (Address addr : neighborNodes) {
                if (!aliveNodes.contains(addr) && (!detectedNodes.contains(addr))) {
                    detectedNodes.add(addr);
                    trigger(new Crash(addr), pfd);
                }
            }

            aliveNodes.clear();
            ScheduleTimeout ct = new ScheduleTimeout(heartbeatInterval + checkInterval);
            ct.setTimeoutEvent(new CheckTimeout(ct));
            trigger(ct, timer);
        }
    };
    Handler<HeartbeatMessage> handleHeartbeatDeliver = new Handler<HeartbeatMessage>() {
        @Override
        public void handle(HeartbeatMessage e) {
            aliveNodes.add(e.getSource());
        }
    };
}
