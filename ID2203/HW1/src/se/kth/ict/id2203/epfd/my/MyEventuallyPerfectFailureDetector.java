/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfd.my;

import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.epfd.EventuallyPerfectFailureDetector;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author Igor
 */
public class MyEventuallyPerfectFailureDetector extends ComponentDefinition {

    Negative<EventuallyPerfectFailureDetector> epfd = provides(EventuallyPerfectFailureDetector.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
}
