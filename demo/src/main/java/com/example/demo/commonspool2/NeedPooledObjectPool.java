package com.example.demo.commonspool2;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 自定义对象池
 */
public class NeedPooledObjectPool extends GenericObjectPool<NeedPooledObject> {
    public NeedPooledObjectPool(PooledObjectFactory<NeedPooledObject> factory) {
        super(factory);
    }

    public NeedPooledObjectPool(PooledObjectFactory<NeedPooledObject> factory, GenericObjectPoolConfig<NeedPooledObject> config) {
        super(factory, config);
    }

    public NeedPooledObjectPool(PooledObjectFactory<NeedPooledObject> factory, GenericObjectPoolConfig<NeedPooledObject> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
