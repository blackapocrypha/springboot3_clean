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
 * 系统日志
 * </p>
 *
 * @author bin.xie
 * @since 2023-10-24
 */
@Getter
@Setter
@TableName("sys_history_logs")
public class SysHistoryLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "slog_id", type = IdType.AUTO)
    private Long slogId;

    /**
     * 登录用户id
     */
    @TableField("slog_login_user_id")
    private Long slogLoginUserId;

    /**
     * 请求者ip
     */
    @TableField("slog_request_ip")
    private String slogRequestIp;

    /**
     * 请求方法
     */
    @TableField("slog_request_method")
    private String slogRequestMethod;

    /**
     * 请求地址
     */
    @TableField("slog_request_url")
    private String slogRequestUrl;

    /**
     * 请求载荷
     */
    @TableField("slog_request_data")
    private String slogRequestData;

    /**
     * 响应数据
     */
    @TableField("slog_response_data")
    private String slogResponseData;

    /**
     * 账号
     */
    @TableField("slog_user_account")
    private String slogUserAccount;

    /**
     * 端源
     */
    @TableField("slog_client")
    private String slogClient;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

}
