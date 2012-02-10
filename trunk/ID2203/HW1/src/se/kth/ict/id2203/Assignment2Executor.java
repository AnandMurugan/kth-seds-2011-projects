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
public final class Assignment2Executor {
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
                defaultLinks(500, 0.0);
            }
        };

        Scenario scenario1 = new Scenario(Assignment2Main.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario2 = new Scenario(Assignment2Main.class) {
            {
                command(1, "S500:BHello from 1");
                command(2, "S10500:BHello from 2");
                command(3, "S20500:BHello from 3");
                command(4, "S30500:BHello from 4");
            }
        };

        scenario1.executeOn(topology1);

        System.exit(0);
    }
}
