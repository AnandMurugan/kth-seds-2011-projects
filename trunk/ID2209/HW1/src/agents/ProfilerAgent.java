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
    //private ProfilerUi gui;
    private int guideTimeDelay;
    private List<Long> visitedArtifacts;
    private Map<Long, AID> artifactsToVisit;
    AID[] tourGuideAgents;
    AID curator;
    // Preferences to be moved to class Profile
    int age = 30;
    String interest = "history";
    String gender = "male";
    String occupation = "historian";
    String style = "cubism";

    @Override
    protected void setup() {
        //gui = new ProfilerUi(this);
        //gui.setVisible(true);
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

        System.out.println("Hello! Profiler " + getAID().getName() + " is ready.");

        addBehaviour(new ProfilerBehaviour(this));
    }

    // Put agent clean-up operations here 
    @Override
    protected void takeDown() {     // Close the GUI 
        //gui.dispose();
        // Printout a dismissal message 
        System.out.println("Profiler-Agent " + getAID().getName() + " terminating.");
    }

    public void requestNewTour() {
        //gui.updateTour();
    }

    private class ProfilerBehaviour extends SequentialBehaviour {
        int requests;
        int replies;
        private MessageTemplate mt;

        ProfilerBehaviour(final Agent aAgent) {
            super(aAgent);

            // search tour guides
            addSubBehaviour(new OneShotBehaviour(aAgent) {
                @Override
                public void action() {

                    System.out.println(myAgent.getAID().getName() + " searching tour guides...");
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("tourguide");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(aAgent, template);
                        if (result.length == 0) {
                            System.out.println("No Tour guides found.");
                            myAgent.doDelete();
                            return;
                        }

                        System.out.println("Tour Guides available:");
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
                    System.out.println(myAgent.getAID().getName() + " sending request to Tour guides...");
                    // Send the cfp to all sellers
                    ACLMessage cfp = new ACLMessage(ACLMessage.REQUEST);
                    for (int i = 0; i < tourGuideAgents.length; ++i) {
                        cfp.addReceiver(tourGuideAgents[i]);
                        System.out.println("  -> Message to: " + tourGuideAgents[i].getName());
                    }

                    cfp.setContent(age + "," + style);
                    cfp.setConversationId("tour-request");
                    cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
                    myAgent.send(cfp);

                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("tour-request"),
                            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                }
            });

            // receive artifact tour proposals
            addSubBehaviour(new Behaviour(aAgent) {
                int tourGuideAnswers = 0;

                @Override
                public void action() {

                    ACLMessage reply = myAgent.receive(mt);
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
                        tourGuideAnswers++;
                    } else {
                        block();
                    }
                }

                @Override
                public boolean done() {
                    return tourGuideAnswers == tourGuideAgents.length;
                }
            });

            // request information of artifacts to curators.
            addSubBehaviour(new OneShotBehaviour(aAgent) {
                @Override
                public void action() {

                    System.out.println(myAgent.getAID().getName() + " request curator about artifact info...");

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
                    mt = MessageTemplate.MatchConversationId("info-request");
                }
            });

            // retrieve artifacts info
            addSubBehaviour(new Behaviour(aAgent) {
                int artifactsInfoReceived = 0;

                @Override
                public void action() {
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        System.out.println(myAgent.getAID().getName() + ": Artifact info received from: " + reply.getSender().getName());
                        if (reply.getPerformative() == ACLMessage.CONFIRM) {
                            try {
                                Artifact currentArtifact = (Artifact) reply.getContentObject();
                                System.out.println("\tReceived details information from artifact:  " + currentArtifact.getName());
                                System.out.println("\tid:" + currentArtifact.getId());
                                System.out.println("\tName: " + currentArtifact.getName());
                                System.out.println("\tStyle: " + currentArtifact.getStyle());
                                System.out.println("\tmuseum:" + currentArtifact.getMuseum());
                                System.out.println("\tDescription: " + currentArtifact.getDescription());
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
