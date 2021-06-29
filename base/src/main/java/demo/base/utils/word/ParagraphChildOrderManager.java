package demo.base.utils.word;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ParagraphChildOrderManager {
    public static int TYPE_RUN = 1;
    public static int TYPE_OMATH = 2;

    List<Integer> typeList = new ArrayList<>();
    XWPFParagraph p;

    public ParagraphChildOrderManager(XWPFParagraph paragraph) {
        this.p = paragraph;
        List<CTOMathPara> oMathParaList = paragraph.getCTP().getOMathParaList();
        //using a cursor to go through the paragraph from top to down
        XmlCursor xmlcursor = paragraph.getCTP().newCursor();
        while (xmlcursor.hasNextToken()) {
            XmlCursor.TokenType tokenType = xmlcursor.toNextToken();
            if (tokenType.isStart()) {
                if (xmlcursor.getName().getPrefix().equalsIgnoreCase("w") && xmlcursor.getName().getLocalPart().equalsIgnoreCase("r")) {
                    typeList.add(TYPE_RUN);
                } else if (xmlcursor.getName().getLocalPart().equalsIgnoreCase("oMath")) {
                    typeList.add(TYPE_OMATH);
                }
            } else if (tokenType.isEnd()) {
                xmlcursor.push();
                xmlcursor.toParent();
                if (xmlcursor.getName().getLocalPart().equalsIgnoreCase("p")) {
                    break;
                }
                xmlcursor.pop();
            }
        }
    }

    public List<Object> getChildList() {
        List<Object> runsOrMathList = new ArrayList<>();
        List<XWPFRun> runs = p.getRuns();
        List<CTOMath> oMathList = p.getCTP().getOMathList();
        int totalRuns = runs.size() + oMathList.size();
        if (typeList.size() != totalRuns) {
            throw new RuntimeException(" word 文件解析公式有问题，请与管理员联系");
        }
        Queue<XWPFRun> runsQueue = new LinkedList<>(runs);
        Queue<CTOMath> mathQueue = new LinkedList<>(oMathList);
        for (int i = 0; i < typeList.size(); i++) {
            Integer type = typeList.get(i);
            if (type.equals(TYPE_RUN) && runs.size() > 0) {
                runsOrMathList.add(runsQueue.poll());
            } else if (type.equals(TYPE_OMATH) && mathQueue.size() > 0) {
                runsOrMathList.add(mathQueue.poll());
            }
        }
        return runsOrMathList;
    }
}
