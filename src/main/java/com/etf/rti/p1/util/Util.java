package com.etf.rti.p1.util;

import java.io.*;

/**
 * TODO: think about creating SinGen runner class that will be in charge for running the processes.
 * TODO: think about importing commons.io with FileUtils classes that will take care about folder managing instead of Util
 */
public class Util {
    private static Util instance;

    private Util() {
    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public String getResourcePath() {
        return Util.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public int runProcess(String command, String[] vars) throws Exception {
        Process pro = Runtime.getRuntime().exec(command, vars);
        readOutput(pro.getErrorStream());
        readOutput(pro.getInputStream());
        pro.waitFor();
        return pro.exitValue();
    }

    private void readOutput(InputStream ins) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        while ((in.readLine()) != null) ;
    }
}
