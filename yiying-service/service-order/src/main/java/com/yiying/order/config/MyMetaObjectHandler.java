package com.yiying.order.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //使用mp实现添加操作
    @Override
    public void insertFill(MetaObject metaObject) {
            this.setFieldValByName("gmtCreate",new Date(),metaObject);
            this.setFieldValByName("gmtModified",new Date(),metaObject);
            this.setFieldValByName("isDelete",0,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }


}
