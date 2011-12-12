/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import items.TourItem;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 *
 * @author Igor
 */
public class TourGuideAgent extends Agent {
    TourItem[][][] tourItems;

    @Override
    protected void setup() {
        initializeTourItems();

        //Register service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("tourguide");
        sd.setName("tourguide");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
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
