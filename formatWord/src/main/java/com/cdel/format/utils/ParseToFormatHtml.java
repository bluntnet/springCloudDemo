package com.cdel.format.utils;

import com.cdel.format.bean.InputBean;
import com.sun.javafx.binding.StringFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseToFormatHtml {
    public static String END_LINE = "\n";
    public static String TWO_SPACE_INDENT = "　　";
    private InputBean inputBean;

    public ParseToFormatHtml(InputBean inputBean) {
        this.inputBean = inputBean;
    }

    public String parseStringToHtml() {
        String html = handleFormat();
        return handleImageTag(html);
    }

    private String handleFormat() {
        String html = inputBean.getContent();
        String chapter = inputBean.getChapter();
        boolean paragraphIndent = inputBean.isParagraphIndent();
        StringBuilder sb = new StringBuilder();
        Document document = Jsoup.parseBodyFragment(html);
        Elements elements = document.body().children();
        if (elements.size() == 0) {
            return "";
        }
        String format = String.format(ParseHtmlConstant.NODE_TEXT_START_TEMPLATE, chapter, "00:00:01", "");
        sb.append(format + END_LINE);
        boolean isQuestionMath = false;
        boolean needIndent = true;
        for (Element element : elements) {
            String tagName = element.tagName();
            if (tagName.equals(ParseHtmlConstant.TAG_P)) {
                String text = element.text();
                needIndent = true;
                Matcher matcher = ParseHtmlConstant.REG_TIME_ANCHOR.matcher(text);
                if (matcher.find()) {
                    if (isQuestionMath) {
                        sb.append(ParseHtmlConstant.XITI_END + END_LINE);
                        isQuestionMath = false;
                    }
                    sb.append(ParseHtmlConstant.NODE_TEXT_END + END_LINE);
                    String matchText = matcher.group(0);
                    String format1 = String.format(ParseHtmlConstant.NODE_TEXT_START_TEMPLATE, chapter, matchText, "");
                    sb.append(format1 + END_LINE);
                    continue;
                }
                if (ParseHtmlConstant.REG_QUESTION.matcher(text).matches()) {
                    sb.append(ParseHtmlConstant.XITI_START + END_LINE);
                    isQuestionMath = true;
                    needIndent = false;
                }
                if (ParseHtmlConstant.REG_RIGHT_ANSWER.matcher(text).matches()) {
                    sb.append(ParseHtmlConstant.XITI_RIGHT_ANSWER);
                    needIndent = false;
                }
                if (paragraphIndent && needIndent) {
                    sb.append(TWO_SPACE_INDENT);
                }
                handlePElement(element );
                sb.append(element.outerHtml() + END_LINE);
            } else if (tagName.equals(ParseHtmlConstant.TAG_TABLE)) {
                String content = parseTable(element);
                sb.append(content);
            }
        }
        sb.append(ParseHtmlConstant.NODE_TEXT_END + END_LINE);
        return sb.toString();
    }
    private void handlePElement(Element p) {
        Elements children = p.children();
        for (Element e : children) {
            if (!e.hasText()) {
                e.remove();
                continue;
            }
            if(inputBean.isReplaceTagU()){
                if (e.tagName().equals("u")) {
                    e.tagName("span");
                    e.attr("class", "font14zd");
                }
            }
        }
    }
    public String parseTable(Element element) {
        // style="border-collapse: collapse;" border="1" cellspacing="0" cellpadding="5"
        element.attr("class", "font14")
                .attr("border", "1")
                .attr("cellSpacing", "0")
                .attr("borderColorLight", "#000000")
                .attr("borderColorDark", "#ffffff")
                .attr("cellPadding", "2")
                .attr("width", "90%")
                .attr("align", "center");

        Elements trElement = element.children().first().children();
        for (Element tr : trElement) {
            Elements tdElements = tr.children();
            for (Element td : tdElements) {
                Elements children = td.children();
                for (Element pElement : children) {
                    handlePElement(pElement);
                }
            }
        }
        return element.outerHtml() + END_LINE;
    }

    public static String handleImageTag(String parseContent) {
        //例如 把 "<p>内容</p>" 转成 "内容<br>" 的内容
        String replaced = parseContent.replace("<p>", "").replace("</p>", "<br>");
        // 处理图片，把 aaaaaa 转成 html 的 img 标签
        Pattern p = Pattern.compile("aaaaaa");
        Matcher m = p.matcher(replaced);
        int index = 1;
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String image = toImageElement(index++);
            m.appendReplacement(sb, image);
        }
        m.appendTail(sb);


        return sb.toString();
    }

    private static String toImageElement(int index) {
        String template = "<img src=\"images0101/%s.png\" align=\"absmiddle\">";
        String indexStr = null;
        if (index < 10) {
            indexStr = "0" + index;
        } else {
            indexStr = String.valueOf(index);
        }
        return String.format(template, indexStr);
    }

    public static void main(String[] args) {
        Pattern p = Pattern.compile("cat");
        Matcher m = p.matcher("one cat two cats in the yard");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "dog");
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
    }
}
