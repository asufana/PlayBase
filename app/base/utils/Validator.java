package base.utils;

import static org.apache.commons.lang.StringUtils.*;

public abstract class Validator {
    
    /** 評価正規表現 */
    protected abstract String regex();
    
    /** 評価 */
    protected void is(final String value) {
        if (isEmpty(value)) {
            return;
        }
        if (value.matches(regex()) == false) {
            throw new IllegalArgumentException("入力が正しくありません");
        }
    }
    
    /** 郵便番号 */
    public static class Zip extends Validator {
        @Override
        protected String regex() {
            return "^[0-9]{7}$";
        }
    }
}
