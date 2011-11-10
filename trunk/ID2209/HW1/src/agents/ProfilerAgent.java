package agents;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Artifact;

/**
 *
 * @author julio
 */
public class ProfilerAgent extends Agent {
    private int guideTimeDelay;
    private List<Long> visitedArtifacts;
    private Map<Long, AID> artifactsToVisit;
    private AID[] tourGuideAgents;
    // Preferences
    private int age;
    private String style;

    @Override
    protected void setup() {
        visitedArtifacts = new ArrayList<Long>();
        artifactsToVisit = new HashMap<Long, AID>();
        guideTimeDelay = 1000;

        Object[] args = getArguments();
        if (args != null && args.length > 1) {
            age = Integer.parseInt((String) args[0]);
            style = (String) args[1];
        } else {
            doDelete();
        }

        System.out.println("Hello! Profiler " + getAID().getName() + " is ready...");
        addBehaviour(new ProfilerBehaviour(this));
    }

    // Put agent clean-up operations here 
    @Override
    protected void takeDown() {
        System.out.println("!!!!!!!!!!!!!!!! Profiler " + getAID().getName() + " terminating...");
    }

    public void requestNewTour() {
        //gui.updateTour();
    }

    private class ProfilerBehaviour extends SequentialBehaviour {
        int requests;
        int replies;
        private MessageTemplate msgTemplate;

        ProfilerBehaviour(final Agent aAgent) {
            super(aAgent);

            // search tour guides
            addSubBehaviour(new OneShotBehaviour(aAgent) {
                @Override
                public void action() {

                    System.out.println(myAgent.getAID().getName() + " is searching for tour-guides...");
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("tourguide");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(aAgent, template);
                        if (result.length == 0) {
                            System.out.println(myAgent.getAID().getName() + ": ERROR - No tour-guides found...");
                            myAgent.doDelete();
                            return;
                        }

                        System.out.println(myAgent.getAID().getName() + " has found the next tour-guides:");
                        tourGuideAgents = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            tourGuideAgents[i] = result[i].getName();
                            System.out.println("\t" + tourGuideAgents[i].getName());
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }
                }
            });

            // send requests
            addSubBehaviour(new WakerBehaviour(aAgent, guideTimeDelay) {
                @Override
                protected void onWake() {
                    System.out.println(myAgent.getAID().getName() + " is sending requests to tour-guides...");
                    // Send the cfp to all sellers
                    ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                    for (int i = 0; i < tourGuideAgents.length; ++i) {
                        req.addReceiver(tourGuideAgents[i]);
                        System.out.println("\tTour request to: " + tourGuideAgents[i].getName());
                    }

                    req.setContent(age + "," + style);
                    req.setConversationId("tour-request");
                    req.setReplyWith(Long.toString(System.currentTimeMillis()));
                    myAgent.send(req);

                    msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("tour-request"),
                            MessageTemplate.MatchInReplyTo(req.getReplyWith()));
                }
            });

            // receive artifact tour proposals
            addSubBehaviour(new Behaviour(aAgent) {
                int tourGuideResponses = 0;

                @Override
                public void action() {

                    ACLMessage reply = myAgent.receive(msgTemplate);
                    if (reply != null) {
                        System.out.println(myAgent.getAID().getName() + ": Proposal received from: " + reply.getSender().getName());
                        if (reply.getPerformative() == ACLMessage.PROPOSE) {
                            List<Entry<Long, AID>> tour = null;
                            try {
                                tour = (List<Entry<Long, AID>>) reply.getContentObject();
                            } catch (UnreadableException ex) {
                                Logger.getLogger(ProfilerAgent.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            handleTourMessageContent(tour);
                            //System.out.println(reply.getContent());
                        }
                        tourGuideResponses++;
                    } else {
                        block();
                    }
                }

                @Override
                public boolean done() {
                    return tourGuideResponses == tourGuideAgents.length;
                }
            });

            // request information of artifacts to curators.
            addSubBehaviour(new OneShotBehaviour(aAgent) {
                @Override
                public void action() {

                    System.out.println(myAgent.getAID().getName() + " is requesting curator for artifact info...");

                    for (Entry<Long, AID> entry : artifactsToVisit.entrySet()) {
                        Long artifactItem = entry.getKey();
                        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                        req.addReceiver(entry.getValue());
                        req.setContent("id;" + artifactItem);
                        req.setConversationId("info-request");
                        req.setReplyWith("info-" + artifactItem);
                        myAgent.send(req);
                    }

                    // Prepare the template to get proposals
                    msgTemplate = MessageTemplate.MatchConversationId("info-request");
                }
            });

            // retrieve artifacts info
            addSubBehaviour(new Behaviour(aAgent) {
                int artifactsInfoReceived = 0;

                @Override
                public void action() {
                    ACLMessage reply = myAgent.receive(msgTemplate);
                    if (reply != null) {
                        System.out.println(myAgent.getAID().getName() + ": Artifact info received from: " + reply.getSender().getName());
                        if (reply.getPerformative() == ACLMessage.CONFIRM) {
                            try {
                                Artifact currentArtifact = (Artifact) reply.getContentObject();
                                System.out.println("\tReceived details information from artifact: " + currentArtifact.getName());
                                System.out.println("\t\tID:\t\t" + currentArtifact.getId());
                                System.out.println("\t\tName:\t\t" + currentArtifact.getName());
                                System.out.println("\t\tCreator:\t" + currentArtifact.getCreator());
                                System.out.println("\t\tStyle:\t\t" + currentArtifact.getStyle());
                                System.out.println("\t\tMuseum:\t\t" + currentArtifact.getMuseum());
                                System.out.println("\t\tDescription:\t" + currentArtifact.getDescription());
                            } catch (UnreadableException uex) {
                                uex.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        artifactsInfoReceived++;
                    } else {
                        block();
                    }
                }

                @Override
                public boolean done() {
                    return artifactsInfoReceived == artifactsToVisit.size();
                }
            });
        }

        private void handleTourMessageContent(List<Entry<Long, AID>> tour) {
            if (tour != null) {
                for (Entry<Long, AID> e : tour) {
                    if (!artifactsToVisit.containsKey(e.getKey()) && !visitedArtifacts.contains(e.getKey())) {
                        artifactsToVisit.put(e.getKey(), e.getValue());
                    }
                }
            }
        }
    }
}
