package com.pwc.sdc.archive.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pwc.sdc.archive.common.constants.ValidConstant;
import com.pwc.sdc.archive.domain.AeUser;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 基础请求类，包含userId，gameId, platformId
 */
@Data
public class BaseInfoDto implements Serializable {
    Long gameId;

    Long userId;

    Long platformId;
}