package base.ddd;

import javax.persistence.*;

import org.apache.commons.lang3.builder.*;
import org.hibernate.proxy.*;

/** DDDエンティティ抽象基底クラス */
@MappedSuperclass
public abstract class Entity<T extends GenericModels> extends GenericModels {
    
    /** エンティティが同一かどうか */
    public boolean sameIdentityAs(final T other) {
        if (id() == null) {
            return false;
        }
        return other != null && id().equals(other.id());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null) {
            return false;
        }
        
        if (o.getClass() != this.getClass()) {
            //参考：http://stackoverflow.com/questions/11013138/hibernate-equals-and-proxy
            if (getClassObj(o).isAssignableFrom(getClassObj(this)) == false
                    && getClassObj(this).isAssignableFrom(getClassObj(o)) == false) {
                return false;
            }
        }
        final T other = (T) o;
        return sameIdentityAs(other);
    }
    
    private Class getClassObj(final Object obj) {
        //HibernateによりJavassistで修正された元エンティティのクラス名取得
        //参考：http://stackoverflow.com/questions/1139611/loading-javassist-ed-hibernate-entity
        return obj instanceof HibernateProxy
                ? HibernateProxyHelper.getClassWithoutInitializingProxy(obj)
                : obj.getClass();
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    public String toStringAll() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
