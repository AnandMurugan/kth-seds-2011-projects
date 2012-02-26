/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application.assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.ApplicationContinue;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.consensus.uniform.UcDecide;
import se.kth.ict.id2203.consensus.uniform.UniformConsensus;
import se.kth.ict.id2203.registers.atomic.AtomicRegister;
import se.kth.ict.id2203.registers.atomic.ReadRequest;
import se.kth.ict.id2203.registers.atomic.ReadResponse;
import se.kth.ict.id2203.registers.atomic.WriteRequest;
import se.kth.ict.id2203.registers.atomic.WriteResponse;
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
            logger.info("UcDecide : [id={}, v={}]", event.getId(), event.getValue());
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
        } else if (cmd.equals("R")) {
            doRead();
            //doNextCommand();
        } else if (cmd.startsWith("W")) {
            doWrite(Integer.parseInt(cmd.substring(1)));
            //doNextCommand();
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
            doNextCommand();
        }
    }

    private void doHelp() {
        logger.info("Available commands: S<n>, R, W<v>, help, X");
        logger.info("Sn: sleeps 'n' milliseconds before the next command");
        logger.info("R: reads value from register 0");
        logger.info("Wv: writes value 'v' to register 0");
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

    private void doRead() {
        logger.info("Reading from register 0...");
        trigger(new ReadRequest(0), uc);
    }

    private void doWrite(int value) {
        logger.info("Writing {} to register 0...", value);
        trigger(new WriteRequest(0, value), uc);
    }
}
