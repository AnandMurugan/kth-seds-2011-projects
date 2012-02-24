/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.fd.epfd.simple;

import java.util.HashSet;
import java.util.Set;
import se.kth.ict.id2203.fd.epfd.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.fd.epfd.Restore;
import se.kth.ict.id2203.fd.epfd.Suspect;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pSend;
import se.kth.ict.id2203.fd.CheckTimeout;
import se.kth.ict.id2203.fd.HeartbeatMessage_FairLoss;
import se.kth.ict.id2203.fd.HeartbeatTimeout;
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
public class SimpleEventuallyPerfectFailureDetector_FairLoss extends ComponentDefinition {
    //ports
    Negative<EventuallyPerfectFailureDetector> epfd = provides(EventuallyPerfectFailureDetector.class);
    Positive<FairLossPointToPointLink> flp2p = requires(FairLossPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    //local variables
    private Address self;
    private Set<Address> neighborSet;
    private Set<Address> alive;
    private Set<Address> suspected;
    private int period;
    private int timeDelay;
    private int delta;

    public SimpleEventuallyPerfectFailureDetector_FairLoss() {
        subscribe(initHandler, control);
        subscribe(heartbeatMessageHandler, flp2p);
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
                trigger(new Flp2pSend(p, new HeartbeatMessage_FairLoss(self)), flp2p);
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
    Handler<HeartbeatMessage_FairLoss> heartbeatMessageHandler = new Handler<HeartbeatMessage_FairLoss>() {
        @Override
        public void handle(HeartbeatMessage_FairLoss event) {
            alive.add(event.getSource());
        }
    };
}
