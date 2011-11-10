
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Igor
 */
public class TourGuideAgent extends Agent {
    private String museum;
    private List<AID> curators;

    @Override
    protected void setup() {
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    private class TourGuideBehavior extends ParallelBehaviour {
    }
}
