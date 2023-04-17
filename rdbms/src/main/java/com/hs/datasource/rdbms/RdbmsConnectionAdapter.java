package com.hs.datasource.rdbms;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.ConnectionAdapter;
import com.hs.datasource.common.MetaDataAdapter;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.common.utils.CloseUtil;

import java.sql.*;

public class RdbmsConnectionAdapter implements ConnectionAdapter {
    private Connection conn;
    private RdbmsDialect dialect;
    private RdbmsDriver driver;

    public RdbmsConnectionAdapter(Connection conn, RdbmsDialect dialect,
                                  RdbmsDriver driver) {
        this.conn = conn;
        this.dialect = dialect;
        this.driver = driver;
    }

    @Override
    public void test() {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(dialect.getValidSql());
            if (!rs.next()) {
                throw CommonException.asException(
                        RdbmsErrorCode.INVALID_SQL_STATEMENT);
            }
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.INVALID_CONNECTION,
                    "测试rdms连接异常.", e);
        } finally {
            CloseUtil.close(rs);
            CloseUtil.close(st);
        }
    }

    @Override
    public MetaDataAdapter getMetaData() {
        try {
            DatabaseMetaData metaData = this.conn.getMetaData();
            return new RdbmsMetaDataAdapter(metaData);
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.INVALID_CONNECTION,
                    "获取rdms元数据异常.", e);
        }
    }

    @Override
    public ArrayNode previewQuery(ObjectNode object) {
        return null;
    }

    @Override
    public void close() {
        CloseUtil.close(this.conn);
    }
}
