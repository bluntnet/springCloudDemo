package demo.base.utils.number;

public class WordNumberFactory {
    public static IWordNumber getWordNumber(String numFmt, String numLevelText) {
        if ("decimal".equals(numFmt)) {
            return new DecimalNumber(numLevelText);
        } else if ("upperLetter".equals(numFmt)) {
            return new UpperLetterNumber(numLevelText);
        } else if ("japaneseCounting".equals(numFmt)) {
            return new JapaneseCountingNumber(numLevelText);
        }
        return null;
    }
}
