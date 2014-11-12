package base.ddd;

import static org.hamcrest.CoreMatchers.*;

import java.math.*;

import javax.persistence.*;

import org.hibernate.annotations.*;
import org.joda.time.*;
import org.junit.*;

import play.test.*;
import base.ddd.ValueObjectValidation.FieldInfo;

public class ValueObjectValidationTest extends UnitTest {
    
    private Name name;
    private final FirstName firstName = new FirstName("Makoto");
    private final LastName lastName = new LastName("Hanafusa");
    
    @Before
    public void before() {
        name = new Name(firstName, lastName);
        assertThat(name, is(not(nullValue())));
    }
    
    //-------------------------------------
    // Equalsテスト
    //-------------------------------------
    
    @Test
    //equalsテスト
    public void testEquals() throws Exception {
        //値がすべて一致する場合は同じと見なすこと
        assertThat(name.equals(new Name(firstName, lastName)), is(true));
        //値がすべてnullでも同一視する
        assertThat(new Name(null, null).equals(new Name(null, null)), is(true));
        
        //値が一部でも異なる場合には同じと見なさないこと
        assertThat(name.equals(new Name(firstName, new LastName("Hanamoge"))),
                   is(false));
        //ignoreCaseも同一視しないこと
        assertThat(name.equals(new Name(firstName, new LastName("hanafusa"))),
                   is(false));
    }
    
    //-------------------------------------
    // Field処理テスト
    //-------------------------------------
    
    @Test
    //Stringプリミティブ・@Column設定なし
    public void testStringFields01() throws Exception {
        final FieldInfo field = new ValidationTestStringVo01("test").fields()
                                                                    .get("value");
        //バリデーションパスして生成されること
        assertThat(field, is(not(nullValue())));
        
        //文字長未指定時は 255 文字となること
        assertThat(field.length(), is(255));
        //nullable未指定時には、null許容となること
        assertThat(field.nullable(), is(true));
    }
    
    @Test
    //Stringプリミティブ・@Column設定あり
    public void testStringFields02() throws Exception {
        final FieldInfo field = new ValidationTestStringVo02("test").fields()
                                                                    .get("value");
        //バリデーションパスして生成されること
        assertThat(field, is(not(nullValue())));
        
        //設定された文字長となること
        assertThat(field.length(), is(4));
        //nullable不許可となること
        assertThat(field.nullable(), is(false));
    }
    
    @Test
    //Integerプリミティブ・@Column設定なし
    public void testIntegerFields01() throws Exception {
        final FieldInfo field = new ValidationTestIntegerVo01(null).fields()
                                                                   .get("value");
        //バリデーションパスして生成されること
        assertThat(field, is(not(nullValue())));
        
        //Columnアノテーション対象フィールドがIntegerの場合、設定された文字長がnullとなること
        assertThat(field.length(), is(nullValue()));
        //nullableとなること
        assertThat(field.nullable(), is(true));
    }
    
    @Test
    //Integerプリミティブ・@Column設定あり
    public void testIntegerFields02() throws Exception {
        final FieldInfo field = new ValidationTestIntegerVo02(123).fields()
                                                                  .get("value");
        //バリデーションパスして生成されること
        assertThat(field, is(not(nullValue())));
        
        //Columnアノテーション対象フィールドがIntegerの場合、設定された文字長がnullとなること
        assertThat(field.length(), is(nullValue()));
        //nullable不許可となること
        assertThat(field.nullable(), is(false));
    }
    
    //-------------------------------------
    // Validation処理テスト
    //-------------------------------------
    
    //---- String型 -------------
    
    //null許容チェック
    @Test
    public void testValidateionNullableString01() throws Exception {
        //例外とならないこと
        new ValidationTestStringVo01(null);
    }
    
    //null許容チェック
    @Test
    public void testValidateionNullableString02() throws Exception {
        //例外とならないこと
        new ValidationTestStringVo01("");
    }
    
    //null許容チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionNullableString03() throws Exception {
        //例外となること
        new ValidationTestStringVo02(null);
    }
    
    //null許容チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionNullableString04() throws Exception {
        //例外となること
        new ValidationTestStringVo02("");
    }
    
    //文字長チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionLength() throws Exception {
        //例外となること
        new ValidationTestStringVo02("aaaaaaaaaaaaa");
    }
    
    //---- Integer型 -------------
    
    //null許容チェック
    @Test
    public void testValidateionNullableInteger01() throws Exception {
        //例外とならないこと
        new ValidationTestIntegerVo01(null);
    }
    
    //null許容チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionNullableInteger02() throws Exception {
        //例外となること
        new ValidationTestIntegerVo02(null);
    }
    
    //---- Long型 -------------
    
    //null許容チェック
    @Test
    public void testValidateionNullableLong01() throws Exception {
        //例外とならないこと
        new ValidationTestLongVo01(null);
    }
    
    //null許容チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionNullableLong02() throws Exception {
        //例外となること
        new ValidationTestLongVo02(null);
    }
    
    //---- BigDecimal型 -------------
    
    //null許容チェック
    @Test
    public void testValidateionNullableBigDecimal01() throws Exception {
        //例外とならないこと
        new ValidationTestBigDecimalVo01(null);
    }
    
    //null許容チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionNullableBigDecimal02() throws Exception {
        //例外となること
        new ValidationTestBigDecimalVo02(null);
    }
    
    //---- DateTime型 -------------
    
    //null許容チェック
    @Test
    public void testValidateionNullableDateTime01() throws Exception {
        //例外とならないこと
        new ValidationTestDateTimeVo01(null);
    }
    
    //null許容チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionNullableDateTime02() throws Exception {
        //例外となること
        new ValidationTestDateTimeVo02(null);
    }
    
    //---- Boolean型 -------------
    
    //null許容チェック
    @Test
    public void testValidateionNullableBoolean01() throws Exception {
        //例外とならないこと
        new ValidationTestBooleanVo01(null);
    }
    
    //null許容チェック
    @Test(expected = IllegalArgumentException.class)
    public void testValidateionNullableBoolean02() throws Exception {
        //例外となること
        new ValidationTestBooleanVo02(null);
    }
    
    //-----------------------------------------
    // Equals テスト用クラス
    //-----------------------------------------
    
    /** Equalsテスト用VO */
    @Embeddable
    static class FirstName extends ValueObject {
        public final String firstName;
        
        public FirstName(final String firstName) {
            this.firstName = firstName;
        }
    }
    
    /** Equalsテスト用VO */
    @Embeddable
    static class LastName extends ValueObject {
        public final String lastName;
        
        public LastName(final String lastName) {
            this.lastName = lastName;
        }
    }
    
    /** Equalsテスト用VO */
    static class Name extends ValueObject {
        @Embedded
        public final FirstName firstName;
        @Embedded
        public final LastName lastName;
        
        public Name(final FirstName firstName, final LastName lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
    
    //-----------------------------------------
    // Validation テスト用クラス
    //-----------------------------------------
    
    //---- String型 --------------------
    
    /** Validationテスト用VO */
    //内部プリミティブがString
    //Columnにパラメータを設定しない
    static class ValidationTestStringVo01 extends ValueObject {
        @Column
        public final String value;
        
        public ValidationTestStringVo01(final String value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    /** Validationテスト用VO */
    //内部プリミティブがString
    //Columnにパラメータを設定する
    static class ValidationTestStringVo02 extends ValueObject {
        @Column(nullable = false, length = 4)
        public final String value;
        
        public ValidationTestStringVo02(final String value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    //---- Integer型 --------------------
    
    /** Validationテスト用VO */
    //内部プリミティブがInteger
    //Columnにパラメータを設定しない
    static class ValidationTestIntegerVo01 extends ValueObject {
        @Column
        public final Integer value;
        
        public ValidationTestIntegerVo01(final Integer value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    /** Validationテスト用VO */
    //内部プリミティブがInteger
    //Columnにパラメータを設定する
    static class ValidationTestIntegerVo02 extends ValueObject {
        @Column(nullable = false)
        public final Integer value;
        
        public ValidationTestIntegerVo02(final Integer value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    //---- Long型 --------------------
    
    /** Validationテスト用VO */
    //内部プリミティブがLong
    //Columnにパラメータを設定しない
    static class ValidationTestLongVo01 extends ValueObject {
        @Column
        public final Long value;
        
        public ValidationTestLongVo01(final Long value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    /** Validationテスト用VO */
    //内部プリミティブがLong
    //Columnにパラメータを設定する
    static class ValidationTestLongVo02 extends ValueObject {
        @Column(nullable = false)
        public final Long value;
        
        public ValidationTestLongVo02(final Long value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    //---- BigDecimal型 --------------------
    
    /** Validationテスト用VO */
    //内部プリミティブがBigDecimal
    //Columnにパラメータを設定しない
    static class ValidationTestBigDecimalVo01 extends ValueObject {
        @Column
        public final BigDecimal value;
        
        public ValidationTestBigDecimalVo01(final BigDecimal value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    /** Validationテスト用VO */
    //内部プリミティブがBigDecimal
    //Columnにパラメータを設定する
    static class ValidationTestBigDecimalVo02 extends ValueObject {
        @Column(nullable = false)
        public final BigDecimal value;
        
        public ValidationTestBigDecimalVo02(final BigDecimal value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    //---- DateTime型 --------------------
    
    /** Validationテスト用VO */
    //内部プリミティブがDateTime
    //Columnにパラメータを設定しない
    static class ValidationTestDateTimeVo01 extends ValueObject {
        @Column
        @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
        public final DateTime value;
        
        public ValidationTestDateTimeVo01(final DateTime value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    /** Validationテスト用VO */
    //内部プリミティブがDateTime
    //Columnにパラメータを設定する
    static class ValidationTestDateTimeVo02 extends ValueObject {
        @Column(nullable = false)
        @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
        public final DateTime value;
        
        public ValidationTestDateTimeVo02(final DateTime value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    //---- Boolean型 --------------------
    
    /** Validationテスト用VO */
    //内部プリミティブがBoolean
    //Columnにパラメータを設定しない
    static class ValidationTestBooleanVo01 extends ValueObject {
        @Column
        public final Boolean value;
        
        public ValidationTestBooleanVo01(final Boolean value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
    
    /** Validationテスト用VO */
    //内部プリミティブがDateTime
    //Columnにパラメータを設定する
    static class ValidationTestBooleanVo02 extends ValueObject {
        @Column(nullable = false)
        public final Boolean value;
        
        public ValidationTestBooleanVo02(final Boolean value) {
            this.value = value;
            
            //バリデーション
            validate();
        }
    }
}
