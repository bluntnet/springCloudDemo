package demo.base.utils.number;

public class JapaneseCountingNumber extends BaseNumber implements IWordNumber {
    String[] baseWord = {"十", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    String dot = "、";
    int count = 0;

    public JapaneseCountingNumber(String splitChar) {
        super(splitChar);
    }

    @Override
    public String nextNum() {
        count++;
        int unit = 0;
        if (count > 99) {
            throw new RuntimeException("仅支持100以下的自动编码");
        }
        if (count > 19) {
            int ten = count / 10;
            unit = count % 10;
            if (unit == 0) {
                return baseWord[ten] + "十" + dot;
            } else {
                return baseWord[ten] + "十" + baseWord[unit] + dot;
            }
        }
        if (count > 10) {
            unit = count % 10;
            return "十" + baseWord[unit] + dot;
        }
        unit = count % 10;
        return baseWord[unit] + dot;
    }
}
