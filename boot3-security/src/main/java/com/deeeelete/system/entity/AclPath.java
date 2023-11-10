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
 * 
 * </p>
 *
 * @author bin.xie
 * @since 2023-05-30
 */
@Getter
@Setter
@TableName("acl_path")
public class AclPath implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "acpa_id", type = IdType.AUTO)
    private Long acpaId;

    /**
     * 路径名
     */
    @TableField("acpa_name")
    private String acpaName;

    /**
     * 上级id
     */
    @TableField("acpa_pid")
    private Long acpaPid;

    /**
     * 路径
     */
    @TableField("acpa_path")
    private String acpaPath;

    /**
     * 排序
     */
    @TableField("acpa_sort")
    private Integer acpaSort;

    /**
     * 类型 0分类 1路径
     */
    @TableField("acpa_type")
    private Integer acpaType;

    /**
     * 备注
     */
    @TableField("acpa_remark")
    private String acpaRemark;

    /**
     * 逻辑删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="UTC")
    @TableField("gmt_modified")
    private Date gmtModified;
}
