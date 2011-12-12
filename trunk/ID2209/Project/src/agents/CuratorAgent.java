/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import items.MuseumItem;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class CuratorAgent extends Agent {
    @Override
    protected void setup() {
        //Register service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("curator");
        sd.setName("curator");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }

        //Find Inventory agent
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sdInventory = new ServiceDescription();
        sdInventory.setType("inventory");

        template.addServices(sdInventory);
        AID inventoryAgent = null;
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            if (result.length == 0) {
                this.doDelete();
                return;
            }

            inventoryAgent = result[0].getName();
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new CuratorBehaviour(this, inventoryAgent));
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    private class CuratorBehaviour extends CyclicBehaviour {
        AID inventoryAgent;

        public CuratorBehaviour(Agent a, AID inventoryAgent) {
            super(a);
            this.inventoryAgent = inventoryAgent;
        }

        @Override
        public void action() {
            ACLMessage msg1 = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
            if (msg1 != null) {
                String id = msg1.getContent();
                ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                msg2.setContent(id);
                msg2.addReceiver(inventoryAgent);
                myAgent.send(msg2);

                ACLMessage reply2 = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                MuseumItem item = null;
                try {
                    item = (MuseumItem) reply2.getContentObject();
                } catch (UnreadableException ex) {
                    Logger.getLogger(CuratorAgent.class.getName()).log(Level.SEVERE, null, ex);
                }

                ACLMessage reply1 = msg1.createReply();
                reply1.setPerformative(ACLMessage.INFORM);
                try {
                    reply1.setContentObject(item);
                } catch (IOException ex) {
                    Logger.getLogger(CuratorAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                myAgent.send(reply1);
            } else {
                block();
            }
        }
    }
}
