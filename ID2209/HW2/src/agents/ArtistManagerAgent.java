/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import gui.ArtistManagerFrame;
import gui.ArtistManagerResponsiveGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Artifact;

/**
 *
 * @author Igor
 */
public class ArtistManagerAgent extends Agent {
    private ArtistManagerResponsiveGUI gui;

    @Override
    protected void setup() {
        //Register the service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("artist-manager");
        sd.setName(getAID().getName() + "_artist-manager");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);

            /* Create and display the form */
            final ArtistManagerAgent agent = this;
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    gui = new ArtistManagerFrame(agent);
                    gui.open();
                }
            });
        } catch (FIPAException ex) {
            doDelete();
        }

        System.out.println("Artist Manager [" + getAID().getLocalName() + "] is ready...");
//        try {
//            Thread.sleep(1000);
//            sellArtifact(new String[]{"Art", "Artman", "Just simple art", "cubism", "150", "50"});
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    protected void takeDown() {
        try {
            System.out.println("Artist Manager [" + getAID().getLocalName() + "] is terminating...");
            DFService.deregister(this);
            gui.close();
        } catch (FIPAException ex) {
            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sellArtifact(String[] args) {
        String name = args[0];
        String creator = args[1];
        String description = args[2];
        String style = args[3];
        float initialPrice = Float.parseFloat(args[4]);
        float reservePrice = Float.parseFloat(args[5]);

        Artifact a = new Artifact(name, creator, description, style);

        addBehaviour(new DutchAuctionInitiatorBehaviour(this, a, initialPrice, reservePrice));
    }

    private class DutchAuctionInitiatorBehaviour extends FSMBehaviour {
        private static final int ROUNDS = 10;
        private String conversationId;
        private AID[] curators;
        private AID winner;
        private List<AID> runnerUps = new ArrayList<AID>();
        private MessageTemplate auctionTemplate;
        private float reductionRate;
        private float currentPrice;

        public DutchAuctionInitiatorBehaviour(final Agent agent, final Artifact artifact, final float initialPrice, final float reservePrice) {
            super(agent);

            curators = new AID[0];
            conversationId = "dutch_" + System.currentTimeMillis();
            auctionTemplate = MessageTemplate.MatchConversationId(conversationId);
            reductionRate = (initialPrice - reservePrice) / (ROUNDS - 1);
            currentPrice = initialPrice;

            //Discover all curators
            registerFirstState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    gui.log("Searching for curators...");//System.out.println(myAgent.getAID().getName() + " is searching for tour-guides...");
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("curator");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        if (result.length == 0) {
                            gui.log("No curators were found...");//System.out.println(myAgent.getAID().getName() + ": ERROR - No tour-guides found...");
                            return;
                        }

                        gui.log("Following curators were found:");//System.out.println(myAgent.getAID().getName() + " has found the next tour-guides:");
                        curators = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            curators[i] = result[i].getName();
                            gui.log("\t" + curators[i].getLocalName());//System.out.println("\t" + tourGuideAgents[i].getName());
                        }
                    } catch (FIPAException ex) {
                        Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public int onEnd() {
                    return curators.length == 0 ? 0 : 1;
                }
            }, "SEARCHING_CURATORS");

            //
            registerLastState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    gui.log("Auction is terminating due to absence of any curator...\n");
                }
            }, "NO_CURATORS");
            //
            registerLastState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    //Respond to all curators
                    ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
                    informMsg.setConversationId(conversationId);
                    informMsg.setContent("terminating");
                    for (AID aid : curators) {
                        informMsg.addReceiver(aid);
                    }
                    myAgent.send(informMsg);

                    //Log
                    gui.log("Auction is terminating...");
                }
            }, "TERMINATING_AUCTION");

            //
            registerState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    //Inform all curators about artifact
                    ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
                    informMsg.setConversationId(conversationId);
                    informMsg.setContent(artifact.getStyle());
                    for (AID aid : curators) {
                        informMsg.addReceiver(aid);
                    }
                    myAgent.send(informMsg);

                    //Log
                    gui.log("Informing curators about start of auction...");
                }
            }, "INFORMING_CURATORS");
            //
            registerState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    //Log
                    gui.log("Sending curators cfp [price=" + currentPrice + "]...");

                    //Inform all curators about artifact
                    ACLMessage cfpMsg = new ACLMessage(ACLMessage.CFP);
                    cfpMsg.setConversationId(conversationId);
                    cfpMsg.setContent(Float.toString(currentPrice));
                    for (AID aid : curators) {
                        cfpMsg.addReceiver(aid);
                        gui.log("\tSending cfp to [" + aid.getLocalName() + "]");
                    }
                    myAgent.send(cfpMsg);
                    gui.log("Collecting responses...");
                }
            }, "CFP");
            //
            registerState(new Behaviour(agent) {
                int responsesCount = 0;

                @Override
                public void action() {
                    ACLMessage response = myAgent.receive(auctionTemplate);
                    if (response != null) {
                        switch (response.getPerformative()) {
                            case ACLMessage.PROPOSE:
                                if (winner == null) {
                                    winner = response.getSender();
                                } else {
                                    runnerUps.add(response.getSender());
                                }
                                break;
                            case ACLMessage.NOT_UNDERSTOOD:
                                break;
                        }
                        responsesCount++;
                        gui.log("\t#" + responsesCount + " [" + response.getSender().getLocalName() + "] responded with " + ACLMessage.getPerformative(response.getPerformative()));
                    } else {
                        block();
                    }
                }

                @Override
                public boolean done() {
                    return responsesCount == curators.length;
                }

                @Override
                public int onEnd() {
                    return winner != null ? 1 : 0;
                }
            }, "COLLECTING_PROPOSALS");
            //
            registerState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    gui.log("Planning next round...");
                    currentPrice -= reductionRate;
                }

                @Override
                public int onEnd() {
                    return (currentPrice < reservePrice) ? 1 : 0;
                }
            }, "PLANNING_NEXT_ROUND");
            //
            registerState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    //Respond to the winner
                    try {
                        ACLMessage acceptMsg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        acceptMsg.setConversationId(conversationId);
                        acceptMsg.setContentObject(artifact);
                        acceptMsg.addReceiver(winner);
                        myAgent.send(acceptMsg);
                    } catch (IOException ex) {
                        Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Respond to the runner-ups
                    ACLMessage rejectMsg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                    rejectMsg.setConversationId(conversationId);
                    for (AID runnerUp : runnerUps) {
                        rejectMsg.addReceiver(runnerUp);
                    }
                    myAgent.send(rejectMsg);

                    //Log
                    gui.log("Auction is succesful:");
                    gui.log("\tWinner is [" + winner.getLocalName() + "]");
                    for (AID runnerUp : runnerUps) {
                        gui.log("\tRunner-up is [" + runnerUp.getLocalName() + "]");
                    }
                }
            }, "FINISHING_AUCTION");

            registerTransition("SEARCHING_CURATORS", "NO_CURATORS", 0);
            registerTransition("SEARCHING_CURATORS", "INFORMING_CURATORS", 1);
            registerDefaultTransition("INFORMING_CURATORS", "CFP");
            registerDefaultTransition("CFP", "COLLECTING_PROPOSALS");
            registerTransition("COLLECTING_PROPOSALS", "PLANNING_NEXT_ROUND", 0);
            registerTransition("COLLECTING_PROPOSALS", "FINISHING_AUCTION", 1);
            registerTransition("PLANNING_NEXT_ROUND", "CFP", 0);
            registerTransition("PLANNING_NEXT_ROUND", "TERMINATING_AUCTION", 1);
            registerDefaultTransition("FINISHING_AUCTION", "TERMINATING_AUCTION");
        }
    }
}
