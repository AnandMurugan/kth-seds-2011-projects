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
public final class Assignment1bExecutor {
    public static void main(String[] args) {
        Topology topology = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);
                node(3, "127.0.0.1", 10003);
                node(4, "127.0.0.1", 10004);

                link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(500, 0);
            }
        };

        Scenario scenario1 = new Scenario(Assignment1bMain.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario2 = new Scenario(Assignment1bMain.class) {
            {
                command(1, "S2500:X").recover("S500", 3000);
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario1_lossy = new Scenario(Assignment1bMain_Lossy.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario2_lossy = new Scenario(Assignment1bMain_Lossy.class) {
            {
                command(1, "S2500:X").recover("S500", 300);
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        scenario1_lossy.executeOn(topology);

        System.exit(0);
    }
}
