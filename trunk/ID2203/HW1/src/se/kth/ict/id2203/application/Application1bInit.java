/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application;

import se.sics.kompics.Init;

/**
 *
 * @author Igor
 */
public class Application1bInit extends Init {
    private final String commandScript;

    public Application1bInit(String commandScript) {
        this.commandScript = commandScript;
    }

    public final String getCommandScript() {
        return commandScript;
    }
}
