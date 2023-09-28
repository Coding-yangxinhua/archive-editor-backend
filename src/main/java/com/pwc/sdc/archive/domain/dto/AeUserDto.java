package com.pwc.sdc.archive.domain.dto;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pwc.sdc.archive.common.constants.ValidConstant;
import com.pwc.sdc.archive.domain.AeUser;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class AeUserDto implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户账号
     */
    @NotEmpty(message = "账号不能为空")
    @Email(message = "账号需要为邮箱格式", groups = {ValidConstant.User.Register.class})
    private String account;

    /**
     * 用户密码
     */
    @NotEmpty(message = "密码不能为空", groups = {ValidConstant.User.Register.class, ValidConstant.User.Login.class})
    @Length(message = "密码至少要六位", groups = {ValidConstant.User.Register.class})
    private String password;

    /**
     * 用户昵称
     */
    @NotEmpty(message = "昵称不能为空", groups = ValidConstant.User.Register.class)
    private String userName;

    private Date banTime;

    private boolean isBan;

    private Integer point;

    private Long inviter;

    private String invitationCode;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    private int deleted;

    public AeUserDto() {

    }

    public AeUser createEntity() {
        AeUser aeUser = new AeUser();
        aeUser.setId(this.id);
        aeUser.setInviter(this.inviter);
        aeUser.setInvitationCode(this.invitationCode);
        aeUser.setAccount(this.account);
        aeUser.setPassword(this.password);
        aeUser.setUserName(this.userName);
        aeUser.setGmtCreate(this.gmtCreate);
        aeUser.setGmtModified(this.getGmtModified());
        aeUser.setDeleted(this.deleted);
        return aeUser;
    }

    public AeUserDto(AeUser user) {
        this.id = user.getId();
        this.account = user.getAccount();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.gmtCreate = user.getGmtCreate();
        this.point = user.getPoint();
        this.inviter = user.getInviter();
        this.invitationCode = user.getInvitationCode();
        this.banTime = user.getBanTime();
    }

    public boolean isBan() {
        return this.banTime != null;
    }

    @JsonIgnore
    private static final long serialVersionUID = 166646682221916857L;
}