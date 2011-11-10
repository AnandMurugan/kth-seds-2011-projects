
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import java.util.List;

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
    private List<Artifact> artifacts;

    @Override
    protected void setup() {
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    private class CuratorBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
