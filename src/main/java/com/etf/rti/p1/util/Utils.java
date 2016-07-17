package com.etf.rti.p1.util;

import java.io.*;

/**
 * TODO: think about creating SinGen runner class that will be in charge for running the processes.
 */
public class Utils {

    private Utils() {
    }

    public static String getResourcePath() {
        return Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public static int runProcess(String command, String[] vars) throws Exception {
        Process pro = Runtime.getRuntime().exec(command, vars);
        readOutput(pro.getErrorStream());
        readOutput(pro.getInputStream());
        pro.waitFor();
        return pro.exitValue();
    }

    private static void readOutput(InputStream ins) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        while ((in.readLine()) != null) ;
    }
}
