/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.pfd.PerfectFailureDetector;
import se.kth.ict.id2203.pfd.Crash;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author julio
 */
public final class Application1a extends ComponentDefinition {
    Positive<PerfectFailureDetector> pfd = requires(PerfectFailureDetector.class);
    Positive<Timer> timer = requires(Timer.class);
    private static final Logger logger = LoggerFactory.getLogger(Application1a.class);
    private String[] commands;
    private int lastCommand;
    private Set<Address> neighborSet;
    private Address self;

    public Application1a() {
        subscribe(handlerInit, control);
        subscribe(handlerStart, control);
        subscribe(handlerContinue, timer);
        subscribe(handlerCrash, pfd);

    }
    Handler<Application1aInit> handlerInit = new Handler<Application1aInit>() {
        @Override
        public void handle(Application1aInit e) {
            commands = e.getCommandScript().split(":");
            lastCommand = -1;
            neighborSet = e.getNeighborSet();
            self = e.getSelf();
        }
    };
    Handler<ApplicationContinue> handlerContinue = new Handler<ApplicationContinue>() {
        public void handle(ApplicationContinue event) {
            doNextCommand();
        }
    };
    Handler<Start> handlerStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            doNextCommand();
        }
    };
    Handler<Crash> handlerCrash = new Handler<Crash>() {
        @Override
        public void handle(Crash e) {
            logger.info("A node crash. Address:{}", e.getNodeCrashed().toString());
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
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
            doNextCommand();
        }
    }

    private void doHelp() {
        logger.info("Available commands: S<n>, help, X");
        logger.info("Sn: sleeps 'n' milliseconds before the next command");
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
}
