/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import com.myprofile.profile.ProfileType;
import daiia.ProfileManager;
import items.MuseumItem;
import items.TourItem;
import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class ProfilerAgent extends Agent {
    private ProfileType profile;
    private ProfileManager profileManager;
    final String DEFAULT_PROFILE_PATH = "Profile.xml";
    private String profilePath = "";
    private TourItem[] currentTour;
    private boolean museumVisited;
    private Location home = here();

    @Override
    protected void setup() {
        // Register language and ontology
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());

        //
        profileManager = new ProfileManager();

        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            profilePath = (String) args[0];
        }

        if (!profilePath.isEmpty()) {
            this.profile = profileManager.loadProfile(profilePath);
        } else {
            this.profile = profileManager.loadProfile(DEFAULT_PROFILE_PATH);
        }

        addBehaviour(new MuseumVisitorBehaviour(this));
    }

    public TourItem[] getCurrentTour() {
        return currentTour;
    }

    public void setCurrentTour(TourItem[] currentTour) {
        this.currentTour = currentTour;
    }

    public boolean isMuseumVisited() {
        return museumVisited;
    }

    public void setMuseumVisited(boolean museumVisited) {
        this.museumVisited = museumVisited;
    }

    @Override
    protected void afterMove() {
        if (!museumVisited) {
            //Find Inventory agent
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sdInventory = new ServiceDescription();
            sdInventory.setType("inventory");

            template.addServices(sdInventory);
            DFAgentDescription[] result = null;
            try {
                result = DFService.search(this, template);
                if (result.length == 0) {
                    this.doDelete();
                    return;
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
            final AID curatorAgent = result[0].getName();

            addBehaviour(new OneShotBehaviour(this) {
                private int repliesCount;

                @Override
                public void action() {
                    for (TourItem ti : ((ProfilerAgent) myAgent).getCurrentTour()) {
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(curatorAgent);
                        msg.setContent(ti.getId());
                    }

                    repliesCount = ((ProfilerAgent) myAgent).getCurrentTour().length;
                    for (int i = 0; i < repliesCount; i++) {
                        ACLMessage reply = myAgent.blockingReceive();
                        try {
                            MuseumItem mi = (MuseumItem) reply.getContentObject();
                            System.out.println(i + ": " + mi);

                            com.myprofile.profile.MuseumItem otherMi = new com.myprofile.profile.MuseumItem();
                            otherMi.setId(mi.getId());
                            otherMi.setName(mi.getTitle());
                            otherMi.setSubject(mi.getSubject()[0]);
                            otherMi.setObjectType(mi.getObjectType()[0]);
                            otherMi.setMaterial(mi.getMaterial()[0]);
                            otherMi.setRating(i % 5 + 1);

                            profile.getVisitedItems().getVisitedItem().add(otherMi);
                        } catch (UnreadableException ex) {
                            Logger.getLogger(ProfilerAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    ((ProfilerAgent) myAgent).setMuseumVisited(true);
                }
            });

            doMove(home);
        } else {
        }
    }

    private class MuseumVisitorBehaviour extends FSMBehaviour {
        private MessageTemplate msgTemplate;
        // define states
        private static final String REQUEST_TOUR_STATE = "request_tour";
        private static final String NEGOTIATE_TOUR_STATE = "wait_tour";
        private static final String VISIT_MUSEUM_STATE = "visit_museum";
        private static final String END_VISITOR_STATE = "end_visitor";
        // define transitions
        private final int REQUESTED_TOUR_TRANSTION = 1;
        private final int RECEIVED_TOUR_TRANSITION = 2;
        private final int COMPLETED_MUSEUM_VISIT_TRANSITION = 3;
        private final int END_TRANSITION = 4;
        private final int DEFAULT_ERROR_STATE = 5;

        public MuseumVisitorBehaviour(Agent a) {
            super(a);
        }

        @Override
        public void onStart() {
            msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

            // Register States
            registerFirstState(new RequestTourBehavior(myAgent), REQUEST_TOUR_STATE);
            registerState(new NegotiateTourBehaviour(myAgent), NEGOTIATE_TOUR_STATE);
            registerState(new VisitTourBehaviour(myAgent), VISIT_MUSEUM_STATE);
            registerLastState(new EndMuseumVisitorBehaviour(myAgent), END_VISITOR_STATE);

            // Register Transitions
            registerTransition(REQUEST_TOUR_STATE, NEGOTIATE_TOUR_STATE, REQUESTED_TOUR_TRANSTION);
            registerTransition(NEGOTIATE_TOUR_STATE, VISIT_MUSEUM_STATE, RECEIVED_TOUR_TRANSITION);
            registerTransition(NEGOTIATE_TOUR_STATE, END_VISITOR_STATE, END_TRANSITION);
            registerTransition(VISIT_MUSEUM_STATE, REQUEST_TOUR_STATE, COMPLETED_MUSEUM_VISIT_TRANSITION);
        }

        private class RequestTourBehavior extends Behaviour {
            public RequestTourBehavior(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean done() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        private class NegotiateTourBehaviour extends Behaviour {
            public NegotiateTourBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean done() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        private class VisitTourBehaviour extends Behaviour {
            private int repliesCount;

            public VisitTourBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public int onEnd() {
                return COMPLETED_MUSEUM_VISIT_TRANSITION;
            }

            @Override
            public void action() {
                Map<String, Location> locations = new HashMap<String, Location>();
                sendRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));
                MessageTemplate mt = MessageTemplate.and(
                        MessageTemplate.MatchSender(getAMS()),
                        MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                ACLMessage resp = blockingReceive(mt);
                ContentElement ce;
                try {
                    ce = getContentManager().extractContent(resp);
                    Result result = (Result) ce;
                    Iterator it = result.getItems().iterator();
                    while (it.hasNext()) {
                        Location loc = (Location) it.next();
                        locations.put(loc.getName(), loc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Location curatorLocation = locations.get("Container-1");

                myAgent.doMove(curatorLocation);
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
                    ex.printStackTrace();
                }
            }

            @Override
            public boolean done() {
                return ((ProfilerAgent) myAgent).isMuseumVisited();
            }
        }

        private class EndMuseumVisitorBehaviour extends OneShotBehaviour {
            boolean gotReply = false;
            int transition = DEFAULT_ERROR_STATE;

            public EndMuseumVisitorBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                if (profilePath.isEmpty()) {
                    profileManager.dumpProfile(profile, DEFAULT_PROFILE_PATH);
                } else {
                    profileManager.dumpProfile(profile, profilePath);
                }

                System.out.println(getAID().getName() + " museum visitor finished. ");
            }
        }
    }
}
