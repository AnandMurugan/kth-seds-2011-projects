package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import model.Artifact;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Igor
 */
public class CuratorAgent extends Agent {
    private String museum;
    private List<Artifact> artifacts = new ArrayList<Artifact>();
    private static long counter = 1;

    @Override
    protected void setup() {
        //Setting the museum name
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            museum = (String) args[0];
        } else {
            doDelete();
        }

        //Adding artifacts to curator
        Artifact a;

        a = new Artifact("Water-lilies", "Monet", "A real masterpiece by Monet", "impressionism", 20, 50, museum);
        artifacts.add(a);

        a = new Artifact("Mona Lisa", "da Vinci", "A real masterpiece by da Vinci", "reneissance", 10, 25, museum);
        artifacts.add(a);

        a = new Artifact("Campbell Soup", "Warhall", "A real masterpieceby Warhall", "new-age", 15, 50, museum);
        artifacts.add(a);

        System.out.println("Hello! Curator " + getAID().getName() + " in museum " + museum + " is ready...");

        //Register service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("curator");
        sd.setName(museum + "_curator" + (counter++));
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }

        addBehaviour(new CuratorBehaviour());
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    private class CuratorBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
            if (msg != null) {
                System.out.println(getAID().getName() + ": Message received...");
                ACLMessage reply = msg.createReply();

                //process message and send reply
                StringTokenizer st = new StringTokenizer(msg.getContent(), ";");
                String request = st.nextToken();
                if (request.equals("items")) {
                    String m = st.nextToken();
                    if (m.equals(museum)) {
                        System.out.println(getAID().getName() + ": Some tour-guide is asking for my artifacts...");

                        reply.setPerformative(ACLMessage.CONFIRM);
                        try {
                            reply.setContentObject((Serializable) artifacts);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println(getAID().getName() + ": That was not " + museum + "'s tour-guide...");
                        return;
                    }
                } else if (request.equals("id")) {
                    System.out.println(getAID().getName() + ": Some profiler is asking for artifact details...");

                    reply.setPerformative(ACLMessage.CONFIRM);
                    long id = Long.parseLong(st.nextToken());
                    Artifact toReply = null;
                    for (Artifact a : artifacts) {
                        if (a.getId() == id) {
                            toReply = a;
                            break;
                        }
                    }

                    try {
                        reply.setContentObject((Serializable) toReply);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(getAID().getName() + ": Wrong message...");

                    // content not understood
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("Content not understood");
                }

                myAgent.send(reply);
                System.out.println(getAID().getName() + " is sending response...");
            } else {
                block();
            }
        }
    }
}
