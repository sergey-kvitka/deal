package com.kvitka.deal.jsonEntities.appliedOffer;

import com.kvitka.deal.jsonEntities.util.UserTypeDefaultMethodImplementations;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AppliedOfferType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Class<AppliedOffer> returnedClass() {
        return AppliedOffer.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session,
                              Object owner) throws HibernateException, SQLException {
        return UserTypeDefaultMethodImplementations.nullSafeGet(rs, names, session, owner, returnedClass());
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        UserTypeDefaultMethodImplementations.nullSafeSet(st, value, index, session);
    }

    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        return UserTypeDefaultMethodImplementations.deepCopy(value);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) this.deepCopy(value);
    }

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return this.deepCopy(cached);
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return this.deepCopy(original);
    }

    @Override
    public boolean equals(final Object obj1, final Object obj2) throws HibernateException {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }

    @Override
    public int hashCode(final Object obj) throws HibernateException {
        return obj.hashCode();
    }
}
