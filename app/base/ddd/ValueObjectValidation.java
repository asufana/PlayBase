package base.ddd;

import static org.apache.commons.lang.StringUtils.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.math.*;
import java.util.*;

import org.joda.time.*;

/** 値オブジェクト評価 */
public class ValueObjectValidation {
    
    private final Object targetValueObject;
    
    /** コンストラクタ */
    ValueObjectValidation(final Object targetValueObject) {
        this.targetValueObject = targetValueObject;
    }
    
    /** Columnアノテーション内容でバリデーション */
    void validate() {
        try {
            fields().validate();
        }
        catch (final IllegalArgumentException iae) {
            throw new IllegalArgumentException(iae.getMessage());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /** (staticを除く）ローカルフィールド一覧 */
    FieldInfoCollection fields() {
        final List<FieldInfo> fields = new ArrayList<FieldInfo>();
        for (final Field field : targetValueObject.getClass()
                                                  .getDeclaredFields()) {
            //staticフィールドは除外
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            fields.add(new FieldInfo(field, targetValueObject));
        }
        return new FieldInfoCollection(fields);
    }
    
    //-----------------------------------------
    // バリデーション用内部クラス
    //-----------------------------------------
    
    /** Fieldラッパークラス */
    static class FieldInfo {
        //フィールド定義
        private final Field field;
        //実インスタンス
        private final Object object;
        //フィールドの文字長制約（制約設定がなければnull）
        private final Integer length;
        //フィールドがnull許容かどうか
        private final boolean nullable;
        
        //インスタンス
        public FieldInfo(final Field field, final Object object) {
            this.field = field;
            this.object = object;
            length = length(field);
            nullable = nullable(field);
            
            //内部型種別の確認
            isSupported(field);
        }
        
        /** 内部クラス種別の確認 */
        public void isSupported(final Field field) {
            final Class clazz = field().getType();
            //指定のプリミティブ型以外は対応しない
            if (!clazz.equals(String.class)
                    && !clazz.equals(Integer.class)
                    && !clazz.equals(Long.class)
                    && !clazz.equals(BigDecimal.class)
                    && !clazz.equals(DateTime.class)
                    && !clazz.equals(Boolean.class)) {
                throw new RuntimeException("指定の型には対応していません：" + clazz.getName());
            }
        }
        
        /** バリデーション */
        public void validate() throws IllegalArgumentException, IllegalAccessException {
            final Object o = field().get(object());
            //null許容確認
            if (nullable == false) {
                if (o == null) {
                    throw new IllegalArgumentException(String.format("入力してください。",
                                                                     object().getClass()
                                                                             .getName()));
                }
                //String型の場合、空文字は不可
                if (length != null && isEmpty((String) o)) {
                    throw new IllegalArgumentException(String.format("入力してください。",
                                                                     object().getClass()
                                                                             .getName()));
                }
            }
            //文字長確認
            if (o != null && length != null) {
                final String value = (String) o;
                if (value.length() > length) {
                    throw new IllegalArgumentException(String.format("文字長が超過しています。",
                                                                     o.getClass()
                                                                      .getName(),
                                                                     value));
                }
            }
        }
        
        /** フィールド */
        public Field field() {
            return field;
        }
        
        /** 実インスタンス */
        public Object object() {
            return object;
        }
        
        /** フィールド名 */
        public String name() {
            return field().getName();
        }
        
        /** 最大文字長 */
        public Integer length() {
            return length;
        }
        
        /** NULL許容可否 */
        public boolean nullable() {
            return nullable;
        }
        
        /** Columnアノテーションのlength値 */
        private Integer length(final Field field) {
            //String以外は文字長チェックしない
            if (field.getType().equals(String.class) == false) {
                return null;
            }
            for (final Annotation a : field.getDeclaredAnnotations()) {
                if (a instanceof javax.persistence.Column) {
                    return ((javax.persistence.Column) a).length();
                }
            }
            return null;
        }
        
        /** Columnアノテーションのnullable値 */
        private boolean nullable(final Field field) {
            for (final Annotation a : field.getDeclaredAnnotations()) {
                if (a instanceof javax.persistence.Column) {
                    return ((javax.persistence.Column) a).nullable();
                }
            }
            return true;
        }
        
    }
    
    /** Fieldクラスコレクション */
    static class FieldInfoCollection {
        private List<FieldInfo> fields;
        
        //コンストラクタ
        public FieldInfoCollection(final List<FieldInfo> fields) {
            if (fields == null || fields.size() == 0) {
                this.fields = Collections.emptyList();
            }
            this.fields = Collections.unmodifiableList(fields);
        }
        
        /** バリデーション */
        public void validate() throws IllegalArgumentException, IllegalAccessException {
            for (final FieldInfo f : fields) {
                f.validate();
            }
        }
        
        /** フィールドの取得 */
        public FieldInfo get(final String fieldName) {
            for (final FieldInfo f : fields) {
                if (f.name().toLowerCase().equals(fieldName.toLowerCase())) {
                    return f;
                }
            }
            return null;
        }
    }
}
