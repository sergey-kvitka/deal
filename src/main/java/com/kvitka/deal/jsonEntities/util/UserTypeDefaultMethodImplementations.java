package com.kvitka.deal.jsonEntities.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class UserTypeDefaultMethodImplementations {
    public static Object deepCopy(Object value) throws HibernateException {
        try { // * use serialization to create a deep copy
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            bos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            return new ObjectInputStream(bais).readObject();
        } catch (ClassNotFoundException | IOException ex) {
            throw new HibernateException(ex);
        }
    }

    public static Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session,
                                     Object owner, Class<?> returnedClass) throws HibernateException, SQLException {
        final String cellContent = rs.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cellContent.getBytes(StandardCharsets.UTF_8), returnedClass);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
        }
    }

    public static void nullSafeSet(PreparedStatement st, Object value, int index,
                                   SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final StringWriter w = new StringWriter();
            mapper.writeValue(w, value);
            w.flush();
            st.setObject(index, w.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
        }
    }
}
