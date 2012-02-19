/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.beb.basic;

import java.util.Set;
import se.kth.ict.id2203.broadcast.beb.BebBroadcast;
import se.kth.ict.id2203.broadcast.beb.BebDeliver;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class BasicBestEffortBroadcast extends ComponentDefinition {
    //ports
    Negative<BestEffortBroadcast> un = provides(BestEffortBroadcast.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    //local variables
    private Address self;
    private Set<Address> neighborSet;

    public BasicBestEffortBroadcast() {
        subscribe(initHandler, control);
        subscribe(unBroadcastHandler, un);
        subscribe(simpleMessageHandler, pp2p);
    }
    //handlers
    Handler<BasicBestEffortBroadcastInit> initHandler = new Handler<BasicBestEffortBroadcastInit>() {
        @Override
        public void handle(BasicBestEffortBroadcastInit event) {
            self = event.getSelf();
            neighborSet = event.getNeighborSet();
        }
    };
    Handler<BebBroadcast> unBroadcastHandler = new Handler<BebBroadcast>() {
        @Override
        public void handle(BebBroadcast event) {
            BasicMessage dm = new BasicMessage(self, event.getMessage());
            for (Address p : neighborSet) {
                trigger(new Pp2pSend(p, dm), pp2p);
            }
            trigger(new Pp2pSend(self, dm), pp2p);
        }
    };
    Handler<BasicMessage> simpleMessageHandler = new Handler<BasicMessage>() {
        @Override
        public void handle(BasicMessage event) {
            trigger(new BebDeliver(event.getSource(), event.getMessage()), un);
        }
    };
}
