package com.kvitka.deal.postgres;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

public class PostgresDialect extends PostgreSQL94Dialect {
    public PostgresDialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}
