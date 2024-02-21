package com.deeeelete.config;


import com.deeeelete.filter.JwtRequestFilter;
import com.deeeelete.system.util.JWTSecurityUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * 权限配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("all")
public class TokenWebSecurityConfig   {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JWTSecurityUtil jwtSecurityUtil;


    @Bean
    public JwtRequestFilter authenticationJwtTokenFilter() {
        return new JwtRequestFilter(redisTemplate,jwtSecurityUtil);
    }

    /**
     * 认证管理器，登录的时候参数会传给 authenticationManager
     *
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        //关闭csrf
        http.csrf().disable()
                // 允许跨域（也可以不允许，看具体需求）
                .cors().and()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 配置路径是否需要认证
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .requestMatchers("/**")
                .permitAll()
                // 配置权限
//                .requestMatchers("/asd/**")
//                .hasAuthority("admin")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                .and()
                .authenticationManager(authenticationManager(authenticationConfiguration))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //此处为添加jwt过滤
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        http.headers().frameOptions().disable();
        return http.build();

    }

    /**
     *  跨域资源配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        //修改为添加而不是设置
         configuration.addAllowedMethod("*");
        //这里很重要，起码需要允许 Access-Control-Allow-Origin
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600 * 24L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    /**
     * 程序启动之后首先刷新一波接口权限到缓存
     */
    @PostConstruct
    public void initPath(){
        jwtSecurityUtil.initPath();
    }
}
