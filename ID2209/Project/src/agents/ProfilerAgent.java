/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import com.myprofile.profile.ProfileType;
import daiia.ProfileManager;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Igor
 */
public class ProfilerAgent extends Agent {
    private ProfileType profile;
    final String DEFAULT_PROFILE_PATH = "Profile.xml";

    @Override
    protected void setup() {
        ProfileManager profiler = new ProfileManager();
        String profilePath = "";
        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            profilePath = (String) args[0];
        }

        if (!profilePath.isEmpty()) {
            this.profile = profiler.loadProfile(profilePath);
        } else {
            this.profile = profiler.loadProfile(DEFAULT_PROFILE_PATH);
        }
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    class MuseumVisitorBehaviour extends FSMBehaviour {
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
            
            
        }

        private class RequestTourBehavior extends Behaviour {
            RequestTourBehavior(Agent aAgent) {
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
            
            NegotiateTourBehaviour(Agent aAgent) {
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
            
            VisitTourBehaviour(Agent aAgent) {
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
        
         private class EndMuseumVisitorBehaviour extends Behaviour {
            boolean gotReply = false;
            int transition = DEFAULT_ERROR_STATE;

            EndMuseumVisitorBehaviour(Agent aAgent) {
                super(aAgent);
            }

            @Override
            public void action() {
                ACLMessage replyMsg = myAgent.receive(msgTemplate);
                if (replyMsg != null) {
                    gotReply = true;
                    System.out.println(getAID().getName() + " museum visitor finished. ");
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
