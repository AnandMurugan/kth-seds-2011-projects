/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.registers.atomic.AtomicRegister;
import se.kth.ict.id2203.registers.atomic.ReadRequest;
import se.kth.ict.id2203.registers.atomic.ReadResponse;
import se.kth.ict.id2203.registers.atomic.WriteRequest;
import se.kth.ict.id2203.registers.atomic.WriteResponse;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author julio
 */
public class Application3a extends ComponentDefinition {
    Positive<AtomicRegister> nnar = requires(AtomicRegister.class);
    Positive<Timer> timer = requires(Timer.class);
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Application3a.class);
    //local variables
    private String[] commands;
    private int lastCommand;

    public Application3a() {
        subscribe(initHandler, control);
        subscribe(startHandler, control);
        subscribe(continueHandler, timer);
        subscribe(ReadResponseHandler, nnar);
        subscribe(WriteResponseHandler, nnar);
        //receivedMessages = new StringBuilder();
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
            logger.info("STARTING...............");
            doNextCommand();
        }
    };
    Handler<ApplicationContinue> continueHandler = new Handler<ApplicationContinue>() {
        @Override
        public void handle(ApplicationContinue event) {
            doNextCommand();
        }
    };
    Handler<ReadResponse> ReadResponseHandler = new Handler<ReadResponse>() {
        @Override
        public void handle(ReadResponse event) {
            logger.info("READ response(register, value) : {},{}", event.getRegister(), event.getValue());
            doNextCommand();
        }
    };
    Handler<WriteResponse> WriteResponseHandler = new Handler<WriteResponse>() {
        @Override
        public void handle(WriteResponse event) {
            logger.info("WRITE response (register): {}", event.getRegister());
            doNextCommand();
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
        if (cmd.startsWith("D")) {
            doSleep(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("W")) {
            doWrite(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("R")) {
            doRead();
        } else if (cmd.startsWith("X")) {
            doShutdown();
        } else if (cmd.equals("help")) {
            doHelp();
            //doNextCommand();
        } else if (cmd.startsWith("P")) {
            //logger.info("Received Messages: {}", receivedMessages.toString());
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
            //doNextCommand();
        }
    }

    private void doHelp() {
        logger.info("Available commands: S<n>, B<m>, help, X");
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

    private void doWrite(int value) {
        logger.debug("WRITE request (register, value): {},{}", 0, value );
        WriteRequest req = new WriteRequest(0, value);
        trigger(req, nnar);
    }

    private void doRead() {
        logger.debug("READ request (register): {}", 0 );
        ReadRequest req = new ReadRequest(0);
        trigger(req, nnar);
    }
}
