package com.kvitka.deal.jsonEntities.passport;

import com.kvitka.deal.jsonEntities.util.UserTypeDefaultMethodImplementations;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Objects;

public class PassportType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Class<Passport> returnedClass() {
        return Passport.class;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor session,
                              Object owner) throws HibernateException, SQLException {
        final String cellContent = resultSet.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {
            JSONObject jsonPassport = new JSONObject(cellContent);
            return new Passport(
                    jsonPassport.getString("series"),
                    jsonPassport.getString("number"),
                    jsonPassport.getString("issueBranch"),
                    LocalDate.parse(jsonPassport.getString("issueDate")));
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, Object value, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            ps.setNull(index, Types.OTHER);
            return;
        }
        try {
            JSONObject jsonPassport = new JSONObject();
            Passport passport = (Passport) value;
            jsonPassport.put("series", passport.getSeries());
            jsonPassport.put("number", passport.getNumber());
            jsonPassport.put("issueBranch", passport.getIssueBranch());
            jsonPassport.put("issueDate", passport.getIssueDate().toString());
            ps.setObject(index, jsonPassport.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
        }
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
        return Objects.equals(obj1, obj2);
    }

    @Override
    public int hashCode(final Object obj) throws HibernateException {
        return obj.hashCode();
    }
}
