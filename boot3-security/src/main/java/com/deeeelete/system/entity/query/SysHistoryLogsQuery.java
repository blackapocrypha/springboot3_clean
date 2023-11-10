package com.deeeelete.system.entity.query;

import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.SysHistoryLogsDTO;
import com.deeeelete.system.entity.SysHistoryLogs;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    * 系统日志 查询类
    * </p>
*
* @author bin.xie
* @since 2023-10-24
*/
@Data
public class SysHistoryLogsQuery extends QueryParam {
    /**
    * 原类
    */
    private SysHistoryLogsDTO param;

    private QueryWrapper<SysHistoryLogs> query;
    private UpdateWrapper<SysHistoryLogs> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    // likeParam




    // 请求者ip
    private String slogRequestIpIsLike; 

    // 请求方法
    private String slogRequestMethodIsLike; 

    // 请求地址
    private String slogRequestUrlIsLike; 

    // 请求载荷
    private String slogRequestDataIsLike; 

    // 响应数据
    private String slogResponseDataIsLike; 

    // 账号
    private String slogUserAccountIsLike;

    // 端源
    private String slogClientIsLike;




    public QueryWrapper<SysHistoryLogs> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new SysHistoryLogsDTO());
        }


        // id
        if(StringUtil.isNotEmpty(getParam().getSlogId())){
            query.eq("slog_id",getParam().getSlogId());
            update.eq("slog_id",getParam().getSlogId());
        }

        // 登录用户id
        if(StringUtil.isNotEmpty(getParam().getSlogLoginUserId())){
            query.eq("slog_login_user_id",getParam().getSlogLoginUserId());
            update.eq("slog_login_user_id",getParam().getSlogLoginUserId());
        }

        // 请求者ip
        if(StringUtil.isNotEmpty(getParam().getSlogRequestIp())){
            query.eq("slog_request_ip",getParam().getSlogRequestIp());
            update.eq("slog_request_ip",getParam().getSlogRequestIp());
        }
        if(StringUtil.isNotEmpty(getSlogRequestIpIsLike())){
            query.like("slog_request_ip",getSlogRequestIpIsLike());
            update.like("slog_request_ip",getSlogRequestIpIsLike());
        }

        // 请求方法
        if(StringUtil.isNotEmpty(getParam().getSlogRequestMethod())){
            query.eq("slog_request_method",getParam().getSlogRequestMethod());
            update.eq("slog_request_method",getParam().getSlogRequestMethod());
        }
        if(StringUtil.isNotEmpty(getSlogRequestMethodIsLike())){
            query.like("slog_request_method",getSlogRequestMethodIsLike());
            update.like("slog_request_method",getSlogRequestMethodIsLike());
        }

        // 端源
        if(StringUtil.isNotEmpty(getParam().getSlogClient())){
            query.eq("slog_client",getParam().getSlogClient());
            update.eq("slog_client",getParam().getSlogClient());
        }
        if(StringUtil.isNotEmpty(getSlogClientIsLike())){
            query.like("slog_client",getSlogClientIsLike());
            update.like("slog_client",getSlogClientIsLike());
        }

        // 请求地址
        if(StringUtil.isNotEmpty(getParam().getSlogRequestUrl())){
            query.eq("slog_request_url",getParam().getSlogRequestUrl());
            update.eq("slog_request_url",getParam().getSlogRequestUrl());
        }
        if(StringUtil.isNotEmpty(getSlogRequestUrlIsLike())){
            query.like("slog_request_url",getSlogRequestUrlIsLike());
            update.like("slog_request_url",getSlogRequestUrlIsLike());
        }

        // 请求载荷
        if(StringUtil.isNotEmpty(getParam().getSlogRequestData())){
            query.eq("slog_request_data",getParam().getSlogRequestData());
            update.eq("slog_request_data",getParam().getSlogRequestData());
        }
        if(StringUtil.isNotEmpty(getSlogRequestDataIsLike())){
            query.like("slog_request_data",getSlogRequestDataIsLike());
            update.like("slog_request_data",getSlogRequestDataIsLike());
        }

        // 响应数据
        if(StringUtil.isNotEmpty(getParam().getSlogResponseData())){
            query.eq("slog_response_data",getParam().getSlogResponseData());
            update.eq("slog_response_data",getParam().getSlogResponseData());
        }
        if(StringUtil.isNotEmpty(getSlogResponseDataIsLike())){
            query.like("slog_response_data",getSlogResponseDataIsLike());
            update.like("slog_response_data",getSlogResponseDataIsLike());
        }

        // 账号
        if(StringUtil.isNotEmpty(getParam().getSlogUserAccount())){
            query.eq("slog_user_account",getParam().getSlogUserAccount());
            update.eq("slog_user_account",getParam().getSlogUserAccount());
        }
        if(StringUtil.isNotEmpty(getSlogUserAccountIsLike())){
            query.like("slog_user_account",getSlogUserAccountIsLike());
            update.like("slog_user_account",getSlogUserAccountIsLike());
        }

        // 创建时间
        if(StringUtil.isNotEmpty(getParam().getGmtCreate())){
            query.eq("gmt_create",getParam().getGmtCreate());
            update.eq("gmt_create",getParam().getGmtCreate());
        }

        // 更新时间
        if(StringUtil.isNotEmpty(getParam().getGmtModified())){
            query.eq("gmt_modified",getParam().getGmtModified());
            update.eq("gmt_modified",getParam().getGmtModified());
        }

    //        // 开始时间
    //        if(StringUtil.isNotEmpty(getStartTime())){
    //            query.ge("create_time",getStartTime());
    //            update.ge("create_time",getStartTime());
    //        }
    //        // 结束时间
    //        if(StringUtil.isNotEmpty(getEndTime())){
    //            query.le("create_time",getEndTime());
    //            update.le("create_time",getEndTime());
    //        }

        // 排序
        if(StringUtil.isNotEmpty(getOrderItem())){
            if(StringUtil.isEmpty(getOrderType())){
                query.orderByAsc(Arrays.asList(getOrderItem().split(",")));
            }else if("asc".equals((getOrderType()))){
                query.orderByAsc(Arrays.asList(getOrderItem().split(",")));
            }else if("desc".equals((getOrderType()))){
                query.orderByDesc(Arrays.asList(getOrderItem().split(",")));
            }
        }

        return query;
    }



    @Override
    public QueryWrapper<SysHistoryLogs> getQueryP() {
        return query;
    }

}