package demo.base.utils.word;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.List;

public class HandleWordTable {
    public static String getTableHtml(XWPFTable table) {
        StringBuilder sb = new StringBuilder("<table style=\"border-collapse: collapse;\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\">");
        // 获取表格对应的行
        List<XWPFTableRow> rows = table.getRows();
        for (int i = 0; i < rows.size(); i++) {
            sb.append("<tr>");
            // 获取行对应的单元格
            List<XWPFTableCell> cells = rows.get(i).getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                CTVMerge rowSpan = cells.get(j).getCTTc().getTcPr().getVMerge();//跨行
                //------------------
                int d = 1;//跨行行数 计算是从跨行下一行开始统计的，故加上首行
                if (rowSpan != null) { //rowSpan 存在未有跨行信息
                    //跨行的第一行为restart，其他行为null
                    STMerge.Enum enum1 = rowSpan.getVal();
                    if (enum1 != null && "restart".equals(enum1.toString())) {//发现跨行信息为第一行时进行计算行数，非跨行首行则绕过不处理，已经计算在内
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
                if (colspan != null && rowSpan != null) {
                    STMerge.Enum enum1 = rowSpan.getVal();
                    if (enum1 != null) {
                        sb.append("<td colspan='" + colspan.getVal() + "' rowspan='" + d + "'>");
                    }
                } else if (colspan == null && rowSpan != null) {
                    STMerge.Enum enum1 = rowSpan.getVal();
                    if (enum1 != null) {
                        sb.append("<td rowspan='" + d + "'>");
                    }
                } else if (colspan != null && rowSpan == null) {
                    sb.append("<td colspan='" + colspan.getVal() + "'>");
                } else if (colspan == null && rowSpan == null) {
                    sb.append("<td>\n");
                }
                sb.append(cells.get(j).getText().trim().replaceAll("[\r\n]", "").toLowerCase());
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }
}
