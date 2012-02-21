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
        Topology topology1 = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);
                node(3, "127.0.0.1", 10003);
                node(4, "127.0.0.1", 10004);

                //link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(100, 0.0);
            }
        };
        Topology topology2 = new Topology() {
            {
                for (int i = 1; i <= NODES; i++) {
                    node(i, "127.0.0.1", 10000 + i);
                }
                link(1, 2, 100, 0.5).bidirectional();
                link(2, 3, 100, 0.5).bidirectional();
                defaultLinks(100, 0.0);
            }
        };
        Scenario scenario1 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        scenario1.executeOn(topology1);

        System.exit(0);
    }
}
