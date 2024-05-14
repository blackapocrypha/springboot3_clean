package com.deeeelete.system.entity.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
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
    @ExcelProperty(value = "性别")
    private String gender;

    /**
     * 提示
     */
    @ExcelProperty(value = "提示")
    @ColumnWidth(20)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 13) // 错误信息表头为黄色
    private String tip;
    
}
