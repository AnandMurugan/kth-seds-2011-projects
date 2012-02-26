/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application.assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.ApplicationContinue;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.consensus.uniform.UcDecide;
import se.kth.ict.id2203.consensus.uniform.UcPropose;
import se.kth.ict.id2203.consensus.uniform.UniformConsensus;
import se.sics.kompics.*;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author Igor
 */
public final class Application4 extends ComponentDefinition {
    //ports
    Positive<UniformConsensus> uc = requires(UniformConsensus.class);
    Positive<Timer> timer = requires(Timer.class);
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Application4.class);
    //local variables
    private String[] commands;
    private int lastCommand;
    private Map<Integer, Object> decisions;
    private Set<Integer> ongoing;
    private boolean waiting;
    private long delayTime;

    public Application4() {
        subscribe(initHandler, control);
        subscribe(startHandler, control);
        subscribe(continueHandler, timer);
        subscribe(ucDecideHandler, uc);
    }
    //handlers
    Handler<ApplicationInit> initHandler = new Handler<ApplicationInit>() {
        @Override
        public void handle(ApplicationInit event) {
            commands = event.getCommandScript().split(":");
            lastCommand = -1;
            decisions = new TreeMap<Integer, Object>();
            ongoing = new HashSet<Integer>();
        }
    };
    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            doNextCommand();
        }
    };
    Handler<ApplicationContinue> continueHandler = new Handler<ApplicationContinue>() {
        @Override
        public void handle(ApplicationContinue event) {
            doNextCommand();
        }
    };
    Handler<UcDecide> ucDecideHandler = new Handler<UcDecide>() {
        @Override
        public void handle(UcDecide event) {
            logger.info("Decision : [id={}, v={}]", event.getId(), event.getValue());

            decisions.put(event.getId(), event.getValue());
            ongoing.remove(event.getId());
            if (waiting && ongoing.isEmpty()) {
                waiting = false;

                ScheduleTimeout st = new ScheduleTimeout(delayTime);
                st.setTimeoutEvent(new ApplicationContinue(st));
                trigger(st, timer);
            }
        }
    };

    //methods
    private void doNextCommand() {
        lastCommand++;

        if (lastCommand > commands.length) {
            return;
        }
        if (lastCommand == commands.length) {
            logger.info("DONE ALL OPERATIONS");
            Thread applicationThread = new Thread("ApplicationThread") {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(System.in));
                    while (true) {
                        try {
                            String line = in.readLine();
                            doCommand(line);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            applicationThread.start();
            return;
        }
        String op = commands[lastCommand];
        doCommand(op);
    }

    private void doCommand(String cmd) {
        if (cmd.startsWith("S")) {
            doSleep(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("X")) {
            doShutdown();
        } else if (cmd.equals("help")) {
            doHelp();
            doNextCommand();
        } else if (cmd.equals("W")) {
            doPrint();
            doNextCommand();
        } else if (cmd.startsWith("P")) {
            int index = cmd.indexOf("-");
            int i = Integer.parseInt(cmd.substring(1, index));
            int j = Integer.parseInt(cmd.substring(index + 1));
            doPropose(i, j);
            doNextCommand();
        } else if (cmd.startsWith("D")) {
            doWait(Integer.parseInt(cmd.substring(1)));
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
            doNextCommand();
        }
    }

    private void doHelp() {
        logger.info("Available commands: S<n>, P<i>-<j>, D<k>, W, help, X");
        logger.info("Sn: sleeps 'n' milliseconds before the next command");
        logger.info("Pi-j: proposes the value 'j' for a consensus instance 'i'");
        logger.info("Dk: waits until decision to all previous proposals made by the node, then sleeps 'k' milliseconds");
        logger.info("W: prints all recieved decisions");
        logger.info("help: shows this help message");
        logger.info("X: terminates this process");
    }

    private void doSleep(long delay) {
        logger.info("Sleeping {} milliseconds...", delay);

        ScheduleTimeout st = new ScheduleTimeout(delay);
        st.setTimeoutEvent(new ApplicationContinue(st));
        trigger(st, timer);
    }

    private void doShutdown() {
        System.out.println("2DIE");
        System.out.close();
        System.err.close();
        Kompics.shutdown();
    }

    private void doPrint() {
        logger.info("Decisions:");

        for (Entry<Integer, Object> d : decisions.entrySet()) {
            logger.info("\tid=" + d.getKey() + " value=" + d.getValue());
        }
    }

    private void doPropose(int id, int value) {
        logger.info("Proposing value={} for consensus instance {}...", value, id);

        ongoing.add(id);
        trigger(new UcPropose(id, new Integer(value)), uc);
    }

    private void doWait(long delay) {
        waiting = true;
        delayTime = delay;
    }
}
