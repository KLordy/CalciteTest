package org.klordy.calcite.factory;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.klordy.calcite.schema.MemorySchema;

import java.util.Map;

/**
 * @Description TODO
 * @Author klordy
 * @Date 2019-09-08 23:30
 */
public class MemorySchemaFactory implements SchemaFactory {
    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        return new MemorySchema(name);
    }
}
