package demo.base.utils.word;

import demo.base.utils.number.IWordNumber;
import demo.base.utils.number.WordNumberFactory;
import demo.base.utils.utils.OmmlUtils;
import demo.base.utils.utils.WmfUtils;
import demo.base.utils.utils.WordMyUnits;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseWordUtils {
    public static String parseDocxContentToHTML(InputStream input, ImageParse imageParser) throws IOException {
        if (input == null) {
            throw new RuntimeException("请输入要转的docx的文件");
        }
        StringBuilder content = new StringBuilder();
        Map<BigInteger, IWordNumber> wordNumberMap = new HashMap<BigInteger, IWordNumber>();
        XWPFDocument docx = new XWPFDocument(input);
        // 拿到所有的段落的表格，这两个属于同级无素
        List<IBodyElement> elements = docx.getBodyElements();
        for (IBodyElement body : elements) {
            if (body.getElementType().equals(BodyElementType.PARAGRAPH)) {
                handleParagraph(content, body, wordNumberMap, imageParser);
            } else if (body.getElementType().equals(BodyElementType.TABLE)) {
                handleTable(content, body, wordNumberMap, imageParser);
            }
        }
        return content.toString();
    }

    private static void handleParagraph(StringBuilder content, IBodyElement body, Map<BigInteger, IWordNumber> wordNumberMap, ImageParse imageParser) {
        XWPFParagraph p = (XWPFParagraph) body;
        if (p.isEmpty() || p.isWordWrap() || p.isPageBreak()) {
            return;
        }
        String tagName = "p";
        content.append("<" + tagName + ">");
        // 自动编号
        handleWordNumber(content, wordNumberMap, p);
      /*XWPFParagraph 有两个方法可以分别提出XWPFRun和CTOMath，但是不知道位置
      ParagraphChildOrderManager这个类是专门解决这个问题的
      */
        ParagraphChildOrderManager runOrMaths = new ParagraphChildOrderManager(p);
        List<Object> childList = runOrMaths.getChildList();
        for (Object child : childList) {
            if (child instanceof XWPFRun) {
                handleParagraphRun(content, (XWPFRun) child, imageParser);
            } else if (child instanceof CTOMath) {
                handleParagraphOMath(content, (CTOMath) child, imageParser);
            }
        }
        content.append("</" + tagName + ">");
    }

    private static String handleTable(StringBuilder content, IBodyElement body, Map<BigInteger, IWordNumber> wordNumberMap, ImageParse imageParser) {
        XWPFTable table = (XWPFTable) body;
        List<XWPFTableRow> rows = table.getRows();

        String tableHtml =HandleWordTable.getTableHtml(table);

        content.append(tableHtml);
        /*

        content.append("<table style=\"border-collapse: collapse;\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\">");


        for (XWPFTableRow row : rows) {
            content.append("<tr>");
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                content.append("<td>");
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    handleParagraph(content, paragraph, wordNumberMap, imageParser);
                }
                content.append("</td>\n");
            }
            content.append("</tr>\n");
        }
        content.append("</table>\n");*/
        return null;
    }

    // 处理自动编号的代码如下
    private static void handleWordNumber(StringBuilder sb, Map<BigInteger, IWordNumber> wordNumberMap, XWPFParagraph p) {
        String prefix = null;
        if (wordNumberMap != null && p.getNumID() != null && p.getNumFmt() != null) {
            IWordNumber wn = wordNumberMap.get(p.getNumID());
            if (wn != null) {
                prefix = wn.nextNum();
            } else {
                IWordNumber newWordNumber = WordNumberFactory.getWordNumber(p.getNumFmt(), p.getNumLevelText());
                if (newWordNumber != null) {
                    wordNumberMap.put(p.getNumID(), newWordNumber);
                    prefix = newWordNumber.nextNum();
                }
            }
        }
        if (StringUtils.isNotBlank(prefix)) {
            sb.append(prefix);
        }
    }

    // 处理XWPFRun
    private static void handleParagraphRun(StringBuilder content, XWPFRun run, ImageParse imageParser) {
//char[] runChars = run.toString().toCharArray();
        // 有内嵌的图片
        List<XWPFPicture> pics = run.getEmbeddedPictures();
        if (pics != null && pics.size() > 0) {
            handleParagraphRunsImage(content, pics, imageParser);
        } else if (isMath(run)) {// 处理wps的公式图片
            handleParagraphRunsImageMath(content, run, imageParser);
        } else { //处理文本
            handleParagraphRunsWithText(content, run);
        }
    }

    // 处理图片
    private static void handleParagraphRunsImage(StringBuilder content, List<XWPFPicture> pics, ImageParse imageParser) {
        for (XWPFPicture pic : pics) {
            String desc = pic.getDescription();

            String path = imageParser.parse(pic.getPictureData().getData(),
                    pic.getPictureData().getFileName());

            CTPicture ctPicture = pic.getCTPicture();
            Node domNode = ctPicture.getDomNode();

            Node extNode = getChildChainNode(domNode, "pic:spPr", "a:ext");
            NamedNodeMap attributes = extNode.getAttributes();
            if (attributes != null && attributes.getNamedItem("cx") != null) {
                int width = WordMyUnits.emuToPx(new Double(attributes.getNamedItem("cx").getNodeValue()));
                int height = WordMyUnits.emuToPx(new Double(attributes.getNamedItem("cy").getNodeValue()));
                content.append(String.format("<img src=\"%s\" width=\"%d\" height=\"%d\" />", path, width, height));
            } else {
                content.append(String.format("<img src=\"%s\" />", path));
            }
        }
    }

    private static Node getChildNode(Node node, String nodeName) {
        if (!node.hasChildNodes()) {
            return null;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (nodeName.equals(childNode.getNodeName())) {
                return childNode;
            }
            childNode = getChildNode(childNode, nodeName);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }

    private static Node getChildChainNode(Node node, String... nodeName) {
        Node childNode = node;
        for (int i = 0; i < nodeName.length; i++) {
            String tmp = nodeName[i];
            childNode = getChildNode(childNode, tmp);
            if (childNode == null) {
                return null;
            }
        }
        return childNode;
    }

    // 处理wps的内嵌的图片公式，里面有wmf to svg的工具类
    private static boolean isMath(XWPFRun run) {
        Node runNode = run.getCTR().getDomNode();
        Node objectNode = getChildNode(runNode, "w:object");
        if (objectNode == null) {
            return false;
        }
        Node shapeNode = getChildNode(objectNode, "v:shape");
        if (shapeNode == null) {
            return false;
        }
        Node imageNode = getChildNode(shapeNode, "v:imagedata");
        if (imageNode == null) {
            return false;
        }
        Node binNode = getChildNode(objectNode, "o:OLEObject");
        if (binNode == null) {
            return false;
        }
        return true;
    }

    private static void handleParagraphRunsImageMath(StringBuilder content, XWPFRun run, ImageParse imageParser) {
        Node runNode = run.getCTR().getDomNode();
        XWPFDocument runDocument = run.getDocument();
        Node objectNode = getChildNode(runNode, "w:object");
        if (objectNode == null) {
            return;
        }
        Node shapeNode = getChildNode(objectNode, "v:shape");
        if (shapeNode == null) {
            return;
        }
        Node imageNode = getChildNode(shapeNode, "v:imagedata");
        if (imageNode == null) {
            return;
        }
        Node binNode = getChildNode(objectNode, "o:OLEObject");
        if (binNode == null) {
            return;
        }
        NamedNodeMap shapeAttrs = shapeNode.getAttributes();
        // 图片在Word中显示的宽高
        String style = shapeAttrs.getNamedItem("style").getNodeValue();

        NamedNodeMap imageAttrs = imageNode.getAttributes();
        // 图片在Word中的ID
        String imageRid = imageAttrs.getNamedItem("r:id").getNodeValue();
        // 获取图片信息
        PackagePart imgPart = runDocument.getPartById(imageRid);
        // word/media/image4.wmf
        String fullName = imgPart.getPartName().getName();

        String extName = imgPart.getPartName().getExtension();
        Pattern p = Pattern.compile("\\w+\\." + extName);
        Matcher matcher = p.matcher(fullName);
        if (matcher.find()) {
            fullName = matcher.group(0);
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            WmfUtils.toSvg(imgPart.getInputStream(), out);
            fullName = fullName.replace(extName, "svg");
            String path = imageParser.parse(new ByteArrayInputStream(out.toByteArray()), fullName);
            content.append("<img src=\"" + path + "\" style=\"" + style + "\"/>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 处理文本的html样式
    private static void handleParagraphRunsWithText(StringBuilder content, XWPFRun run) {
        String c = run.toString();

        c = escapeHtmlTag(c);
        if (c == null || c.length() == 0) {
            return;
        }
        if (run.getSubscript() != null) {
            VerticalAlign va = run.getSubscript();
            if (va.equals(VerticalAlign.SUBSCRIPT)) {
                c = "<sub>" + c + "</sub>";
            } else if (va.equals(VerticalAlign.SUPERSCRIPT)) {
                c = "<sup>" + c + "</sup>";
            }
        }
        if (run.isBold()) {
            c = "<b>" + c + "</b>";
        } else if (run.isItalic()) {
            c = "<i>" + c + "</i>";
        } else if (run.isStrikeThrough()) {
            c = "<strike>" + c + "</strike>";
        }
        if (run.getUnderline() != null && run.getUnderline() != UnderlinePatterns.NONE) {
            c = "<span style='text-decoration: underline;'>" + c + "</span>";
        }
        if (c.equals("\n")) {
            c = "<br>";
        } else {
            if (run.getColor() != null && !run.getColor().equals("000000")) {
                c = "<span style='color:#" + run.getColor().toLowerCase() + ";'>" + c + "</span>";
            }
        }

        content.append(c);
    }

    private static String escapeHtmlTag(String text) {
        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        return text;
    }

    private static void handleParagraphOMath(StringBuilder content, CTOMath child, ImageParse imageParser) {
        String s = OmmlUtils.convertOmathToPng(child, imageParser);
        content.append(s);
    }

    public static void main(String[] args) throws IOException {
        String filepath = "D:\\开发文档\\课件讲义\\table_demo.docx";
        String target = "D:\\开发文档\\课件讲义\\parse";
        String html = target + "\\demo_index.html";
        String s = ParseWordUtils.parseDocxContentToHTML(new FileInputStream(filepath), new ImageParse(target, ""));
        System.out.println(s);
        Files.write(Paths.get(html), s.getBytes());
    }
}
