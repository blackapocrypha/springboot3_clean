package com.deeeelete.system.entity.dto;


import com.deeeelete.excel.annotation.ExportField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户导出实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AclUserExportDTO {


    /**
     * 账号
     */
    @ExportField(columnName = "账号")
    private String account;

    /**
     * 密码
     */
    @ExportField(columnName = "密码")
    private String password;

    /**
     * 姓名
     */
    @ExportField(columnName = "姓名")
    private String realName;

    /**
     * 昵称
     */
    @ExportField(columnName = "昵称")
    private String nickName;

    /**
     * 手机号
     */
    @ExportField(columnName = "手机号")
    private String phone;


    /**
     * 性别 0无 1男 2女
     */
    @ExportField(columnName = "性别")
    private String gender;

    /**
     * 提示
     */
    @ExportField(columnName = "提示")
    private String tip;

    /**
     * 错误行数
     */
    @ExportField(columnName = "错误行数")
    private Integer oldRow;
}
