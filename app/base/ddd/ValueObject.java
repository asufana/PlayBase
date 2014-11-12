package base.ddd;

import java.io.*;

import org.apache.commons.lang3.builder.*;

import base.ddd.ValueObjectValidation.FieldInfoCollection;

/** DDD値オブジェクト抽象基底クラス */
public abstract class ValueObject implements Serializable {
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        //すべてのフィールドが同一かどうか
        return EqualsBuilder.reflectionEquals(this, o);
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    /** Columnアノテーション内容でバリデーション */
    protected void validate() {
        new ValueObjectValidation(this).validate();
    }
    
    /** 値オブジェクト内のフィールド一覧 */
    protected FieldInfoCollection fields() {
        return new ValueObjectValidation(this).fields();
    }
    
}
