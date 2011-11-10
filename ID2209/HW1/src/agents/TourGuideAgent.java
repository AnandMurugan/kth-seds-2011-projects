package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Artifact;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Igor
 */
public class TourGuideAgent extends Agent {
    private String targetMuseum;
    private Map<Artifact, AID> artifactsAndCurators = new HashMap<Artifact, AID>();

    @Override
    protected void setup() {
        //Setting the target museum name
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            targetMuseum = (String) args[0];
        } else {
            doDelete();
        }

        System.out.println("Hello! TourGuide " + getAID().getName() + " for museum " + targetMuseum + " is ready.");

        //Register the service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("tourguide");
        sd.setName(targetMuseum + "_tourguide");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new TourGuideBehaviour(this));
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    private class TourGuideBehaviour extends ParallelBehaviour {
        public TourGuideBehaviour(Agent a) {
            super(a, ParallelBehaviour.WHEN_ALL);

            //reply to ProfilerAgent
            addSubBehaviour(new CyclicBehaviour(a) {
                @Override
                public void action() {
                    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                    ACLMessage msg = myAgent.receive(mt);
                    if (msg != null) {
                        ACLMessage reply = msg.createReply();

                        StringTokenizer st = new StringTokenizer(msg.getContent(), ",");
                        int age = Integer.parseInt(st.nextToken());
                        String style = st.nextToken();

                        System.out.println(myAgent.getAID().getName() + " got request for tour!");

                        //StringBuilder tour = new StringBuilder();
                        List<Entry<Long, AID>> tour = new ArrayList<Entry<Long, AID>>();
                        for (Entry<Artifact, AID> entry : artifactsAndCurators.entrySet()) {
                            Artifact a = entry.getKey();
                            AID aid = entry.getValue();
                            if (a.getStyle().equals(style) && (a.getMinAge() <= age) && (a.getMaxAge() >= age)) {
                                Entry<Long, AID> e = new SimpleEntry<Long, AID>(a.getId(), aid);
                                tour.add(e);
                            }
                        }

                        try {
                            reply.setContentObject((Serializable) tour);
                            reply.setPerformative(ACLMessage.PROPOSE);
                            System.out.println(myAgent.getAID().getName() + " sending tour!");
                            myAgent.send(reply);
                        } catch (IOException ex) {
                            Logger.getLogger(TourGuideAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        block();
                    }
                }
            });

            //search for CuratorAgents and ask for artifacts
            addSubBehaviour(new TickerBehaviour(a, 5000) {
                @Override
                protected void onTick() {
                    Set<AID> curators = new HashSet<AID>();

                    //Search
                    System.out.println(myAgent.getAID().getName() + " is searching for curators!");

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("curator");

                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        if (result.length == 0) {
                            System.out.println(myAgent.getAID().getName() + ": ERROR - No curators found");
                            myAgent.doDelete();
                            return;
                        }

                        for (DFAgentDescription d : result) {
                            curators.add(d.getName());
                        }


                        System.out.println(myAgent.getAID().getName() + " found the following curators:");
                        for (AID aid : curators) {
                            System.out.println("  ->  " + aid.getName());
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    //ask for artifacts
                    for (AID c : curators) {
                        System.out.println(myAgent.getAID().getName() + " is messaging the curator " + c.getName());

                        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                        req.addReceiver(c);
                        req.setContent("items;" + targetMuseum);
                        req.setConversationId("items");
                        req.setReplyWith(Long.toString(System.currentTimeMillis()));
                        myAgent.send(req);
                    }
                }
            });

            //receive artifacts
            addSubBehaviour(new CyclicBehaviour(a) {
                @Override
                public void action() {
                    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
                    ACLMessage msg = myAgent.receive(mt);
                    if (msg != null) {
                        AID curator = msg.getSender();
                        System.out.println(myAgent.getAID().getName() + " got answer from the curator " + curator.getName() + "!");
                        List<Artifact> artifacts;
                        try {
                            artifacts = (List<Artifact>) msg.getContentObject();
                            for (Artifact a : artifacts) {
                                if (!containsArtifact(a)) {
                                    artifactsAndCurators.put(a, curator);
                                }
                            }
                        } catch (UnreadableException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        block();
                    }
                }

                private boolean containsArtifact(Artifact a) {
                    boolean result = false;

                    for (Artifact aInMap : artifactsAndCurators.keySet()) {
                        if (aInMap.getId() == a.getId()) {
                            result = true;
                            break;
                        }
                    }

                    return result;
                }
            });
        }
    }
}
