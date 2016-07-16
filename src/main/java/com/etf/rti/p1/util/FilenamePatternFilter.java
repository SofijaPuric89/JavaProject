package com.etf.rti.p1.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Filter filenames by provided string pattern
 */
public class FilenamePatternFilter implements FilenameFilter {

    private Pattern pattern;

    public FilenamePatternFilter(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
    }
}
