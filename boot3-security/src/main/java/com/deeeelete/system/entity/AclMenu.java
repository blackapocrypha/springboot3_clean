package com.deeeelete.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author bin.xie
 * @since 2023-06-24
 */
@Getter
@Setter
@TableName("acl_menu")
public class AclMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "acme_id", type = IdType.AUTO)
    private Long acmeId;

    /**
     * 菜单名称
     */
    @TableField("acme_name")
    private String acmeName;

    /**
     * 父级，0为顶级
     */
    @TableField("acme_pid")
    private Long acmePid;

    /**
     * 代码
     */
    @TableField("acme_code")
    private String acmeCode;

    /**
     * 类型 1菜单 2按钮
     */
    @TableField("acme_type")
    private Integer acmeType;

    /**
     * 排序
     */
    @TableField("acme_sort")
    private Integer acmeSort;

    /**
     * 是否可选Y是N否
     */
    @TableField("acme_select")
    private String acmeSelect;

    /**
     * 状态
     */
    @TableField("acme_status")
    private Integer acmeStatus;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField("gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date gmtModified;

    /**
     * 层级
     */
    @TableField("level")
    private Integer level;
}
