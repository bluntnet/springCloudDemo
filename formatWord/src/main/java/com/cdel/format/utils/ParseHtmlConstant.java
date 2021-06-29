package com.cdel.format.utils;

import java.util.regex.Pattern;

public class ParseHtmlConstant {

    public static Pattern REG_TIME_ANCHOR = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");


    public static String NODE_TEXT_START_TEMPLATE = "<!-- NodeText(%s,%s,%s);-->";
    public static String NODE_TEXT_END = "<!-- NodeTextEnd();-->";

    public static String XITI_START = "<!-- XiTiStart();-->";
    public static String XITI_END = "<!-- XiTiEnd();-->";

    public static String XITI_RIGHT_ANSWER = "<!-- XiTi(\"3\");-->";

    public static String TAG_P = "p";
    public static String TAG_TABLE = "table";
    public static String TAG_IMAGE = "img";
    public static Pattern REG_QUESTION = Pattern.compile("【(单|多)选题】[\\s\\S]*");
    public static Pattern REG_RIGHT_ANSWER = Pattern.compile("【正确答案】[\\s\\S]*");

}
