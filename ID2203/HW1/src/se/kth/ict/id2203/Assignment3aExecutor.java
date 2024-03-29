/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import se.sics.kompics.launch.Scenario;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author julio
 */
public class Assignment3aExecutor {
    private static final int NODES = 3;

    public static void main(String[] args) {
        Topology topology1 = new Topology() {
            {
                node(1, "127.0.0.1", 10001);
                node(2, "127.0.0.1", 10002);
                node(3, "127.0.0.1", 10003);

                //link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(0000, 0.0);
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
        Scenario scenario0 = new Scenario(Assignment3aMain.class) {
            {
                command(1, "S0");
                command(2, "S0");
                command(3, "S0");
            }
        };
        Scenario scenario1 = new Scenario(Assignment3aMain.class) {
            {
                command(1, "S3000:R");
                command(2, "S500:W4:S2500");
                command(3, "S1000:R");
            }
        };
        Scenario scenario2 = new Scenario(Assignment3aMain.class) {
            {
                command(1, "S500:BHello from 1");
                command(2, "S10500:BHello from 2");
                command(3, "S20500:BHello from 3");
                command(4, "S30500:BHello from 4");
            }
        };
        Scenario scenario3 = new Scenario(Assignment3aMain.class) {
            {
                command(1, "S500:Bdebug1");
                command(2, "S2000:X");
                command(3, "S2000:X");
                command(4, "S2000:X");
            }
        };
        Scenario scenario4 = new Scenario(Assignment3aMain.class) {
            {
                command(1, "S100:Ba1:"
                        + "S200:Ba2:"
                        + "S200:Ba3:"
                        + "S200:Ba4:"
                        + "S200:Ba5:"
                        + "S200:Ba6:"
                        + "S200:Ba7:"
                        + "S200:Ba8:"
                        + "S200:Ba9:"
                        + "S200:Ba10");
                command(2, "S100");
                command(3, "S150:Bb1:"
                        + "S250:Bb2:"
                        + "S250:Bb3:"
                        + "S250:Bb4:"
                        + "S250:Bb5:"
                        + "S250:Bb6:"
                        + "S250:Bb7:"
                        + "S250:Bb8:"
                        + "S250:Bb9:"
                        + "S250:Bb10");
                for (int i = 4; i <= NODES; i++) {
                    command(i, "S100");
                }
            }
        };

        scenario0.executeOn(topology1);

        System.exit(0);
    }
}
