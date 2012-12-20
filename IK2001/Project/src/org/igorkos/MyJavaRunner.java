package org.igorkos;

import org.igorkos.core.MySecureClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyJavaRunner implements Runnable {
    private final static int NUM_ARGS = 2;

    private ClassLoader cl;
    private Object args[];
    private String className;

    MyJavaRunner(ClassLoader cl, String className, Object args[]) {
        this.cl = cl;
        this.className = className;
        this.args = args;
    }

    private static Object[] getArgs(String args[]) {
        String passArgs[] = new String[args.length - NUM_ARGS];
        System.arraycopy(args, NUM_ARGS, passArgs, 0, args.length - NUM_ARGS);

        Object wrapArgs[] = new Object[1];
        wrapArgs[0] = passArgs;
        return wrapArgs;
    }

    @SuppressWarnings("unchecked")
    private void invokeMain(Class clazz) {
        Class argList[] = new Class[]{String[].class};
        Method mainMethod = null;
        try {
            mainMethod = clazz.getMethod("main", argList);
        } catch (NoSuchMethodException nsme) {
            System.out.println("No main method in " + clazz.getName());
            System.exit(-1);
        }

        try {
            mainMethod.invoke(null, args);
        } catch (Exception e) {
            Throwable t;
            if (e instanceof InvocationTargetException) {
                t = ((InvocationTargetException) e).getTargetException();
            } else {
                t = e;
            }
            System.out.println("Procedure exited with exception " + t);
            t.printStackTrace();
        }
    }

    @Override
    public void run() {
        Class target;
        try {
            target = cl.loadClass(className);
            invokeMain(target);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't load " + className);
        }
    }

    public static void main(String args[]) throws ClassNotFoundException {
        if (args.length < 1) {
            System.err.println("usage:  JavaRunner <location> <classfile>");
            System.exit(-1);
        }
        MySecureClassLoader cl = new MySecureClassLoader(args[0], ClassLoader.getSystemClassLoader());
        Thread t = new Thread(new MyJavaRunner(cl, args[1], getArgs(args)));
        t.start();
        try {
            t.join();
        } catch (InterruptedException ie) {
            System.out.println("Thread was interrupted");
        }
    }
}