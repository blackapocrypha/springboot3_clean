package com.deeeelete.system.entity.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.deeeelete.system.entity.convert.GenderConverter;
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
    @ExcelProperty(value = "账号")
    private String account;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码")
    private String password;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    private String realName;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    private String nickName;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phone;


    /**
     * 性别 0无 1男 2女
     */
    @ExcelProperty(value = "性别",converter = GenderConverter.class)
    private Integer gender;
}
