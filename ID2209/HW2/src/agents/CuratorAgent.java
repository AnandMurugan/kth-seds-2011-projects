package agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

        addBehaviour(new DutchAuctionParticipant());
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    

    class DutchAuctionParticipant extends FSMBehaviour {
        private MessageTemplate msgTemplate;
        // Define states
        private static final String WAIT_AUCTION_STATE = "wait-auction";
        private static final String WAIT_CFP_OR_TERMINATE_STATE = "wait-cfp-or-terminate";
        private static final String WAIT_PROPOSAL_REPLY_STATE = "wait-proposal-reply";
        private static final String END_AUCTION_STATE = "end-auction";
        // Define transitions
        private final int NEW_AUCTION_TRANSITION = 1;
        private final int NO_BIDS_TRANSITION = 2;
        private final int SENT_PROPOSAL_TRANSITION = 3;
        private final int NOT_GOOD_PRICE_TRANSITION = 4;
        private final int RECEIVED_PROPOSAL_REPLY_TRANSITION = 5;
        private final int DEFAULT_ERROR_STATE = 5;

        public void onStart() {
            msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            
            // Register states
            registerFirstState( new waitAuction(myAgent), WAIT_AUCTION_STATE);
            registerState(new waitCfp(myAgent), WAIT_CFP_OR_TERMINATE_STATE);
            registerState(new waitReplyBehaviour(myAgent), WAIT_PROPOSAL_REPLY_STATE);
            registerLastState(new waitEndAuctionBehaviour(myAgent), END_AUCTION_STATE);
            
            // Register Transitions
            registerTransition(WAIT_AUCTION_STATE, WAIT_CFP_OR_TERMINATE_STATE, NEW_AUCTION_TRANSITION);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, WAIT_PROPOSAL_REPLY_STATE, SENT_PROPOSAL_TRANSITION);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, WAIT_CFP_OR_TERMINATE_STATE, NOT_GOOD_PRICE_TRANSITION);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, END_AUCTION_STATE, DEFAULT_ERROR_STATE);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, END_AUCTION_STATE, NO_BIDS_TRANSITION);
            registerTransition(WAIT_PROPOSAL_REPLY_STATE, END_AUCTION_STATE, RECEIVED_PROPOSAL_REPLY_TRANSITION);
            
            
        }

        private class waitAuction extends Behaviour {
            boolean notWaitAuction = false;

            waitAuction(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage initAuctionMsg = myAgent.receive(msgTemplate);
                if (initAuctionMsg != null) {
                    notWaitAuction = true;
                    System.out.println(getAID().getName() + "Started new auction...");
                    msgTemplate = MessageTemplate.and(
                            MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchPerformative(ACLMessage.INFORM)),
                            MessageTemplate.MatchConversationId(initAuctionMsg.getConversationId()));
                }
            }

            @Override
            public boolean done() {
                return notWaitAuction;
            }

            @Override
            public int onEnd() {
                return NEW_AUCTION_TRANSITION;
            }
        }

        private class waitCfp extends Behaviour {
            boolean notWaitCfp = false;
            int transition = DEFAULT_ERROR_STATE;

            waitCfp(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage cfpMsg = myAgent.receive(msgTemplate);
                if (cfpMsg != null) {
                    notWaitCfp = true;
                    System.out.println(getAID().getName() + " received a message from art agent...");
                    if (cfpMsg.getPerformative() == ACLMessage.CFP) {
                        // TODO. get price and style
                        float price = Float.parseFloat(cfpMsg.getContent());
                        String style = "cubism";
                        System.out.println(getAID().getName() + " processing CFP message.");
                        boolean satisfactoryPrice = true; //evaluate(style, price);

                        if (satisfactoryPrice) {
                            // Send proposal
                            ACLMessage proposalMsg = new ACLMessage(ACLMessage.PROPOSE);
                            proposalMsg.addReceiver(cfpMsg.getSender());
                            proposalMsg.setConversationId(cfpMsg.getConversationId());
                            myAgent.send(proposalMsg);
                            
                            transition = SENT_PROPOSAL_TRANSITION;

                            msgTemplate = MessageTemplate.and(
                                    MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL), MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)),
                                    MessageTemplate.MatchConversationId(cfpMsg.getConversationId()));
                        } else {
                            System.out.println(getAID().getName() + " not convenient price.");
                            ACLMessage proposalMsg = new ACLMessage(ACLMessage.NOT_UNDERSTOOD);
                            proposalMsg.addReceiver(cfpMsg.getSender());
                            proposalMsg.setConversationId(cfpMsg.getConversationId());
                            myAgent.send(proposalMsg);
                            
                            transition = NOT_GOOD_PRICE_TRANSITION;

                            msgTemplate = MessageTemplate.and(
                                    MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchPerformative(ACLMessage.INFORM)),
                                    MessageTemplate.MatchConversationId(cfpMsg.getConversationId()));
                        }
                    } else if (cfpMsg.getPerformative() == ACLMessage.INFORM) {
                        transition = NO_BIDS_TRANSITION;
                        System.out.println(getAID().getName() + " no bids.");
                        
                        msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(cfpMsg.getConversationId()));
                    }
                }
            }

            @Override
            public boolean done() {
                return notWaitCfp;
            }

            @Override
            public int onEnd() {
                return transition;
            }
        }

        private class waitReplyBehaviour extends Behaviour {
            boolean gotReply = false;
            int transition = DEFAULT_ERROR_STATE;

            waitReplyBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage replyMsg = myAgent.receive(msgTemplate);
                if (replyMsg != null) {
                    gotReply = true;
                    transition = RECEIVED_PROPOSAL_REPLY_TRANSITION;
                    if (replyMsg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        System.out.println(getAID().getName() + " Bid accepted. ");
                    } else if (replyMsg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                        System.out.println(getAID().getName() + " Bid rejected, someone else bought the item. ");
                    }

                    msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(replyMsg.getConversationId()));
                }
            }

            @Override
            public boolean done() {
                return gotReply;
            }

            @Override
            public int onEnd() {
                return transition;
            }
        }

        private class waitEndAuctionBehaviour extends Behaviour {
            boolean gotReply = false;
            int transition = DEFAULT_ERROR_STATE;

            waitEndAuctionBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage replyMsg = myAgent.receive(msgTemplate);
                if (replyMsg != null) {
                    gotReply = true;
                    System.out.println(getAID().getName() + " Auction finished. ");
                }
            }

            @Override
            public boolean done() {
                return gotReply;
            }

            @Override
            public int onEnd() {
                return transition;
            }
        }
    }
}
