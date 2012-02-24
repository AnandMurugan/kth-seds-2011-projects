/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import se.sics.kompics.launch.Scenario;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author Igor
 */
public final class Assignment3bExecutor {
    private static final int NODES = 6;

    public static void main(String[] args) {
        Topology topology0 = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);
                node(3, "127.0.0.1", 10003);
                node(4, "127.0.0.1", 10004);

                defaultLinks(100, 0.0);
            }
        };
        Topology topologyEx1And2 = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);
                node(3, "127.0.0.1", 10003);

                defaultLinks(1000, 0.0);
            }
        };
        Topology topologyEx3 = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);
                node(3, "127.0.0.1", 10003);

//                link(1, 2, 1000, 0).bidirectional();
//                link(1, 3, 2000, 0).bidirectional();
//                link(2, 3, 1750, 0).bidirectional();
                //defaultLinks(1000, 0.0);
                link(1, 2, 1000, 0).bidirectional();
                link(1, 3, 2000, 0).bidirectional();
                link(2, 3, 0, 0).bidirectional();
            }
        };


        Scenario scenario0 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };
        Scenario scenarioEx1 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "S30000");
                command(2, "S500:W4:S25000");
                command(3, "S10000:R");
            }
        };
        Scenario scenarioEx2 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "S500:W5:R:S5000:R:S30000");
                command(2, "S500:W6:R:S5000:R:S30000");
                command(3, "S500:R:S500:R:S10000", 20000);
            }
        };
        Scenario scenarioEx3 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "S500:W1:R:S500:R:S8000",000);
                command(2, "S500:W2:R:S500:R:S8000",100);
                command(3, "S500:W3:R:S500:R:S8000",200);
            }
        };

        scenarioEx2.executeOn(topologyEx1And2);

        System.exit(0);
    }
}
