package demo.base.utils.utils;

import demo.base.utils.word.ImageParse;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OmmlUtils {
    /**
     * 直接转node有等号会出问题，先转成xml的string，再转成mathML的字符串
     *
     * @param node
     * @return
     * @throws IOException
     * @throws TransformerException
     */
    private static String getMathMLFromNode(Node node) throws IOException, TransformerException {
        final String xslFile = "/OMML2MML.XSL";
        StreamSource streamSource = new StreamSource(OmmlUtils.class.getResourceAsStream(xslFile));
        String s = W3cNodeUtil.node2XmlStr(node);
        // encoding utf-16
        String mathML = W3cNodeUtil.xml2Xml(s, streamSource);

        mathML = mathML.replaceAll("xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"", "");
        mathML = mathML.replaceAll("xmlns:mml", "xmlns");
        mathML = mathML.replaceAll("mml:", "");
        return mathML;
    }


    public static String convertOmathToPng(XmlObject xmlObject, ImageParse imageParser) {
        Document document = null;
        try {
            String mathMLStr = getMathMLFromNode(xmlObject.getDomNode());
            document = W3cNodeUtil.xmlStr2Node(mathMLStr, "utf-16");
            return documentToImageHTML(document, imageParser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String documentToImageHTML(Document document, ImageParse imageParser) {
        try {
            Converter mathMLConvert = Converter.getInstance();
            LayoutContextImpl localLayoutContextImpl = new LayoutContextImpl(LayoutContextImpl.getDefaultLayoutContext());
            localLayoutContextImpl.setParameter(Parameter.MATHSIZE, 18);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            mathMLConvert.convert(document, os, "image/png", localLayoutContextImpl);
            String pngName = imageParser.parse(os.toByteArray(), ".png");
            os.close();
            return "<img src=\"" + pngName + "\" align=\"absmiddle\"/>";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
