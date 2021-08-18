package com.example.demo.commonspool2;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * TestObject 工厂类
 */
public class NeedPooledObjectFactory extends BasePooledObjectFactory<NeedPooledObject> {

    /**
     * 激活一个对象，使其可用用
     * @param pooledObject
     */
    @Override
    public void activateObject(PooledObject<NeedPooledObject> pooledObject) {
        pooledObject.getObject().setActive(true);
    }

    /**
     *  创建一个新的对象
     * @return
     * @throws Exception
     */
    @Override
    public NeedPooledObject create() throws Exception {
        return new NeedPooledObject();
    }

    /**
     * 封装为池化对象
     * @param obj
     * @return
     */
    @Override
    public PooledObject<NeedPooledObject> wrap(NeedPooledObject obj) {
        return new DefaultPooledObject<>(new NeedPooledObject());
    }

    /**
     * 销毁对象
     * @param pooledObject
     */
    @Override
    public void destroyObject(PooledObject<NeedPooledObject> pooledObject) {
        pooledObject.getObject().destroy();
    }


    /**
     * 构造一个封装对象
     * @return
     */
    @Override
    public PooledObject<NeedPooledObject> makeObject() {
        return new DefaultPooledObject<>(new NeedPooledObject());
    }

    /**
     *  钝化一个对象,也可以理解为反初始化
     * @param pooledObject
     */
    @Override
    public void passivateObject(PooledObject<NeedPooledObject> pooledObject) {

    }

    /**
     * 验证对象是否可用
     * @param pooledObject
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<NeedPooledObject> pooledObject) {
        return pooledObject.getObject().isActive();
    }
}
