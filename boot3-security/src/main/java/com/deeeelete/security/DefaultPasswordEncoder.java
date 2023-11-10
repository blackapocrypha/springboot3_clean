package com.deeeelete.security;

import com.deeeelete.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



/**
 * 密码加密器
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {


    public DefaultPasswordEncoder() {
        super();
    }


    // 密码加密
    @Override
    public String encode(CharSequence charSequence) {

        return MD5Util.getSaltMD5(charSequence.toString(),"4771602995648064");
    }

    // 加密密码比较
    @Override
    public boolean matches(CharSequence charSequence, String encodePassword) {
        return MD5Util.getSaltMD5(charSequence.toString(),"4771602995648064").equals(encodePassword);
    }
}
