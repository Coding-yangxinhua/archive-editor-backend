package com.pwc.sdc.archive.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.sdc.archive.common.constants.ValidConstant;
import com.pwc.sdc.archive.domain.AeUser;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Data
public class AeUserDto implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户账号
     */
    @NotEmpty(message = "Account 不能为空")
    private String account;

    /**
     * 用户密码
     */
    @NotEmpty(message = "Password 不能为空")
    private String password;

    /**
     * 用户昵称
     */
    @NotEmpty(message = "昵称不能为空", groups = ValidConstant.User.Register.class)
    private String userName;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    private int deleted;

    public AeUser createEntity() {
        AeUser aeUser = new AeUser();
        aeUser.setId(this.id);
        aeUser.setAccount(this.account);
        aeUser.setPassword(this.password);
        aeUser.setUserName(this.userName);
        aeUser.setGmtCreate(this.gmtCreate);
        aeUser.setGmtModified(this.getGmtModified());
        aeUser.setDeleted(this.deleted);
        return aeUser;
    }

}