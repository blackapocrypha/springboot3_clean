package com.deeeelete.system.entity.query;

import com.deeeelete.system.entity.enums.EnableEnum;
import com.deeeelete.utils.StringUtil;
import com.deeeelete.pojo.QueryParam;
import com.deeeelete.system.entity.dto.AclUserDTO;
import com.deeeelete.system.entity.AclUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;

/**
* <p>
    * 用户 查询类
    * </p>
*
* @author bin.xie
* @since 2023-05-30
*/
@Data
public class AclUserQuery extends QueryParam {

    /**
    * 原类
    */
    private AclUserDTO param;

    private QueryWrapper<AclUser> query;
    private UpdateWrapper<AclUser> update;

    /**
    * 开始时间
    */
    private Date startTime;

    /**
    * 结束时间
    */
    private Date endTime;

    // likeParam


    // 账号
    private String accountIsLike; 

    // 姓名
    private String realNameIsLike; 

    // 昵称
    private String nickNameIsLike; 

    // 手机号
    private String phoneIsLike; 

    // 盐
    private String saltIsLike; 

    // 头像
    private String userImageIsLike; 


    public QueryWrapper<AclUser> buildExample() {
        query = new QueryWrapper<>();
        update = new UpdateWrapper<>();

        if(getParam() == null){
            setParam(new AclUserDTO());
        }

        // id
        if(StringUtil.isNotEmpty(getParam().getId())){
            query.eq("id",getParam().getId());
            update.eq("id",getParam().getId());
        }

        // 账号
        if(StringUtil.isNotEmpty(getParam().getAccount())){
            query.eq("account",getParam().getAccount());
            update.eq("account",getParam().getAccount());
        }
        if(StringUtil.isNotEmpty(getAccountIsLike())){
            query.like("account",getAccountIsLike());
            update.like("account",getAccountIsLike());
        }

        // 密码
        if(StringUtil.isNotEmpty(getParam().getPassword())){
            query.eq("password",getParam().getPassword());
            update.eq("password",getParam().getPassword());
        }


        // 姓名
        if(StringUtil.isNotEmpty(getParam().getRealName())){
            query.eq("real_name",getParam().getRealName());
            update.eq("real_name",getParam().getRealName());
        }
        if(StringUtil.isNotEmpty(getRealNameIsLike())){
            query.like("real_name",getRealNameIsLike());
            update.like("real_name",getRealNameIsLike());
        }

        // 昵称
        if(StringUtil.isNotEmpty(getParam().getNickName())){
            query.eq("nick_name",getParam().getNickName());
            update.eq("nick_name",getParam().getNickName());
        }
        if(StringUtil.isNotEmpty(getNickNameIsLike())){
            query.like("nick_name",getNickNameIsLike());
            update.like("nick_name",getNickNameIsLike());
        }

        // 手机号
        if(StringUtil.isNotEmpty(getParam().getPhone())){
            query.eq("phone",getParam().getPhone());
            update.eq("phone",getParam().getPhone());
        }
        if(StringUtil.isNotEmpty(getPhoneIsLike())){
            query.like("phone",getPhoneIsLike());
            update.like("phone",getPhoneIsLike());
        }

        // 盐
        if(StringUtil.isNotEmpty(getParam().getSalt())){
            query.eq("salt",getParam().getSalt());
            update.eq("salt",getParam().getSalt());
        }
        if(StringUtil.isNotEmpty(getSaltIsLike())){
            query.like("salt",getSaltIsLike());
            update.like("salt",getSaltIsLike());
        }

        // 头像
        if(StringUtil.isNotEmpty(getParam().getUserImage())){
            query.eq("user_image",getParam().getUserImage());
            update.eq("user_image",getParam().getUserImage());
        }
        if(StringUtil.isNotEmpty(getUserImageIsLike())){
            query.like("user_image",getUserImageIsLike());
            update.like("user_image",getUserImageIsLike());
        }

        // 性别 0无 1男 2女
        if(StringUtil.isNotEmpty(getParam().getGender())){
            query.eq("gender",getParam().getGender());
            update.eq("gender",getParam().getGender());
        }

        // 逻辑删除 0未删除 1已删除
        if(StringUtil.isNotEmpty(getParam().getIsDeleted())){
            query.eq("is_deleted",getParam().getIsDeleted());
            update.eq("is_deleted",getParam().getIsDeleted());
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
    public QueryWrapper<AclUser> getQueryP() {
        return query;
    }

}