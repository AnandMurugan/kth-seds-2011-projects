/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.broadcast.un.UnBroadcast;
import se.kth.ict.id2203.broadcast.un.UnDeliver;
import se.kth.ict.id2203.broadcast.un.UnreliableBroadcast;
import se.sics.kompics.*;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author Igor
 */
public class Application2 extends ComponentDefinition {
    //ports
    Positive<UnreliableBroadcast> un = requires(UnreliableBroadcast.class);
    Positive<Timer> timer = requires(Timer.class);
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Application2.class);
    //local variables
    private String[] commands;
    private int lastCommand;

    public Application2() {
        subscribe(initHandler, control);
        subscribe(startHandler, control);
        subscribe(continueHandler, timer);
        subscribe(unDeliverHandler, un);
    }
    //handlers
    Handler<ApplicationInit> initHandler = new Handler<ApplicationInit>() {
        @Override
        public void handle(ApplicationInit event) {
            commands = event.getCommandScript().split(":");
            lastCommand = -1;
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
    Handler<UnDeliver> unDeliverHandler = new Handler<UnDeliver>() {
        @Override
        public void handle(UnDeliver event) {
            logger.info("Received broadcast message '{}' from {}", event.getMessage(), event.getSource());
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
        } else if (cmd.startsWith("B")) {
            doBroadcast(cmd.substring(1));
            doNextCommand();
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
            doNextCommand();
        }
    }

    private void doHelp() {
        logger.info("Available commands: S<n>, B<m> help, X");
        logger.info("Sn: sleeps 'n' milliseconds before the next command");
        logger.info("Bm: broadcasts message 'm'");
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

    private void doBroadcast(String message) {
        logger.info("Broadcasting message '{}'...", message);
        trigger(new UnBroadcast(message), un);
    }
}
