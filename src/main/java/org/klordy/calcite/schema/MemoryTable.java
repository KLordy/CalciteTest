package org.klordy.calcite.schema;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 数据库表实现类，主要封装table对应的元数据，必须继承 AbstractTable 并实现 ScannableTable，
 * AbstractTable主要是要实现getRowType，获取到表每一列的类型，不继承实现该方法中间解析时会因为获取到null而空指针异常。
 * ScannableTable不实现的话，中间解析获取对应schema方法的地方会无法选择到正确的用于查询的schema解析类，而导致parseSql的时候报错。
 * @Author klordy
 * @Date 2019-09-08 23:59
 */
public class MemoryTable extends AbstractTable implements ScannableTable {
    private MemoryData.Table sourceTable;
    private RelDataType dataType;

    public MemoryTable(MemoryData.Table table) {
        this.sourceTable = table;
        dataType = null;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        if(dataType == null){
            RelDataTypeFactory.FieldInfoBuilder fieldInfo = typeFactory.builder();
            for (MemoryData.Column column : this.sourceTable.columns) {
                RelDataType sqlType = typeFactory.createJavaType(
                        MemoryData.JAVATYPE_MAPPING.get(column.type));
                sqlType = SqlTypeUtil.addCharsetAndCollation(sqlType, typeFactory);
                fieldInfo.add(column.name, sqlType);
            }
            dataType = typeFactory.createStructType(fieldInfo);
        }
        return dataType;
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        final List<String> types = new ArrayList<String>(sourceTable.columns.size());
        for(MemoryData.Column column : sourceTable.columns) {
            types.add(column.type);
        }
        final int[] fields = identityList(this.dataType.getFieldCount());
        return new AbstractEnumerable<Object[]>() {
            public Enumerator<Object[]> enumerator() {
                return new MemoryEnumerator<Object[]>(fields, types, sourceTable.data);
            }
        };
    }

    private static int[] identityList(int n) {
        int[] integers = new int[n];
        for (int i = 0; i < n; i++) {
            integers[i] = i;
        }
        return integers;
    }
}
