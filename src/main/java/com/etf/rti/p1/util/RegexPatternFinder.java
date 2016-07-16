package com.etf.rti.p1.util;

import com.etf.rti.p1.exceptions.ERegexFail;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zika on 09.12.2015..
 */
public class RegexPatternFinder {

    public static String find(String source, String pattern, int groupIndex) throws ERegexFail {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        if (m.find()) {
            MatchResult r = m.toMatchResult();
            String ret = r.group(groupIndex);
            if (ret == null) {
                throw new ERegexFail(String.format("Group %d not present in pattern %s", groupIndex, pattern));
            }
            return ret;
        }
        throw new ERegexFail(String.format("Pattern %s not present in string %s", pattern, source));
    }

}
