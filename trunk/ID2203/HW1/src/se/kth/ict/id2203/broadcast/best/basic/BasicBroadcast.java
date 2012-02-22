/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.best.basic;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.broadcast.best.BebBroadcast;
import se.kth.ict.id2203.broadcast.best.BestEffortBroadcast;
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
public class BasicBroadcast extends ComponentDefinition {
    //ports
    Negative<BestEffortBroadcast> beb = provides(BestEffortBroadcast.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    //local variables
    private Address self;
    private Set<Address> neighborSet;
    public BasicBroadcast() {
        subscribe(initHandler, control);
        subscribe(bebBroadcastHandler, beb);
        subscribe(BebMessageHandler, pp2p);
    }
    //handlers
    Handler<BasicBroadcastInit> initHandler = new Handler<BasicBroadcastInit>() {
        @Override
        public void handle(BasicBroadcastInit event) {
            self = event.getSelf();
            neighborSet = event.getNeighborSet();
        }
    };
    Handler<BebBroadcast> bebBroadcastHandler = new Handler<BebBroadcast>() {
        @Override
        public void handle(BebBroadcast event) {
            BebMessage bm = new BebMessage(self, event.getDeliverEvent());
            for (Address p : neighborSet) {
                trigger(new Pp2pSend(p, bm), pp2p);
            }
            trigger(new Pp2pSend(self, bm), pp2p);
        }
    };
    Handler<BebMessage> BebMessageHandler = new Handler<BebMessage>() {
        @Override
        public void handle(BebMessage event) {
            trigger(event.getDeliverEvent(), beb);
        }
    };
}
