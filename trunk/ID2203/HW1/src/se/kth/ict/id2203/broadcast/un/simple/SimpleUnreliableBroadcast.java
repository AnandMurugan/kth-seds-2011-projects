/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un.simple;

import java.util.Set;
import se.kth.ict.id2203.broadcast.un.UnBroadcast;
import se.kth.ict.id2203.broadcast.un.UnDeliver;
import se.kth.ict.id2203.broadcast.un.UnreliableBroadcast;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pSend;
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
        subscribe(dataMessageHandler, flp2p);
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
            String message = event.getMessage();
            for (Address p : neighborSet) {
                trigger(new Flp2pSend(p, new DataMessage(self, message)), flp2p);
            }
        }
    };
    Handler<DataMessage> dataMessageHandler = new Handler<DataMessage>() {
        @Override
        public void handle(DataMessage event) {
            trigger(new UnDeliver(event.getSource(), event.getMessage()), un);
        }
    };
}
