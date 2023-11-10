package com.deeeelete.system.entity.dto;


import com.deeeelete.excel.annotation.ImportField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户导入实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AclUserImportDTO {


    /**
     * 账号
     */
    @ImportField
    private String account;

    /**
     * 密码
     */
    @ImportField
    private String password;

    /**
     * 姓名
     */
    @ImportField
    private String realName;

    /**
     * 昵称
     */
    @ImportField
    private String nickName;

    /**
     * 手机号
     */
    @ImportField
    private String phone;


    /**
     * 性别 0无 1男 2女
     */
    @ImportField
    private String gender;
}
