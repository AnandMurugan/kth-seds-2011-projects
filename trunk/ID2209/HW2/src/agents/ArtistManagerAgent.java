/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import gui.ArtistManagerFrame;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.awt.EventQueue;
import model.Artifact;

/**
 *
 * @author Igor
 */
public class ArtistManagerAgent extends Agent {
    private ArtistManagerFrame gui;

    @Override
    protected void setup() {
        //Register the service in DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("artist-manager");
        sd.setName(getAID().getName() + "_artist-manager");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);

            /* Create and display the form */
            final ArtistManagerAgent agent = this;
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    gui = new ArtistManagerFrame(agent);
                    gui.setVisible(true);
                }
            });
        } catch (FIPAException ex) {
            doDelete();
        }

        System.out.println("Artist Manager [" + getAID().getName() + "] is ready...");
    }

    @Override
    protected void takeDown() {
        //gui.
        System.out.println("Artist Manager [" + getAID().getName() + "] is terminating...");
    }

    public void sellArtifact(String[] args) {
        String name = args[0];
        String creator = args[1];
        String description = args[2];
        String style = args[3];

        Artifact a = new Artifact(name, creator, description, style, null);

        addBehaviour(new DutchAuctionInitiatorBehaviour(this, a));
    }

    private class DutchAuctionInitiatorBehaviour extends SequentialBehaviour {
        public DutchAuctionInitiatorBehaviour(final Agent agent, final Artifact artifact) {
            super(agent);

        }
    }
}
