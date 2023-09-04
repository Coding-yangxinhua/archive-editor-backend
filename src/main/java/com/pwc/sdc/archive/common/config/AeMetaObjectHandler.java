package com.pwc.sdc.archive.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;

public class AeMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		Date now = new Date();
		metaObject.setValue("GMT_CREATE", now);
		metaObject.setValue("GMT_MODIFIED", now);
		metaObject.setValue("DELETED", now);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Date now = new Date();
		metaObject.setValue("GMT_MODIFIED", now);
	}

}
