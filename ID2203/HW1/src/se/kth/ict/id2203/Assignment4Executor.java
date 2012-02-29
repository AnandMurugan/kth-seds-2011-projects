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

                defaultLinks(1000, 0.0);
            }
        };
        Topology topologyEx3 = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);
                node(3, "127.0.0.1", 10003);

                defaultLinks(1000, 0.0);
            }
        };
        Topology topologyEx4 = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);

                defaultLinks(2100, 0.0);
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
        Scenario scenario1 = new Scenario(Assignment4Main.class) {
            {
                command(1, "P1-7:D100:P3-5:P4-9:D20000:W");
                command(2, "S0");
                command(3, "S0");
                command(4, "S0");
            }
        };
        Scenario scenarioEx3 = new Scenario(Assignment4Main.class) {
            {
                command(1, "D2000:P1-1");
                command(2, "D2200:P1-2");
                command(3, "D2200:P1-3");
            }
        };
        Scenario scenarioEx4 = new Scenario(Assignment4Main.class) {
            {
                command(1, "P1-1:D0:W");
                command(2, "P1-2:D0:W");
            }
        };

        scenario0.executeOn(topology0);

        System.exit(0);
    }
}
