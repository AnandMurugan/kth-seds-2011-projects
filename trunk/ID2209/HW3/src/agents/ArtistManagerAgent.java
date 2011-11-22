/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import gui.ArtistManagerFrame;
import gui.ArtistManagerResponsiveGUI;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Iterator;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.Serializable;
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
 * @author Igor
 */
public class ArtistManagerAgent extends GuiAgent {
    private static final String MAIN_CONTAINER_NAME = "Main-Container";
    private transient ArtistManagerResponsiveGUI gui;
    public static final int QUIT = 0;
    public static final int START_AUCTION = 1;
    private Location home;
    private AID cloneParent;
    private Artifact artifact;
    private float initialPrice;
    private float reservePrice;
    private AuctionLocalResult auctionResult;

    @Override
    protected void setup() {
        // Register language and ontology
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());

        //registerService();

        createGui();

        System.out.println("Artist Manager [" + getLocalName() + "] is ready...");
    }

    private void createGui() {
        /* Create and display the form */
        final ArtistManagerAgent agent = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new ArtistManagerFrame(agent);
                gui.open();
            }
        });
    }

    private void registerService() {
        //Register the service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("artist-manager");
        sd.setName("artist-manager_" + getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            doDelete();
        }
    }

    @Override
    protected void takeDown() {
//        try {
        System.out.println("Artist Manager [" + getAID().getLocalName() + "] is terminating...");
//            DFService.deregister(this);
//        } catch (FIPAException ex) {
//            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void sellArtifact() {
        gui.log("Starting local auction...");
        addBehaviour(new DutchAuctionInitiatorBehaviour(this, artifact, initialPrice, reservePrice));
    }

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        switch (ge.getType()) {
            case QUIT:
                gui.close();
                doDelete();
                break;
            case START_AUCTION:
                String[] args = (String[]) ge.getParameter(0);
                startMultiContainerAuction(args);
                break;
        }
    }

    @Override
    protected void afterClone() {
        //In target container already
        gui = new ArtistManagerResponsiveGUI() {
            String name = getLocalName();

            @Override
            public void open() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void close() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void log(String msg) {
                System.out.println("########" + name + "######## " + msg);
            }
        };

        System.out.println("Artist Manager [" + getLocalName() + "] is ready (after cloning to " + here().getName() + ")...");

        sellArtifact();
    }

    @Override
    protected void afterMove() {
        //In home container
        addBehaviour(new AfterReturningBehaviour(this));
    }

    private void startMultiContainerAuction(String[] args) {
        try {
            gui.log("Starting a mobile auction...");

            //Setting auction params for clones (using fields)
            String name = args[0];
            String creator = args[1];
            String description = args[2];
            String style = args[3];
            artifact = new Artifact(name, creator, description, style);
            initialPrice = Float.parseFloat(args[4]);
            reservePrice = Float.parseFloat(args[5]);
            gui.log("\tLot:");
            gui.log("\t\tName: " + name);
            gui.log("\t\tCreator: " + creator);
            gui.log("\t\tDescription: " + description);
            gui.log("\t\tStyle: " + style);
            gui.log("\tAuction parameters:");
            gui.log("\t\tInitial price: " + initialPrice);
            gui.log("\t\tReserve price: " + reservePrice);

            home = here();
            cloneParent = getAID();

            //Discovering locations      
            gui.log("Discovering locations (containers)...");
            Map<String, Location> locations = new HashMap<String, Location>();

            sendRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));

            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(getAMS()),
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            ACLMessage resp = blockingReceive(mt);
            ContentElement ce = getContentManager().extractContent(resp);
            Result result = (Result) ce;
            Iterator it = result.getItems().iterator();
            while (it.hasNext()) {
                Location loc = (Location) it.next();
                locations.put(loc.getName(), loc);
            }

            //Removing home location and main-container location (has only df, ams and rma)
            locations.remove(home.getName());
            locations.remove(MAIN_CONTAINER_NAME);

            //Cloning
            gui.log("Cloning:");
            MessageTemplate resultsMt = MessageTemplate.not(MessageTemplate.MatchAll());
            for (Entry<String, Location> entry : locations.entrySet()) {
                final String cloneName = "clone-" + getLocalName() + "_" + entry.getKey();
                final Location cloneLocation = entry.getValue();
                addBehaviour(new OneShotBehaviour(this) {
                    @Override
                    public void action() {
                        if (!getLocalName().contains("clone")) {
                            doClone(cloneLocation, cloneName);
                        }
                    }
                });
                //doClone(cloneLocation, cloneName);
                gui.log("\tCreated a clone [" + cloneName + "] in " + cloneLocation.getName());

                resultsMt = MessageTemplate.or(resultsMt, MessageTemplate.MatchInReplyTo(cloneName));
            }
            resultsMt = MessageTemplate.and(resultsMt, MessageTemplate.MatchPerformative(ACLMessage.INFORM));

            //Preparing for collecting results
            gui.log("Collecting results:");
            int locationsCount = locations.size();
            addBehaviour(new CollectingResultsBehaviour(this, locationsCount, resultsMt));
        } catch (CodecException ex) {
            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UngroundedException ex) {
            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OntologyException ex) {
            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendRequest(Action action) {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(new SLCodec().getName());
        request.setOntology(MobilityOntology.getInstance().getName());
        try {
            getContentManager().fillContent(request, action);
            request.addReceiver(action.getActor());
            send(request);
        } catch (Exception ex) {
            Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        private int responsesCount;

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
                    sd.setType(here().getName() + "_curator");
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
            registerState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    gui.log("Auction is terminating due to absence of any curator...\n");
                }
            }, "NO_CURATORS");
            //
            registerState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    //Respond to all curators
                    ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
                    informMsg.setConversationId(conversationId);
                    informMsg.setContent("terminating");
                    for (AID aid : curators) {
//                        if ((winner == null)
//                                || (!aid.equals(winner))) {
                        informMsg.addReceiver(aid);
//                        }
                    }
                    myAgent.send(informMsg);

                    //Log
                    gui.log("Auction is terminating...");
                }
            }, "TERMINATING_AUCTION");
            //
            registerLastState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    if (winner != null) {
                        auctionResult = new AuctionLocalResult(winner, here(), currentPrice, conversationId);

                    } else {
                        auctionResult = null;
                    }
                    myAgent.doMove(home);
                }
            }, "RETURNING");

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
                    responsesCount = 0;
                }
            }, "CFP");
            //
            registerState(new Behaviour(agent) {
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
                    return Float.compare(currentPrice + 0.0001f, reservePrice) <= 0 ? 1 : 0;
                }
            }, "PLANNING_NEXT_ROUND");
            //
            registerState(new OneShotBehaviour(agent) {
                @Override
                public void action() {
                    //Respond to the winner
//                    ACLMessage acceptMsg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
//                    acceptMsg.setConversationId(conversationId);
//                    acceptMsg.addReceiver(winner);
//                    myAgent.send(acceptMsg);

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
            registerDefaultTransition("TERMINATING_AUCTION", "RETURNING");
            registerDefaultTransition("NO_CURATORS", "RETURNING");
        }
    }

    private class CollectingResultsBehaviour extends Behaviour {
        private final int locationsCount;
        private int responsesCount;
        private final MessageTemplate mt;
        private AID winner;
        private List<AID> runnerUps = new ArrayList<AID>();
        private List<String> runnerUpConversationIds = new ArrayList<String>();
        private float winPrice = Float.MIN_VALUE;
        private Location winLocation;
        private String winConversationId;

        public CollectingResultsBehaviour(Agent a, int locationsCount, MessageTemplate mt) {
            super(a);
            this.locationsCount = locationsCount;
            this.mt = mt;
        }

        @Override
        public void action() {
            ACLMessage response = myAgent.receive(mt);
            if (response == null) {
                block();
                return;
            }

            ++responsesCount;
            try {
                AuctionLocalResult result = (AuctionLocalResult) response.getContentObject();
                gui.log("\tResponse #" + responsesCount + ":");
                if (result != null) {
                    AID w = result.winner;
                    Location location = result.location;
                    float price = result.price;
                    String cId = result.conversationId;

                    gui.log("\t\tWinner: " + w.getLocalName());
                    gui.log("\t\tLocation: " + location.getName());
                    gui.log("\t\tPrice: " + price);
                    if (price > winPrice) {
                        if (winner != null) {
                            runnerUps.add(winner);
                            runnerUpConversationIds.add(winConversationId);
                        }

                        winPrice = price;
                        winner = w;
                        winLocation = location;
                        winConversationId = cId;
                    } else {
                        runnerUps.add(w);
                        runnerUpConversationIds.add(cId);
                    }
                } else {
                    gui.log("\t\tNo bids");
                }
            } catch (UnreadableException ex) {
                Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (responsesCount == locationsCount) {
                gui.log("Auction is finished:");
                if (winner != null) {
                    //Respond to the winner
                    ACLMessage acceptMsg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    acceptMsg.setConversationId(winConversationId);
                    acceptMsg.addReceiver(winner);
                    myAgent.send(acceptMsg);

                    //Respond to the runner-ups
                    for (int i = 0; i < runnerUps.size(); i++) {
                        ACLMessage rejectMsg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                        rejectMsg.setConversationId(runnerUpConversationIds.get(i));
                        rejectMsg.addReceiver(runnerUps.get(i));
                        myAgent.send(rejectMsg);
                    }

//                    //Respond to all local winners (winner and runner-ups)
//                    ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
//                    informMsg.setConversationId(winConversationId);
//                    informMsg.setContent("terminating");
//                    informMsg.addReceiver(winner);
//                    myAgent.send(informMsg);
//                    for (int i = 0; i < runnerUps.size(); i++) {
//                        informMsg = new ACLMessage(ACLMessage.INFORM);
//                        informMsg.setConversationId(runnerUpConversationIds.get(i));
//                        informMsg.setContent("terminating");
//                        informMsg.addReceiver(runnerUps.get(i));
//                        myAgent.send(informMsg);
//                    }

                    gui.log("\tWinner: " + winner.getLocalName() + " from " + winLocation.getName());
                    gui.log("\tWinning price: " + winPrice);
                } else {
                    gui.log("\tNo bids");
                }
            }
        }

        @Override
        public boolean done() {
            return responsesCount == locationsCount;
        }
    }

    private class AfterReturningBehaviour extends SequentialBehaviour {
        public AfterReturningBehaviour(Agent a) {
            super(a);

            addSubBehaviour(new OneShotBehaviour(a) {
                @Override
                public void action() {
                    try {
                        ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
                        informMsg.setInReplyTo(getLocalName());
                        informMsg.setContentObject(auctionResult);
                        informMsg.addReceiver(cloneParent);
                        myAgent.send(informMsg);
                    } catch (IOException ex) {
                        Logger.getLogger(ArtistManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            addSubBehaviour(new OneShotBehaviour(a) {
                @Override
                public void action() {
                    doDelete();
                }
            });
        }
    }

    private class AuctionLocalResult implements Serializable {
        public AID winner;
        public Location location;
        public float price;
        public String conversationId;

        public AuctionLocalResult(AID winner, Location location, float price, String conversationId) {
            this.winner = winner;
            this.location = location;
            this.price = price;
            this.conversationId = conversationId;
        }
    }
}
