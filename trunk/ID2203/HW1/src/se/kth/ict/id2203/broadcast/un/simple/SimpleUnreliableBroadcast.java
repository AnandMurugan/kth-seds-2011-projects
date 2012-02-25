/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un.simple;

import java.util.Set;
import se.kth.ict.id2203.broadcast.un.UnBroadcast;
import se.kth.ict.id2203.broadcast.un.UnreliableBroadcast;
import se.kth.ict.id2203.links.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.links.flp2p.Flp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class SimpleUnreliableBroadcast extends ComponentDefinition {
    //ports
    Negative<UnreliableBroadcast> un = provides(UnreliableBroadcast.class);
    Positive<FairLossPointToPointLink> flp2p = requires(FairLossPointToPointLink.class);
    //local variables
    private Address self;
    private Set<Address> neighborSet;

    public SimpleUnreliableBroadcast() {
        subscribe(initHandler, control);
        subscribe(unBroadcastHandler, un);
        subscribe(simpleMessageHandler, flp2p);
    }
    //handlers
    Handler<SimpleUnreliableBroadcastInit> initHandler = new Handler<SimpleUnreliableBroadcastInit>() {
        @Override
        public void handle(SimpleUnreliableBroadcastInit event) {
            self = event.getSelf();
            neighborSet = event.getNeighborSet();
        }
    };
    Handler<UnBroadcast> unBroadcastHandler = new Handler<UnBroadcast>() {
        @Override
        public void handle(UnBroadcast event) {
            SimpleUnreliableBroadcastMessage m = new SimpleUnreliableBroadcastMessage(self, event.getDeliverEvent());
            for (Address p : neighborSet) {
                trigger(new Flp2pSend(p, m), flp2p);
            }
            trigger(new Flp2pSend(self, m), flp2p);
        }
    };
    Handler<SimpleUnreliableBroadcastMessage> simpleMessageHandler = new Handler<SimpleUnreliableBroadcastMessage>() {
        @Override
        public void handle(SimpleUnreliableBroadcastMessage event) {
            trigger(event.getDeliverEvent(), un);
        }
    };
}
