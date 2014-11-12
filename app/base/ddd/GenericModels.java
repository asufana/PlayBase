package base.ddd;

import javax.persistence.*;

import org.hibernate.annotations.*;
import org.joda.time.*;

import play.db.jpa.*;

/**
 * エンティティ基底クラス
 */
@MappedSuperclass
public abstract class GenericModels extends GenericModel {
    
    protected static final Integer ENABLE = 0;
    protected static final Integer DISABLE = 1;
    
    //サロゲートキー
    @Id
    @GeneratedValue
    protected Long id;
    
    //楽観ロックカラム
    @Version
    private Long version;
    
    //生成日次
    @Column(nullable = false)
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    protected DateTime createDate;
    
    //更新日時
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    protected DateTime modifyDate;
    
    //無効フラグ
    @Column(nullable = false)
    protected int isDisable = ENABLE;
    
    // アクセサ -------------------------------------
    
    public Long id() {
        return id;
    }
    
    @Override
    public Object _key() {
        return id;
    }
    
    public DateTime createDate() {
        return createDate;
    }
    
    public DateTime modifyDate() {
        return modifyDate;
    }
    
    public boolean isDisable() {
        return isDisable == DISABLE;
    }
    
    public void enable() {
        isDisable = ENABLE;
        this.save();
    }
    
    public void disable() {
        isDisable = DISABLE;
        this.save();
    }
    
    //保存時処理
    @PrePersist
    protected void prePersist() {
        createDate = new DateTime();
    }
    
    //更新時処理
    @PreUpdate
    protected void preUpdate() {
        modifyDate = new DateTime();
    }
    
    /** 保存時にエンティティ保存仕様を満たすか確認する */
    @Override
    public <T extends JPABase> T save() {
        isSatisfied();
        return super.save();
    }
    
    /** エンティティが仕様を満たしているかどうか（サブクラス実装） */
    public abstract void isSatisfied();
    
}
