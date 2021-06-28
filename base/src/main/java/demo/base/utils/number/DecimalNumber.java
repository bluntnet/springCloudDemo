package demo.base.utils.number;

public class DecimalNumber extends BaseNumber implements IWordNumber {
    int number = 0;

    public DecimalNumber(String splitChar) {
        super(splitChar);
    }

    @Override
    public String nextNum() {
        number++;
        return number + this.splitChar;
    }
}
