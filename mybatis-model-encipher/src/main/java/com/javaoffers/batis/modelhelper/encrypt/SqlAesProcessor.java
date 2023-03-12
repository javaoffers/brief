package com.javaoffers.batis.modelhelper.encrypt;

import com.javaoffers.batis.modelhelper.encrypt.parser.ColNameProcessorInfo;
import com.javaoffers.batis.modelhelper.encrypt.parser.ConditionName;
import com.javaoffers.batis.modelhelper.encrypt.parser.SqlParserProcessor;
import com.javaoffers.thrid.jsqlparser.schema.Column;
import com.javaoffers.thrid.jsqlparser.schema.Table;


import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * SQL field encryption processor. Who is either Aes128 using the encryption algorithm.
 * The class by {@code AesEncryptConfigBeanRegistrar} parsing is registered as a bean
 *
 * @author mingJie
 */

public class SqlAesProcessor {

    //Encrypted into hexadecimal, encryption prefix;
    private static final String ENCRYPT_PREFIX = "HEX(AES_ENCRYPT(";
    //Encryption suffix
    private static final String ENCRYPT_SUFFIX = "))";
    //Decryption prefix
    private static final String DECRYPT_PREFIX = "AES_DECRYPT(UNHEX(";
    //Decryption suffix
    private static final String DECRYPT_SUFFIX = ")";
    //Aes private key. When the real to fill in hexadecimal (32);
    private String key;
    //The SQL parser
    private SqlParserProcessor sqlParserProcessor;
    //The field processing;
    private Consumer<ColNameProcessorInfo> processor = colNameProcessorInfo -> {
        Column column = colNameProcessorInfo.getColumn();
        ConditionName conditionName = colNameProcessorInfo.getConditionName();
        String columnName = column.getColumnName();
        //select / whereon
        if (ConditionName.SELECT_COL_NAME == conditionName || ConditionName.isWhereOnName(conditionName)) {
            if (column.getTable() != null) {
                column.setColumnName(DECRYPT_PREFIX + column.getTable().getName() + "." + columnName + ")," + "'" + key + "'" + DECRYPT_SUFFIX);
                //This table should be set to null, because above tableName has been filled into the columnName;
                column.setTable(new Table());
            } else {
                column.setColumnName(DECRYPT_PREFIX + columnName + ")," + "'" + key + "'" + DECRYPT_SUFFIX);
            }
        }
        // insert values/value.
        else if (ConditionName.VALUES == conditionName) {
            column.setColumnName(ENCRYPT_PREFIX + columnName + ", '" + key + "'" + ENCRYPT_SUFFIX);
        }
        //update set.
        else if (ConditionName.UPDATE_SET == conditionName) {
            column.setColumnName(ENCRYPT_PREFIX + columnName + ", '" + key + "'" + ENCRYPT_SUFFIX);
        }
    };

    /**
     * Parse SQL
     *
     * @param sql Raw SQL
     * @return The parsed SQL
     */
    public String parseSql(String sql) throws Exception {
        String tempSql = sql.toLowerCase();
        Set<String> needParseTables = sqlParserProcessor.getNeedParseTables();
        for (String tableName : needParseTables) {
            if (tempSql.contains(tableName.toLowerCase())) {
                return sqlParserProcessor.parseSql(sql);
            }
        }
        return sql;
    }

    public SqlAesProcessor(String key) {

        this.key = key;
        SqlParserProcessor.SqlParserProcessorBuild builder = SqlParserProcessor.builder();
        List<EncryptConfigContext.TableColumns> tableColums = EncryptConfigContext.getTableColumsByKey(key);
        for (EncryptConfigContext.TableColumns tableColumns : tableColums) {
            String tableName = tableColumns.getTableName();
            Set<String> columns = tableColumns.getColumns();
            builder.addProcessor(tableName, processor);
            for (String column : columns) {
                builder.addColName(tableName, column);
            }
        }
        this.sqlParserProcessor = builder.build();
    }

}
