package com.pwc.sdc.archive.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pwc.sdc.archive.common.enums.EnableStatus;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class AeMetaObjectHandler implements MetaObjectHandler { ;

	@Override
	public void insertFill(MetaObject metaObject) {
		Date now = new Date();
		metaObject.setValue("gmtCreate", now);
		metaObject.setValue("gmtModified", now);
		metaObject.setValue("deleted", EnableStatus.DISABLE.value());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Date now = new Date();
		metaObject.setValue("gmtModified", now);
	}

}
