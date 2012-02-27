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
public class Assignment4Executor {
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

        Scenario scenario0 = new Scenario(Assignment4Main.class) {
            {
                command(1, "S0");
                command(2, "S0");
                command(3, "S0");
                command(4, "S0");
            }
        };


        scenario0.executeOn(topology0);

        System.exit(0);
    }
}
