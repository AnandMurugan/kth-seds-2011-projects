/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfd;

import java.util.ArrayList;
import java.util.List;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
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
    private List<Address> neighborNodes;
    private List<Address> aliveNodes;
    private List<Address> detectedNodes;
    private int heartbeatInterval = 1000;
    private int checkInterval = 5000;

    public PFD() {
        subscribe(handleInit, control);
        subscribe(handleHeartbeatTimeout, timer);
        subscribe(handleCheckTimeout, timer);
    }
    Handler<PfdInit> handleInit = new Handler<PfdInit>() {
        @Override
        public void handle(PfdInit e) {
            //  aliveNodes = neighborNodes;
            detectedNodes = new ArrayList<Address>();
            aliveNodes = neighborNodes;

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
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    Handler<CheckTimeout> handleCheckTimeout = new Handler<CheckTimeout>() {
        @Override
        public void handle(CheckTimeout e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
}
