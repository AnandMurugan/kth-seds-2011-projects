/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
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
public final class PFD extends ComponentDefinition {
    Negative<PerfectFailureDetector> pfd = provides(PerfectFailureDetector.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    private Set<Address> neighborNodes;
    private List<Address> aliveNodes;
    private List<Address> detectedNodes;
    private int heartbeatInterval;
    private int checkInterval;
    private Address self;

    public PFD() {
        subscribe(handleInit, control);
        subscribe(handleHeartbeatTimeout, timer);
        subscribe(handleCheckTimeout, timer);
        subscribe(handleHeartbeatDeliver, pfd);
        detectedNodes = new ArrayList<Address>();
    }
    Handler<PfdInit> handleInit = new Handler<PfdInit>() {
        @Override
        public void handle(PfdInit e) {
            neighborNodes = e.getNeighborSet();
            self = e.getSelf();
            heartbeatInterval = e.getHeartbeatInterval();
            checkInterval = e.getCheckInterval();

            for (Address addr : neighborNodes) {
                aliveNodes.add(addr);
            }

            ScheduleTimeout hbt = new ScheduleTimeout(heartbeatInterval);
            hbt.setTimeoutEvent(new HeartbeatTimeout(hbt));
            trigger(hbt, timer);

            ScheduleTimeout ct = new ScheduleTimeout(heartbeatInterval + checkInterval);
            ct.setTimeoutEvent(new HeartbeatTimeout(ct));
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
                    trigger(new PfdCrash(addr), pfd);
                }
            }

            aliveNodes.clear();
            ScheduleTimeout ct = new ScheduleTimeout(heartbeatInterval + checkInterval);
            ct.setTimeoutEvent(new HeartbeatTimeout(ct));
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
