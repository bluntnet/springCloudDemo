package com.cdel.format.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseToFormatHtml {
    public static String END_LINE = "\n";

    public static String parseStringToHtml(String html, String chapter) {
        StringBuilder sb = new StringBuilder();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("p,table,h1,h2,h3,image");
        String format = String.format(ParseHtmlConstant.NODE_TEXT_START_TEMPLATE, chapter, "00:00:01", "");
        sb.append(format + END_LINE);
        boolean isQuestionMath = false;
        for (Element element : elements) {
            if (element.tagName().equals(ParseHtmlConstant.TAG_P)) {
                String text = element.text();
                if (ParseHtmlConstant.REG_TIME_ANCHOR.matcher(text).matches()) {
                    if (isQuestionMath) {
                        sb.append(ParseHtmlConstant.XITI_END + END_LINE);
                        isQuestionMath = false;
                    }
                    sb.append(ParseHtmlConstant.NODE_TEXT_END + END_LINE);
                    String format1 = String.format(ParseHtmlConstant.NODE_TEXT_START_TEMPLATE, chapter, text, "");
                    sb.append(format1 + END_LINE);
                    continue;
                }
                if (ParseHtmlConstant.REG_QUESTION.matcher(text).matches()) {
                    sb.append(ParseHtmlConstant.XITI_START + END_LINE);
                    isQuestionMath = true;
                }
                if (ParseHtmlConstant.REG_RIGHT_ANSWER.matcher(text).matches()) {
                    sb.append(ParseHtmlConstant.XITI_RIGHT_ANSWER);
                }
                sb.append(element.outerHtml() + END_LINE);
            }
        }

        sb.append(ParseHtmlConstant.NODE_TEXT_END + END_LINE);
        return sb.toString();
    }
}
