package com.cdel.format.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseToFormatHtml {
    public static String END_LINE = "\n";

    public static String parseStringToHtml(String html, String chapter) {
        StringBuilder sb = new StringBuilder();
        Document document = Jsoup.parseBodyFragment(html);
//        Elements elements = document.select("p,table,h1,h2,h3");
        Elements elements = document.body().children();
        if (elements.size() == 0) {
            return "";
        }
        String format = String.format(ParseHtmlConstant.NODE_TEXT_START_TEMPLATE, chapter, "00:00:01", "");
        sb.append(format + END_LINE);
        boolean isQuestionMath = false;
        for (Element element : elements) {
            String tagName = element.tagName();
            if (tagName.equals(ParseHtmlConstant.TAG_P)) {
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
                sb.append("　　"+element.text() + "<br>" + END_LINE);
            } else if (tagName.equals(ParseHtmlConstant.TAG_TABLE)) {
                // style="border-collapse: collapse;" border="1" cellspacing="0" cellpadding="5"
                element.attr("class", "font14 ")
                        .attr("border", "1")
                        .attr("cellSpacing", "0")
                        .attr("borderColorLight", "#000000")
                        .attr("borderColorDark", "#ffffff")
                        .attr("cellpadding", "2")
                        .attr("width", "90%")
                        .attr("align", "center");

                sb.append(element.outerHtml() + END_LINE);
            }

        }
        sb.append(ParseHtmlConstant.NODE_TEXT_END + END_LINE);
        return sb.toString();
    }
}
