package com.deeeelete.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 白名单
 * </p>
 *
 * @author bin.xie
 * @since 2024-01-25
 */
@Getter
@Setter
@TableName("acl_white_list")
public class AclWhiteList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "acwl_id", type = IdType.AUTO)
    private Long acwlId;

    /**
     * 接口地址
     */
    @TableField("acwl_path")
    private String acwlPath;

    /**
     * 强制锁(1不允许删除 0允许)
     */
    @TableField("acwl_lock")
    private Integer acwlLock;
}
