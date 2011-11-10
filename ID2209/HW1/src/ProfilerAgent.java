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
import java.util.List;

/**
 *
 * @author julio
 */
public class ProfilerAgent extends Agent {
    //private ProfilerUi gui;
    private int guideTimeDelay;
    private List<String> availableTours;
    private List<String> currentTourArtifacts;
    private List<String> visitedArtifacts;
    private List<String> currentTour;
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
        currentTour = new ArrayList<String>();
        availableTours = new ArrayList<String>();
        currentTourArtifacts = new ArrayList<String>();
        visitedArtifacts = new ArrayList<String>();
        guideTimeDelay = 1000;
        

        // TODO. get Preferences from command line argument
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
                public void action() {

                    System.out.println(myAgent.getAID().getName() + " searching tour guides...");
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("tour-guide");
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
                            System.out.println(tourGuideAgents[i].getName());
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }
                }
            });

            // send requests
            addSubBehaviour(new WakerBehaviour(aAgent, guideTimeDelay ) {
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
                        System.out.println("Proposal received from:" + myAgent.getAID().getName());
                        if (reply.getPerformative() == ACLMessage.PROPOSE) {
                            handleTourMessageContent(reply.getContent());
                            System.out.println(reply.getContent());
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

            // search curator
            addSubBehaviour(new OneShotBehaviour(aAgent) {
                @Override
                public void action() {
                    System.out.println(myAgent.getAID().getName() + " searching curator agent...");

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("curator");

                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        if (result.length == 0) {
                            System.out.println("No curator found");
                            myAgent.doDelete();
                            return;
                        }

                        curator = result[0].getName();

                        System.out.println("Found the following Curator:");
                        System.out.println("  ->  " + curator.getName());
                    } catch (FIPAException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // request information of artifacts to curators.
            addSubBehaviour(new OneShotBehaviour(aAgent) {
                public void action() {

                    System.out.println(myAgent.getAID().getName() + " request curator about artifact info...");

                    // Send the requests to curator
                    for (String artifactItem : currentTour) {
                        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                        req.addReceiver(curator);
                        req.setContent(artifactItem);
                        req.setConversationId("info-request");
                        req.setReplyWith("inf" + artifactItem);
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
                        System.out.println("Artifact info received from:" + myAgent.getAID().getName());
                        if (reply.getPerformative() == ACLMessage.CONFIRM) {
                            try {
                                Artifact currentArtifact = (Artifact) reply.getContentObject();
                                System.out.println("Received details information from artifact:  " + currentArtifact.getName());
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
                    return artifactsInfoReceived == currentTour.size();
                }
            });
        }

        private void handleTourMessageContent(String aContent) {
            if (!aContent.equals("")) {
                String[] artifactList = aContent.split(";");
                for (String currentArtifact : artifactList) {
                    if (!currentTour.contains(currentArtifact) && !visitedArtifacts.contains(currentArtifact)) {
                        currentTour.add(currentArtifact);
                    }
                }
            }
        }
    }
}
