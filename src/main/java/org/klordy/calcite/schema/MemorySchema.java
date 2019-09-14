package org.klordy.calcite.schema;

import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author klordy
 * @Date 2019-09-08 23:57
 */
public class MemorySchema extends AbstractSchema {
    private String dbName;

    public MemorySchema(String dbName){
        this.dbName = dbName;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        Map<String, Table> tables = new HashMap<String, Table>();
        MemoryData.Database database = MemoryData.MAP.get(this.dbName);
        if(database == null)
            return tables;
        for(MemoryData.Table table : database.tables) {
            tables.put(table.tableName, new MemoryTable(table));
        }
        return tables;
    }
}
