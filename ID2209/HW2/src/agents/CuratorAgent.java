package agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private String preferedStyle;
    private List<Artifact> artifacts = new ArrayList<Artifact>();
    private static long counter = 1;

    @Override
    protected void setup() {
        //Setting the museum name
        Object[] args = getArguments();
        if (args != null && args.length > 1) {
            museum = (String) args[0];
            preferedStyle = (String) args[1];
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

        addBehaviour(new DutchAuctionParticipantBehaviour());
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    class DutchAuctionParticipantBehaviour extends FSMBehaviour {
        private MessageTemplate msgTemplate;
        private Random rand;
        private float price;
        private float maxPrice;
        private String style;
        // Define states
        private static final String WAIT_AUCTION_STATE = "wait-auction";
        private static final String WAIT_CFP_OR_TERMINATE_STATE = "wait-cfp-or-terminate";
        private static final String WAIT_PROPOSAL_REPLY_STATE = "wait-proposal-reply";
        private static final String END_AUCTION_STATE = "end-auction";
        private static final String END_AUCTION_NO_BIDS_STATE = "end-auction-no-bids";
        // Define transitions
        private final int NEW_AUCTION_TRANSITION = 1;
        private final int NO_BIDS_TRANSITION = 2;
        private final int SENT_PROPOSAL_TRANSITION = 3;
        private final int NOT_GOOD_PRICE_TRANSITION = 4;
        private final int RECEIVED_PROPOSAL_REPLY_TRANSITION = 5;
        private final int DEFAULT_ERROR_STATE = 5;

        @Override
        public void onStart() {
            msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            rand = new Random(System.nanoTime());

            // Register states
            registerFirstState(new WaitAuctionBehaviour(myAgent), WAIT_AUCTION_STATE);
            registerState(new WaitCfpBehaviour(myAgent), WAIT_CFP_OR_TERMINATE_STATE);
            registerState(new WaitReplyBehaviour(myAgent), WAIT_PROPOSAL_REPLY_STATE);
            registerLastState(new WaitEndAuctionBehaviour(myAgent), END_AUCTION_STATE);
            registerLastState(new NoBidsEndAuctionBehaviour(myAgent), END_AUCTION_NO_BIDS_STATE);

            // Register Transitions
            registerTransition(WAIT_AUCTION_STATE, WAIT_CFP_OR_TERMINATE_STATE, NEW_AUCTION_TRANSITION);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, WAIT_PROPOSAL_REPLY_STATE, SENT_PROPOSAL_TRANSITION);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, WAIT_CFP_OR_TERMINATE_STATE, NOT_GOOD_PRICE_TRANSITION);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, END_AUCTION_STATE, DEFAULT_ERROR_STATE);
            registerTransition(WAIT_CFP_OR_TERMINATE_STATE, END_AUCTION_NO_BIDS_STATE, NO_BIDS_TRANSITION);
            registerTransition(WAIT_PROPOSAL_REPLY_STATE, END_AUCTION_STATE, RECEIVED_PROPOSAL_REPLY_TRANSITION);
        }

        private class WaitAuctionBehaviour extends Behaviour {
            boolean notWaitAuction = false;

            WaitAuctionBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage initAuctionMsg = myAgent.receive(msgTemplate);
                if (initAuctionMsg != null) {
                    notWaitAuction = true;
                    style = initAuctionMsg.getContent();
                    System.out.println(getAID().getName() + "Started new auction...");
                    msgTemplate = MessageTemplate.and(
                            MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchPerformative(ACLMessage.INFORM)),
                            MessageTemplate.MatchConversationId(initAuctionMsg.getConversationId()));
                } else {
                    block();
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

        private class WaitCfpBehaviour extends Behaviour {
            boolean notWaitCfp = false;
            int transition = DEFAULT_ERROR_STATE;

            WaitCfpBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage cfpMsg = myAgent.receive(msgTemplate);
                if (cfpMsg != null) {
                    notWaitCfp = true;
                    System.out.println(getAID().getName() + " received a message from art agent...");
                    if (cfpMsg.getPerformative() == ACLMessage.CFP) {
                        price = Float.parseFloat(cfpMsg.getContent());
                        if (maxPrice <= 0) {
                            float low = 0.5f;
                            maxPrice = price * (low + rand.nextFloat() * (1.0f - low));
                        }
                        System.out.println(getAID().getName() + " processing CFP message.");
                        boolean satisfactoryPrice = evaluate(style, price);

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
                } else {
                    block();
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

            private boolean evaluate(String style, float price) {
                if (!preferedStyle.equals(style)) {
                    return false;
                } else {
                    if (Float.compare(maxPrice, price) > 0) {
                        double coin = rand.nextDouble();
                        return coin > 0.5;
                    } else {
                        return false;
                    }
                }
            }
        }

        private class WaitReplyBehaviour extends Behaviour {
            boolean gotReply = false;
            int transition = DEFAULT_ERROR_STATE;

            WaitReplyBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage replyMsg = myAgent.receive(msgTemplate);
                if (replyMsg != null) {
                    gotReply = true;
                    transition = RECEIVED_PROPOSAL_REPLY_TRANSITION;
                    if (replyMsg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        try {
                            artifacts.add((Artifact) replyMsg.getContentObject());
                        } catch (UnreadableException ex) {
                            Logger.getLogger(CuratorAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println(getAID().getName() + " Bid accepted. ");
                    } else if (replyMsg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                        System.out.println(getAID().getName() + " Bid rejected, someone else bought the item. ");
                    }

                    msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(replyMsg.getConversationId()));
                } else {
                    block();
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

        private class WaitEndAuctionBehaviour extends Behaviour {
            boolean gotReply = false;

            WaitEndAuctionBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage replyMsg = myAgent.receive(msgTemplate);
                if (replyMsg != null) {
                    gotReply = true;
                    System.out.println(getAID().getName() + " Auction finished. ");
                    myAgent.addBehaviour(new DutchAuctionParticipantBehaviour());
                } else {
                    block();
                }
            }

            @Override
            public boolean done() {
                return gotReply;
            }
        }

        private class NoBidsEndAuctionBehaviour extends Behaviour {
            NoBidsEndAuctionBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                System.out.println(getAID().getName() + " Auction finished. ");
                myAgent.addBehaviour(new DutchAuctionParticipantBehaviour());
            }

            @Override
            public boolean done() {
                return true;
            }
        }
    }
}
