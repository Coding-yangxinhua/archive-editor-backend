package com.pwc.sdc.archive.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.sdc.archive.domain.AeUserStatement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import org.apache.ibatis.annotations.Param;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER_STATEMENT(用户流水明细)】的数据库操作Mapper
* @createDate 2023-09-27 15:02:57
* @Entity com.pwc.sdc.archive.domain.AeUserStatement
*/
public interface AeUserStatementMapper extends BaseMapper<AeUserStatement> {

    IPage<AeUserDto> listInvitee(Page<AeUserDto> page, @Param("userId") Long userId);

}




