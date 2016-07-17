package com.etf.rti.p1.compiler;

import com.etf.rti.p1.util.FilenamePatternFilter;
import com.etf.rti.p1.util.RegexPatternFinder;
import com.etf.rti.p1.util.Utils;
import org.antlr.v4.Tool;
import org.antlr.v4.tool.ErrorType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


/**
 * Created by zika on 09.12.2015..
 */
public class CompilerGenerator {
    private static final String packageName = "com.etf.rti.p1.generated";
    private String grammar;
    private File tempDir;

    public CompilerGenerator(String grammar) {
        this.grammar = grammar;
    }


    private String[] params() throws IOException {
        String[] ret = new String[5];
        ret[0] = grammar;

        tempDir = Files.createTempDirectory("temp" + Long.toString(System.nanoTime())).toFile();
        tempDir.deleteOnExit();

        ret[1] = "-o";
        ret[2] = tempDir.getAbsolutePath();
        ret[3] = "-package";
        ret[4] = packageName;
        return ret;
    }

    private void compile() throws Exception {
        String srcs = "";
        for (String x : tempDir.list(new FilenamePatternFilter(".*\\.java$"))) {
            String fileJava = new File(tempDir, x).getAbsolutePath();
            srcs += " " + fileJava;
        }
        Utils.runProcess("javac" + srcs, new String[]{"CLASSPATH=" +
                System.getProperty("java.class.path")});
    }

    private void build(String[] args) {
        Tool antlr = new Tool(args);
        if (args.length == 0) {
            antlr.help();
            antlr.exit(0);
        }

        try {
            antlr.processGrammarsOnCommandLine();
        } finally {
            if (antlr.log) {
                try {
                    String logname = antlr.logMgr.save();
                    System.out.println("wrote " + logname);
                } catch (IOException ioe) {
                    antlr.errMgr.toolError(ErrorType.INTERNAL_ERROR, ioe);
                }
            }
        }
    }

    private void copy() throws IOException {
        Path dstDir = new File(Utils.getResourcePath()).toPath();
        for (String s : packageName.split("\\.")) {
            dstDir = dstDir.resolve(s);
        }
        Files.createDirectories(dstDir);

        for (String x : tempDir.list(new FilenamePatternFilter(".*\\.class$"))) {
            File classFile = new File(tempDir.getAbsolutePath(), x);
            Files.copy(classFile.toPath(), dstDir.resolve(x), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * @return RuntimeCompiler generated with param args
     * @throws Exception
     */
    public Compiler generate() throws Exception {
        String[] args = params();
        build(args);
        compile();
        copy();

        String pattern = "([^\\:]*?)\\.[^\\.]*$";
        String grammarName = RegexPatternFinder.find(grammar.replace(File.separatorChar, ':'), pattern, 1);
        return new RuntimeCompiler(grammarName, packageName);

    }
}
