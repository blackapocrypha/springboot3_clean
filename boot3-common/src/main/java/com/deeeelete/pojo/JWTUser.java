package com.deeeelete.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTUser {

    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 真实姓名
     */
    private String userRealname;

    /**
     * 盐值
     */
    private String userSalt;

    /**
     * 头像
     */
    private String img;

    /**
     * 性别1男2女
     */
    private Integer userGender;

    /**
     * 权限
     */
    private List<String> auth;

    /**
     * 所拥有的菜单
     */
    private List<String> routes;

    /**
     * 所拥有的按钮
     */
    private List<String> buttons;




}
