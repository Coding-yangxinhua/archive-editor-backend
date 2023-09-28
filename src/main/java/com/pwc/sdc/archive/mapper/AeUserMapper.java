package com.pwc.sdc.archive.mapper;

import com.pwc.sdc.archive.domain.AeUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Xinhua X Yang
* @description 针对表【AE_USER(用户信息表)】的数据库操作Mapper
* @createDate 2023-09-04 15:41:58
* @Entity com.pwc.sdc.archive.domain.AeUser
*/
public interface AeUserMapper extends BaseMapper<AeUser> {

    AeUser getUserByAccount(@Param("account") String account);

    AeUser getUserByInvitationCode(@Param("invitationCode") String invitationCode);


    boolean changePoint(@Param("userId") Long userId, @Param("point") Integer point);

    void changePointBatch(@Param("list") List<AeUserDto> list);
}




