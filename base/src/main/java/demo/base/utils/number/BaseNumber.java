package demo.base.utils.number;

import java.math.BigInteger;

public class BaseNumber {
    BigInteger numberId;
    String fmt;
    String splitChar = ".";
    public BaseNumber(String splitChar) {
        if (splitChar.startsWith("%1")) {
            this.splitChar = splitChar.replace("%1", "");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        BaseNumber bn = (BaseNumber) obj;
        if (bn.numberId == null || this.numberId == null) return false;
        if (bn.numberId == this.numberId) {
            return true;
        }
        return false;
    }
}
