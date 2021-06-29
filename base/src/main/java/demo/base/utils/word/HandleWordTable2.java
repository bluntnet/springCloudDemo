package demo.base.utils.word;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HandleWordTable2 {
    public static String getAllTable2(String sourceFile) {
        String str = "";
        try {
            XWPFDocument docx = new XWPFDocument(new FileInputStream(sourceFile));
            List<XWPFTable> tables = docx.getTables();
            List<XWPFTableRow> rows;
            List<XWPFTableCell> cells;
            for (XWPFTable table : tables) {
                str += "<table border='1px solid black' cellspacing='0'>\n";
                // 获取表格对应的行
                rows = table.getRows();
                for (int i = 0; i < rows.size(); i++) {
                    System.out.println("row---" + i);
                    str += "<tr>\n";
                    // 获取行对应的单元格
                    cells = rows.get(i).getTableCells();
                    for (int j = 0; j < cells.size(); j++) {
                        System.out.println("cell---" + j);
                        CTVMerge rowspan = cells.get(j).getCTTc().getTcPr().getVMerge();// 跨行
                        //------------------
                        int d = 1;// 跨行行数 //计算是从跨行下一行开始统计的，故加上首行
                        if (rowspan != null) {//rowspan存在未有跨行信息
                            //跨行的第一行为restart，其他行为null
                            STMerge.Enum enum1 = rowspan.getVal();
                            if (enum1 != null && "restart".equals(enum1.toString())) {// 发现跨行信息为第一行时进行计算行数，非跨行首行则绕过不处理，已经计算在内

                                for (int k = i + 1; k < rows.size(); k++) {//从当前单元格的跨行首行开始遍历，避免不必要的计算
                                    if (rows.get(k).getTableCells().size() > j) {
                                        CTVMerge mer = rows.get(k).getTableCells().get(j).getCTTc().getTcPr().getVMerge();
                                        if (mer != null) {//只要不为null就是存在连续的跨行
                                            STMerge.Enum enum2 = mer.getVal();
                                            if (enum2 != null && "restart".equals(enum2.toString())) {//若再次遇到restart，为下一次跨行，终止此次
                                                break;
                                            } else {//只要不是restart就加计算
                                                d++;
                                            }
                                        } else {//连续中断，出现null则为此单元格跨行结束
                                            break;
                                        }
                                    } else {
                                        d++;
                                    }
                                }
                            }
                        }
                        //------------------
                        CTDecimalNumber colspan = cells.get(j).getCTTc().getTcPr().getGridSpan();//跨列
                        if (colspan != null && rowspan != null) {
                            STMerge.Enum enum1 = rowspan.getVal();
                            if (enum1 != null) {
                                str += "<td colspan='" + colspan.getVal() + "' rowspan='" + d + "'>\n";
                            }
                        } else if (colspan == null && rowspan != null) {
                            STMerge.Enum enum1 = rowspan.getVal();
                            if (enum1 != null) {
                                str += "<td rowspan='" + d + "'>\n";
                            }
                        } else if (colspan != null && rowspan == null) {
                            str += "<td colspan='" + colspan.getVal() + "'>";
                        } else if (colspan == null && rowspan == null) {
                            str += "<td>\n";
                        }
                        str += cells.get(j).getText().trim().replaceAll("[\r\n]", "").toLowerCase();
                        str += "</td>\n";
                    }
                    str += "</tr>\n";
                }
                str += "</table>\n";
                System.out.println(str);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void main(String[] args) throws Exception {
        String sourceFile = "D:\\开发文档\\课件讲义\\table_demo.docx";
        String target = "D:\\开发文档\\课件讲义\\parse\\demo_index.html";
        String allTable2 = getAllTable2(sourceFile);
        Files.write(Paths.get(target), allTable2.getBytes());
    }
}
