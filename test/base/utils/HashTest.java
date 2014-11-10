package base.utils;

import static org.hamcrest.CoreMatchers.*;

import org.junit.*;

import play.test.*;
import base.utils.Hash.HashAlgorithm;

public class HashTest extends UnitTest {
    
    @Test
    public void testGenerate() {
        final String hashedStr = Hash.generate("test", HashAlgorithm.MD5);
        assertThat(hashedStr, is(not(nullValue()))); //ハッシュ値生成されること
        System.out.println(hashedStr);
    }
    
}
