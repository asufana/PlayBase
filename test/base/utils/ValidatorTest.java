package base.utils;

import org.junit.*;

import play.test.*;

public class ValidatorTest extends UnitTest {
    
    @Test
    //郵便番号
    public void testZip() {
        final Validator validator = new Validator.Zip();
        mustbeSuccess(validator, "1600022");
        mustbeFailure(validator, "160002");
        mustbeFailure(validator, "16000022");
        mustbeFailure(validator, "160-0022");
    }
    
    //成功想定
    private void mustbeSuccess(final Validator validator, final String value) {
        validator.is(value);
    }
    
    //エラー想定
    private void mustbeFailure(final Validator validator, final String value) {
        try {
            //エラーとなること
            validator.is(value);
            fail();
        }
        catch (final Exception e) {}
    }
}
