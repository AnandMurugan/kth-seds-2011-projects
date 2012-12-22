package org.igorkos.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;

public class IK2001SecureClassLoader extends SecureClassLoader {

    protected URL urlBase;
    public boolean debug = true;

    public IK2001SecureClassLoader(String base, ClassLoader parent) {
        super(parent);
        try {
            if (!(base.endsWith("/"))) {
                base = base + "/";
            }
            urlBase = new URL(base);
        } catch (Exception e) {
            throw new IllegalArgumentException(base);
        }
    }

    private byte[] getClassBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        boolean eof = false;
        while (!eof) {
            try {
                int i = bis.read();
                if (i == -1) {
                    eof = true;
                } else baos.write(i);
            } catch (IOException e) {
                return null;
            }
        }
        return baos.toByteArray();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String urlName = name.replace('.', '/');
        byte buf[];
        Class clazz;

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            int i = name.lastIndexOf('.');
            if (i >= 0) {
                sm.checkPackageDefinition(name.substring(0, i));
            }
        }
        try {
            URL url = new URL(urlBase, urlName + ".ik2001");
            if (debug) {
                System.out.println("Loading " + url);
            }
            InputStream is = url.openConnection().getInputStream();
            buf = getClassBytes(is);
            clazz = defineClass(name, buf, 0, buf.length, new CodeSource(urlBase, (Certificate[]) null));
            return clazz;
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        }
    }
}
