package com.kvitka.deal.jsonEntities.statusHistory;

import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.ChangeType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class StatusHistoryType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Class<StatusHistory> returnedClass() {
        return StatusHistory.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session,
                              Object owner) throws HibernateException, SQLException {
        final String cellContent = rs.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(cellContent);
            int length = jsonArray.length();
            List<StatusHistory.StatusHistoryUnit> units = new ArrayList<>();
            JSONObject jsonObject;
            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                units.add(new StatusHistory.StatusHistoryUnit(
                        ApplicationStatus.valueOf(jsonObject.getString("status")),
                        ZonedDateTime.parse(jsonObject.getString("time")),
                        ChangeType.valueOf(jsonObject.getString("changeType"))));
            }
            return new StatusHistory(units);

        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (StatusHistory.StatusHistoryUnit unit : ((StatusHistory) value).getAll()) {
                jsonArray.put(new JSONObject()
                        .put("status", unit.getStatus().name())
                        .put("time", unit.getTime().toString())
                        .put("changeType", unit.getChangeType()));
            }
            st.setObject(index, jsonArray.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        return new StatusHistory((StatusHistory) value);
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
