package com.hs.datasource.plugin.oracle;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.ConnectionAdapter;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.plugin.rdbms.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDataSource extends RdbmsDataSource {
    private ObjectNode config;
    private OracleDialect dialect = new OracleDialect();
    private OracleDriver driver = new OracleDriver();

    @Override
    public ObjectNode getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(ObjectNode config) {
        this.config = config;
    }

    @Override
    public ConnectionAdapter getConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                    driver.getUrl(config),
                    driver.getProperties(config));
            return new RdbmsConnectionAdapter(conn, dialect, driver);
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.INVALID_CONNECTION,
                    "获取Mysql连接异常.", e);
        }
    }

    @Override
    public RdbmsDialect getDialect() {
        return dialect;
    }

    @Override
    public RdbmsDriver getDriver() {
        return driver;
    }
}
