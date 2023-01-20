package com.kvitka.deal.jsonEntities.paymentSchedule;

import com.kvitka.deal.jsonEntities.statusHistory.StatusHistory;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentScheduleType implements UserType {

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
            List<PaymentSchedule.PaymentScheduleElement> units = new ArrayList<>();
            JSONObject jsonObject;
            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                units.add(new PaymentSchedule.PaymentScheduleElement(
                        jsonObject.getInt("number"),
                        LocalDate.parse(jsonObject.getString("date")),
                        new BigDecimal(jsonObject.getString("totalPayment")),
                        new BigDecimal(jsonObject.getString("interestPayment")),
                        new BigDecimal(jsonObject.getString("debtPayment")),
                        new BigDecimal(jsonObject.getString("remainingDebt"))));
            }
            return new PaymentSchedule(units);
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
            for (PaymentSchedule.PaymentScheduleElement element : ((PaymentSchedule) value).getAll()) {
                jsonArray.put(new JSONObject()
                        .put("number", element.getNumber())
                        .put("date", element.getDate().toString())
                        .put("totalPayment", element.getTotalPayment().stripTrailingZeros().toPlainString())
                        .put("interestPayment", element.getInterestPayment().stripTrailingZeros().toPlainString())
                        .put("debtPayment", element.getDebtPayment().stripTrailingZeros().toPlainString())
                        .put("remainingDebt", element.getRemainingDebt().stripTrailingZeros().toPlainString()));
            }
            st.setObject(index, jsonArray.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        return new PaymentSchedule((PaymentSchedule) value);
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
