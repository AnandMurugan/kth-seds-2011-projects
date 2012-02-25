/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure.epfd.simple;

import java.util.HashSet;
import java.util.Set;
import se.kth.ict.id2203.detectors.failure.epfd.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.detectors.failure.epfd.Restore;
import se.kth.ict.id2203.detectors.failure.epfd.Suspect;
import se.kth.ict.id2203.detectors.failure.CheckTimeout;
import se.kth.ict.id2203.detectors.failure.HeartbeatMessage;
import se.kth.ict.id2203.detectors.failure.HeartbeatTimeout;
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
public class SimpleEventuallyPerfectFailureDetector extends ComponentDefinition {
    //ports
    Negative<EventuallyPerfectFailureDetector> epfd = provides(EventuallyPerfectFailureDetector.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    //local variables
    private Address self;
    private Set<Address> neighborSet;
    private Set<Address> alive;
    private Set<Address> suspected;
    private int period;
    private int timeDelay;
    private int delta;

    public SimpleEventuallyPerfectFailureDetector() {
        subscribe(initHandler, control);
        subscribe(heartbeatMessageHandler, pp2p);
        subscribe(checkTimeoutHandler, timer);
        subscribe(heartbeatTimeoutHandler, timer);
    }
    //handlers
    Handler<SimpleEventuallyPerfectFailureDetectorInit> initHandler = new Handler<SimpleEventuallyPerfectFailureDetectorInit>() {
        @Override
        public void handle(SimpleEventuallyPerfectFailureDetectorInit event) {
            self = event.getSelf();
            neighborSet = event.getNeighborSet();
            alive = new HashSet<Address>(event.getNeighborSet());
            suspected = new HashSet<Address>();

            timeDelay = event.getTimeDelay();
            period = timeDelay;
            delta = event.getDelta();

            ScheduleTimeout heartbeat = new ScheduleTimeout(timeDelay);
            heartbeat.setTimeoutEvent(new HeartbeatTimeout(heartbeat));
            trigger(heartbeat, timer);

            ScheduleTimeout check = new ScheduleTimeout(period);
            check.setTimeoutEvent(new CheckTimeout(check));
            trigger(check, timer);
        }
    };
    Handler<HeartbeatTimeout> heartbeatTimeoutHandler = new Handler<HeartbeatTimeout>() {
        @Override
        public void handle(HeartbeatTimeout event) {
            for (Address p : neighborSet) {
                trigger(new Pp2pSend(p, new HeartbeatMessage(self)), pp2p);
            }

            ScheduleTimeout heartbeat = new ScheduleTimeout(timeDelay);
            heartbeat.setTimeoutEvent(new HeartbeatTimeout(heartbeat));
            trigger(heartbeat, timer);
        }
    };
    Handler<CheckTimeout> checkTimeoutHandler = new Handler<CheckTimeout>() {
        @Override
        public void handle(CheckTimeout event) {
            Set<Address> intersection = new HashSet<Address>(alive);
            intersection.retainAll(suspected);
            if (intersection.size() > 0) {
                period += delta;
            }

            for (Address p : neighborSet) {
                if ((!alive.contains(p)) && (!suspected.contains(p))) {
                    suspected.add(p);
                    trigger(new Suspect(p, period), epfd);
                } else if ((alive.contains(p)) && (suspected.contains(p))) {
                    suspected.remove(p);
                    trigger(new Restore(p, period), epfd);
                }
            }

            alive.clear();

            ScheduleTimeout check = new ScheduleTimeout(period);
            check.setTimeoutEvent(new CheckTimeout(check));
            trigger(check, timer);
        }
    };
    Handler<HeartbeatMessage> heartbeatMessageHandler = new Handler<HeartbeatMessage>() {
        @Override
        public void handle(HeartbeatMessage event) {
            alive.add(event.getSource());
        }
    };
}
