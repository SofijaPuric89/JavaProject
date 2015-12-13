package com.etf.rti.p1.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Created by zika on 09.12.2015..
 */
public class PatterFileFilter implements FilenameFilter {

    private Pattern reg;

    public PatterFileFilter(String pattern) {
        reg = Pattern.compile(pattern);
    }

    public boolean accept(File dir, String name) {
        return reg.matcher(name).matches();
    }
}
