/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import items.TourItem;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class TourGuideAgent extends Agent {
    TourItem[][][] tourItems;
    private String targetMuseum;
    private Map<AID, ProfilerInfo> profilerNextTourMap = new HashMap<AID, ProfilerInfo>();
    private final int MAXIMUM_TOURS = 3;
    private int[] prices = new int[MAXIMUM_TOURS];
    private final String ASTRONOMY_PREFERENCE = "astronomy";
    private final String GEOMETRY_PREFERENCE = "geometry";

    private class ProfilerInfo {
        int currentTour, prebuiltTourId;
        String preferences;
        String[] profilerInfo;

        ProfilerInfo(String preferences) {
            this.preferences = preferences;
            currentTour = 1;
            // Init info
            profilerInfo = new String[3];
            profilerInfo[0] = "";  // P1
            profilerInfo[1] = "";  // P2
            profilerInfo[2] = "";  // P3
        }

        int getCurrentTour() {
            return currentTour;
        }

        void setCurrentTour(int currentTour) {
            this.currentTour = currentTour;
        }

        void setPrebuiltTourId(int prebuiltId) {
            prebuiltTourId = prebuiltId;
        }

        int getPrebuildTourId() {
            return this.prebuiltTourId;
        }

        void updateInfo(String[] newInfo) {
            for (String info : newInfo) {
                if (info.equals("p1")) {
                    profilerInfo[0] = "p1";
                } else if (info.equals("p2")) {
                    profilerInfo[1] = "p2";
                } else if (info.equals("p3")) {
                    profilerInfo[2] = "p3";
                }
            }
        }
    }

    @Override
    protected void setup() {
        //Setting the target museum name
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            targetMuseum = (String) args[0];
        } else {
            doDelete();
        }

        //Register service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("tourguide");
        sd.setName(targetMuseum + "tourguide");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }

        initializeTourItems();
        // Set prices
        prices[0] = 0;
        prices[1] = 15;
        prices[2] = 30;

        addBehaviour(new TourGuideBehaviour(this));
    }

    class TourGuideBehaviour extends CyclicBehaviour {
        private MessageTemplate msgTemplate;

        public TourGuideBehaviour(Agent a) {
            super(a);
        }

        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                // Case it is a request
                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    AID profilerId = msg.getSender();
                    System.out.println(myAgent.getAID().getName() + " received tour-request from " + profilerId.toString());
                    String preferences = msg.getContent();

                    ProfilerInfo profilerInfo = null;

                    if (profilerNextTourMap.containsKey(profilerId)) {
                        profilerInfo = profilerNextTourMap.get(profilerId);

                        if (profilerInfo.getCurrentTour() < MAXIMUM_TOURS) {
                            profilerInfo.setCurrentTour(profilerInfo.getCurrentTour() + 1);
                            System.out.println(myAgent.getAID().getName() + " setting next tour for: [" + profilerId.toString() + "]");
                        } else {
                            // Handle more than maximum tours;
                            System.out.println(myAgent.getAID().getName() + " MAXIMUM_TOURS limit over-passed: [" + profilerId.toString() + "]");

                        }
                    } else {
                        int builtinTourId = 0; // tour according to agent preferences

                        if (preferences.equals(ASTRONOMY_PREFERENCE)) {
                            builtinTourId = 0;
                        } else if (preferences.equals(GEOMETRY_PREFERENCE)) {
                            builtinTourId = 1;
                        }

                        profilerInfo = new ProfilerInfo(preferences);
                        profilerInfo.setPrebuiltTourId(builtinTourId);
                        profilerNextTourMap.put(profilerId, profilerInfo); // create  new profiler info.
                    }

                    // Send the new tour info
                    if (profilerInfo.getCurrentTour() < MAXIMUM_TOURS) {
                        ACLMessage reply = msg.createReply();
                        reply.setContent("T" + profilerInfo.getCurrentTour() + ";" + prices[profilerInfo.getCurrentTour() - 1]);
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                        System.out.println(myAgent.getAID().getName() + " sending tour#" + profilerInfo.getCurrentTour() + " description for: [" + profilerId.toString() + "]");
                    }
                } else if (msg.getPerformative() == ACLMessage.INFORM) {  // case tour agent accepts tour
                    // get the profile payment
                    AID profilerAID = msg.getSender();
                    System.out.println(myAgent.getAID().getName() + " received profile data from: [" + profilerAID.toString() + "]");
                    String profilerData = msg.getContent();
                    System.out.println(myAgent.getAID().getName() + " data from: [" + profilerAID.toString() + "]");
                    System.out.println(myAgent.getAID().getName() + " data: [" + profilerData + "]");
                    ProfilerInfo profilerInfo = profilerNextTourMap.get(profilerAID);

                    if (!profilerData.isEmpty()) {
                        String[] newInfo = profilerData.split(";");
                        profilerInfo.updateInfo(newInfo);
                        try {
                            // send the tour value
                            if (profilerInfo != null) {
                                ACLMessage reply = msg.createReply();
                                reply.setPerformative(ACLMessage.INFORM);
                                reply.setContentObject(tourItems[profilerInfo.getPrebuildTourId()][profilerInfo.getCurrentTour() - 1]);
                                myAgent.send(reply);
                                System.out.println(myAgent.getAID().getName() + "sending tour #" + profilerInfo.getCurrentTour() + " to : [" + profilerAID.toString() + "]");
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(TourGuideAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        // Profiler doesnt have more info to send, next tour should be set to unreachable tours
                        if (profilerInfo != null) {
                            profilerInfo.setCurrentTour(MAXIMUM_TOURS);
                        }
                    }
                }
            } else {
                block();
            }
        }
    }

    private void initializeTourItems() {
        tourItems = new TourItem[2][3][15];

        // Profile 1
        // -------------
        // Aleatory Tour
        tourItems[0][0][15] = new TourItem("urn:imss:instrument:401076", "Geometrical square@en");
        tourItems[0][0][1] = new TourItem("urn:imss:instrument:401001", "Arab celestial globe@en");
        tourItems[0][0][2] = new TourItem("urn:imss:instrument:405036", "Terrestrial telescope@en");
        tourItems[0][0][3] = new TourItem("urn:imss:instrument:414002", "Azimuth compass@en");
        tourItems[0][0][4] = new TourItem("urn:imss:instrument:404018", "Apparatus for Galilean experiments@en");
        tourItems[0][0][5] = new TourItem("urn:imss:instrument:402079", "Folding square@en");
        tourItems[0][0][6] = new TourItem("urn:imss:instrument:414058", "Aurora tube@en");
        tourItems[0][0][7] = new TourItem("urn:imss:instrument:414022", "Portable plate electrical machine, English type@en");
        tourItems[0][0][8] = new TourItem("urn:imss:instrument:414035", "Set of mercury friction tubes@en");
        tourItems[0][0][9] = new TourItem("urn:imss:instrument:401037", "Sundial@en");
        tourItems[0][0][10] = new TourItem("urn:imss:instrument:401110", "Gunner's sight and level@en");
        tourItems[0][0][11] = new TourItem("urn:imss:instrument:405030", "Terrestrial telescope@en");
        tourItems[0][0][12] = new TourItem("urn:imss:instrument:403017", "Case of mathematical instruments@en");
        tourItems[0][0][13] = new TourItem("urn:imss:instrument:402015", "Horizontal dial@en");
        tourItems[0][0][14] = new TourItem("urn:imss:instrument:404016", "Galileo and Viviani@en");


        // Interesting Tour
        tourItems[0][1][0] = new TourItem("urn:imss:instrument:416015", "Multi-lever apparatus@en");
        tourItems[0][1][1] = new TourItem("urn:imss:instrument:401056", "Diptych dial@en");
        tourItems[0][1][2] = new TourItem("urn:imss:instrument:403038", "Astronomical compendium@en");
        tourItems[0][1][3] = new TourItem("urn:imss:instrument:401022", "Nocturnal@en");
        tourItems[0][1][4] = new TourItem("urn:imss:instrument:403051", "Armillary sphere@en");
        tourItems[0][1][5] = new TourItem("urn:imss:instrument:401030", "Vertical dial@en");
        tourItems[0][1][6] = new TourItem("urn:imss:instrument:401024", "Nocturnal and sundial@en");
        tourItems[0][1][7] = new TourItem("urn:imss:instrument:401039", "Sundial@en");
        tourItems[0][1][8] = new TourItem("urn:imss:instrument:402042", "Mechanical almanac@en");
        tourItems[0][1][9] = new TourItem("urn:imss:instrument:402036", "Surveying instrument@en");
        tourItems[0][1][10] = new TourItem("urn:imss:instrument:414116", "Nobili's Wollaston battery element@en");
        tourItems[0][1][11] = new TourItem("urn:imss:instrument:417023", "Apparatus showing the composition of motion@en");
        tourItems[0][1][12] = new TourItem("urn:imss:instrument:414137", "Dial telegraph@en");
        tourItems[0][1][13] = new TourItem("urn:imss:instrument:414022", "Portable plate electrical machine, English type@en");
        tourItems[0][1][14] = new TourItem("urn:imss:instrument:402011", "Reel@en");

        // Very Interesting Tour
        tourItems[0][2][0] = new TourItem("urn:imss:instrument:403052", "Quadrant@en");
        tourItems[0][2][1] = new TourItem("urn:imss:instrument:401051", "Sundial@en");
        tourItems[0][2][2] = new TourItem("urn:imss:instrument:402039", "Diptych dial@en");
        tourItems[0][2][3] = new TourItem("urn:imss:instrument:402040", "Diptych dial@en");
        tourItems[0][2][4] = new TourItem("urn:imss:instrument:403026", "Correction dial@en");
        tourItems[0][2][5] = new TourItem("urn:imss:instrument:404010", "Middle finger of Galileo's right hand@en");
        tourItems[0][2][6] = new TourItem("urn:imss:instrument:401045", "Sundial@en");
        tourItems[0][2][7] = new TourItem("urn:imss:instrument:402073", "Equinoctial dial@en");
        tourItems[0][2][8] = new TourItem("urn:imss:instrument:402032", "Astrolabe@en");
        tourItems[0][2][9] = new TourItem("urn:imss:instrument:401009", "Astrolabe@en");
        tourItems[0][2][10] = new TourItem("urn:imss:instrument:401046", "Astrological disk@en");
        tourItems[0][2][11] = new TourItem("urn:imss:instrument:402033", "Astrolabe@en");
        tourItems[0][2][12] = new TourItem("urn:imss:instrument:401008", "Astrolabe@en");
        tourItems[0][2][13] = new TourItem("urn:imss:instrument:401032", "Polyhedral dial@en");
        tourItems[0][2][14] = new TourItem("urn:imss:instrument:401020", "Nocturnal@en");

        // Profile 2 
        // ------------
        // Aleatory Tour
        tourItems[1][0][0] = new TourItem("urn:imss:instrument:402068", "Astrolabe@en");
        tourItems[1][0][1] = new TourItem("urn:imss:instrument:401093", "Proportional compasses@en");
        tourItems[1][0][2] = new TourItem("urn:imss:instrument:414069", "Volta hydrogen lamp@en");
        tourItems[1][0][3] = new TourItem("urn:imss:instrument:414143", "Magnetic declination compass@en");
        tourItems[1][0][4] = new TourItem("urn:imss:instrument:402090", "Portrait of Nikolaus Kratzer@en");
        tourItems[1][0][5] = new TourItem("urn:imss:instrument:403034", "Triangulation instrument@en");
        tourItems[1][0][6] = new TourItem("urn:imss:instrument:401089", "Gunner's quadrant@en");
        tourItems[1][0][7] = new TourItem("urn:imss:instrument:401043", "Sundial@en");
        tourItems[1][0][8] = new TourItem("urn:imss:instrument:414042", "Straw electrometer, Volta type@en");
        tourItems[1][0][9] = new TourItem("urn:imss:instrument:402057", "Astrolabe@en");
        tourItems[1][0][10] = new TourItem("urn:imss:instrument:402027", "Quadrant@en");
        tourItems[1][0][11] = new TourItem("urn:imss:instrument:414083", "Nobili's constant-current thermopile@en");
        tourItems[1][0][12] = new TourItem("urn:imss:instrument:404002", "Galileo Galilei, \"Sidereus Nuncius\"@en");
        tourItems[1][0][13] = new TourItem("urn:imss:instrument:414078", "Nobili's thermoelectric galvanometer@en");
        tourItems[1][0][14] = new TourItem("urn:imss:instrument:414118", "Demonstration model of Oersted's experiment@en");

        // Interesting Tour
        tourItems[1][1][0] = new TourItem("urn:imss:instrument:401133", "Pantograph@en");
        tourItems[1][1][1] = new TourItem("urn:imss:instrument:414118", "Demonstration model of Oersted's experiment@en");
        tourItems[1][1][2] = new TourItem("urn:imss:instrument:417002", "Apparatus to demonstrate the isochronism of falls along a spiral@en");
        tourItems[1][1][3] = new TourItem("urn:imss:instrument:417025", "Plane with variable inclination@en");
        tourItems[1][1][4] = new TourItem("urn:imss:instrument:404018", "Apparatus for Galilean experiments@en");
        tourItems[1][1][5] = new TourItem("urn:imss:instrument:417018", "Model winch@en");
        tourItems[1][1][6] = new TourItem("urn:imss:instrument:416002", "Apparatus for showing the effects of the centrifugal force@en");
        tourItems[1][1][7] = new TourItem("urn:imss:instrument:402019", "Tripod legs@en");
        tourItems[1][1][8] = new TourItem("urn:imss:instrument:417012", "Cogwheels@en");
        tourItems[1][1][9] = new TourItem("urn:imss:instrument:402011", "Reel@en");
        tourItems[1][1][10] = new TourItem("urn:imss:instrument:402026", "Quadrant@en");
        tourItems[1][1][11] = new TourItem("urn:imss:instrument:402020", "Surveying rod@en");
        tourItems[1][1][12] = new TourItem("urn:imss:instrument:401088", "Measuring rule@en");
        tourItems[1][1][13] = new TourItem("urn:imss:instrument:402018", "Box for mining instruments@en");
        tourItems[1][1][14] = new TourItem("urn:imss:instrument:401083", "Ruler@en");

        // Very Interesting tour
        tourItems[1][2][0] = new TourItem("urn:imss:instrument:402077", "Square@en");
        tourItems[1][2][1] = new TourItem("urn:imss:instrument:401119", "Ramrods@en");
        tourItems[1][2][2] = new TourItem("urn:imss:instrument:401068", "Archimetro\"@en");
        tourItems[1][2][3] = new TourItem("urn:imss:instrument:401118", "Gunner's quadrant@en");
        tourItems[1][2][4] = new TourItem("urn:imss:instrument:401110", "Gunner's sight and level@en");
        tourItems[1][2][5] = new TourItem("urn:imss:instrument:402025", "Gunner's level@en");
        tourItems[1][2][6] = new TourItem("urn:imss:instrument:402010", "Box of mining instruments@en");
        tourItems[1][2][7] = new TourItem("urn:imss:instrument:402030", "Archimetro\"@en");
        tourItems[1][2][8] = new TourItem("urn:imss:instrument:403037", "Clinometer@en");
        tourItems[1][2][9] = new TourItem("urn:imss:instrument:401129", "Case for military instruments@en");
        tourItems[1][2][10] = new TourItem("urn:imss:instrument:402005", "Gimbaled compass@en");
        tourItems[1][2][11] = new TourItem("urn:imss:instrument:403018", "Level@en");
        tourItems[1][2][12] = new TourItem("urn:imss:instrument:403005", "Gunner's calliper@en");
        tourItems[1][2][13] = new TourItem("urn:imss:instrument:401082", "Square@en");
        tourItems[1][2][14] = new TourItem("urn:imss:instrument:403036", "Surveying instrument@en");
    }
}
