package com.hs.datasource.rdbms;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.MetaDataAdapter;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.common.utils.CloseUtil;
import com.hs.datasource.common.utils.JsonUtil;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RdbmsMetaDataAdapter implements MetaDataAdapter {
    private DatabaseMetaData metaData;

    public RdbmsMetaDataAdapter(DatabaseMetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public ArrayNode get(String type) {
        switch (type) {
            case RdbmsConst.CATALOG:
                return getCatalogs();
            case RdbmsConst.SCHEMA:
                return getSchemas();
            case RdbmsConst.TABLE:
                return getTables();
            case RdbmsConst.COLUMN:
                return getColumns();
            case RdbmsConst.PRIMARY:
                return getPrimaries();
            default:
                throw CommonException.asException(
                        RdbmsErrorCode.UNSUPPORTED_META_TYPE);
        }
    }

    @Override
    public ArrayNode get(String type, ObjectNode meta) {
        switch (type) {
            case RdbmsConst.CATALOG:
                return getCatalogs(meta);
            case RdbmsConst.SCHEMA:
                return getSchemas(meta);
            case RdbmsConst.TABLE:
                return getTables(meta);
            case RdbmsConst.COLUMN:
                return getColumns(meta);
            case RdbmsConst.PRIMARY:
                return getPrimaries(meta);
            default:
                throw CommonException.asException(
                        RdbmsErrorCode.UNSUPPORTED_META_TYPE);
        }
    }

    public ArrayNode getCatalogs() {
        ArrayNode nodes = JsonUtil.createArrayNode();
        ResultSet rs = null;
        try {
            rs = this.metaData.getCatalogs();
            while (rs.next()) {
                ObjectNode node = JsonUtil.createObjectNode();
                String tableCat = rs.getString("TABLE_CAT");
                JsonUtil.putNotNull(node, RdbmsKey.CATALOG, tableCat);
                nodes.add(node);
            }
            return nodes;
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.METADATA_EXCEPTION,
                    "获取[catalog]元数据异常.", e);
        } finally {
            CloseUtil.close(rs);
        }
    }

    public ArrayNode getCatalogs(ObjectNode meta) {
        String catalog = JsonUtil.getString(meta, RdbmsKey.CATALOG);
        ArrayNode nodes = JsonUtil.createArrayNode();
        if (catalog != null) {
            ObjectNode node = JsonUtil.createObjectNode();
            JsonUtil.putNotNull(node, RdbmsKey.CATALOG, catalog);
            nodes.add(node);
        }
        return nodes;
    }

    public ArrayNode getSchemas() {
        return getSchemas(null);
    }

    public ArrayNode getSchemas(ObjectNode meta) {
        String catalog = JsonUtil.getString(meta, RdbmsKey.CATALOG);
        String schema = JsonUtil.getString(meta, RdbmsKey.SCHEMA);
        ArrayNode nodes = JsonUtil.createArrayNode();
        ResultSet rs = null;
        try {
            rs = this.metaData.getSchemas(catalog, schema);
            while (rs.next()) {
                ObjectNode node = JsonUtil.createObjectNode();
                String tableCat = rs.getString("TABLE_CATALOG");
                JsonUtil.putNotNull(node, RdbmsKey.CATALOG, tableCat);
                String tableSch = rs.getString("TABLE_SCHEM");
                JsonUtil.putNotNull(node, RdbmsKey.SCHEMA, tableSch);
                nodes.add(node);
            }
            return nodes;
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.METADATA_EXCEPTION,
                    "获取[schema]元数据异常.", e);
        } finally {
            CloseUtil.close(rs);
        }
    }

    public ArrayNode getTables() {
        return getTables(null);
    }

    public ArrayNode getTables(ObjectNode meta) {
        String catalog = JsonUtil.getString(meta, RdbmsKey.CATALOG);
        String schema = JsonUtil.getString(meta, RdbmsKey.SCHEMA);
        String tableName = JsonUtil.getString(meta, RdbmsKey.TABLE_NAME);

        ResultSet rs = null;
        ArrayNode nodes = JsonUtil.createArrayNode();
        try {
            rs = this.metaData.getTables(catalog, schema,
                    tableName, new String[]{RdbmsConst.TABLE.toUpperCase()});
            while (rs.next()) {
                ObjectNode node = JsonUtil.createObjectNode();
                String tableCat = rs.getString("TABLE_CAT");
                JsonUtil.putNotNull(node, RdbmsKey.CATALOG, tableCat);
                String tableSch = rs.getString("TABLE_SCHEM");
                JsonUtil.putNotNull(node, RdbmsKey.SCHEMA, tableSch);
                String tableNam = rs.getString("TABLE_NAME");
                node.put(RdbmsKey.TABLE_NAME, tableNam);
                String remarks = rs.getString("REMARKS");
                node.put(RdbmsKey.COMMENT, remarks);
                nodes.add(node);
            }
            return nodes;
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.METADATA_EXCEPTION,
                    "获取[table]元数据异常.", e);
        } finally {
            CloseUtil.close(rs);
        }
    }

    public ArrayNode getColumns() {
        return getColumns(null);
    }

    public ArrayNode getColumns(ObjectNode object) {
        String catalog = JsonUtil.getString(object, RdbmsKey.CATALOG);
        String schema = JsonUtil.getString(object, RdbmsKey.SCHEMA);
        String table = JsonUtil.getString(object, RdbmsKey.TABLE_NAME);
        ResultSet rs = null;
        ArrayNode nodes = JsonUtil.createArrayNode();
        try {
            rs = this.metaData.getColumns(catalog, schema,
                    table, null);
            while (rs.next()) {
                ObjectNode node = JsonUtil.createObjectNode();
                String tableCat = rs.getString("TABLE_CAT");
                JsonUtil.putNotNull(node, RdbmsKey.CATALOG, tableCat);
                String tableSch = rs.getString("TABLE_SCHEM");
                JsonUtil.putNotNull(node, RdbmsKey.SCHEMA, tableSch);
                String tableName = rs.getString("TABLE_NAME");
                JsonUtil.putNotNull(node, RdbmsKey.TABLE_NAME, tableName);
                String columnName = rs.getString("COLUMN_NAME");
                node.put(RdbmsKey.COLUMN_NAME, columnName);
                int dataType = rs.getInt("DATA_TYPE");
                node.put(RdbmsKey.DATA_TYPE, dataType);
                String typeName = rs.getString("TYPE_NAME");
                node.put(RdbmsKey.TYPE_NAME, typeName);
                int precision = rs.getInt("COLUMN_SIZE");
                node.put(RdbmsKey.PRECISION, precision);
                int scale = rs.getInt("DECIMAL_DIGITS");
                node.put(RdbmsKey.SCALE, scale);
                int nullable = rs.getInt("NULLABLE");
                if (nullable == 0) {
                    node.put(RdbmsKey.NULLABLE, false);
                } else {
                    node.put(RdbmsKey.NULLABLE, true);
                }
                String comment = rs.getString("REMARKS");
                node.put(RdbmsKey.COMMENT, comment);
                int length = rs.getInt("CHAR_OCTET_LENGTH");
                node.put(RdbmsKey.LENGTH, length);
                int sort = rs.getInt("ORDINAL_POSITION");
                node.put(RdbmsKey.SORT, sort);
                nodes.add(node);
            }
            return nodes;
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.METADATA_EXCEPTION,
                    "获取[column]元数据异常.", e);
        } finally {
            CloseUtil.close(rs);
        }
    }


    public ArrayNode getPrimaries() {
        ArrayNode tableNodes = getTables(null);
        ArrayNode nodes = JsonUtil.createArrayNode();
        tableNodes.forEach(node -> {
            nodes.addAll(getPrimariesByTable((ObjectNode) node));
        });
        return nodes;
    }

    public ArrayNode getPrimaries(ObjectNode object) {
        String table = JsonUtil.getString(object, RdbmsKey.TABLE_NAME);
        if (table != null) {
            return getPrimariesByTable(object);
        }
        ArrayNode tableNodes = getTables(object);
        ArrayNode nodes = JsonUtil.createArrayNode();
        tableNodes.forEach(node -> {
            nodes.addAll(getPrimariesByTable((ObjectNode) node));
        });
        return nodes;
    }

    public ArrayNode getPrimariesByTable(ObjectNode object) {
        String catalog = JsonUtil.getString(object, RdbmsKey.CATALOG);
        String schema = JsonUtil.getString(object, RdbmsKey.SCHEMA);
        String table = JsonUtil.getString(object, RdbmsKey.TABLE_NAME);
        ResultSet rs = null;
        ArrayNode nodes = JsonUtil.createArrayNode();
        try {
            rs = this.metaData.getPrimaryKeys(catalog, schema, table);
            while (rs.next()) {
                ObjectNode node = JsonUtil.createObjectNode();
                String tableCat = rs.getString("TABLE_CAT");
                JsonUtil.putNotNull(node, RdbmsKey.CATALOG, tableCat);
                String tableSch = rs.getString("TABLE_SCHEM");
                JsonUtil.putNotNull(node, RdbmsKey.SCHEMA, tableSch);
                String tableName = rs.getString("TABLE_NAME");
                JsonUtil.putNotNull(node, RdbmsKey.TABLE_NAME, tableName);
                String columnName = rs.getString("COLUMN_NAME");
                node.put(RdbmsKey.COLUMN_NAME, columnName);
                int keySeq = rs.getInt("KEY_SEQ");
                node.put(RdbmsKey.KEY_SEQ, keySeq);
                String pkName = rs.getString("PK_NAME");
                node.put(RdbmsKey.PK_NAME, pkName);
                nodes.add(node);
            }
            return nodes;
        } catch (SQLException e) {
            throw CommonException.asException(
                    RdbmsErrorCode.METADATA_EXCEPTION,
                    "获取主键[column]元数据异常.", e);
        } finally {
            CloseUtil.close(rs);
        }
    }

}
