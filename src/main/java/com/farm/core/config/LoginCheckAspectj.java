package com.farm.core.config;


import com.farm.base.BaseException;
import com.farm.base.user.User;
import com.farm.core.user.exception.UserException;
import com.farm.core.user.mapper.UserMapper;
import com.farm.core.util.JWTUtil;
import com.farm.core.util.RedisUtil;
import com.farm.core.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 登录检查，检查通过后，将在TreadLocal中放入从数据库中取出的User对象
 */
@Aspect
@Order(2)
@Component
public class LoginCheckAspectj {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginCheckAspectj.class);
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Autowired
    @SuppressWarnings("all")
    public LoginCheckAspectj(UserMapper userMapper, JWTUtil jwtUtil, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    /**
     * 1.获取request信息
     * 2.根据request 的header获得用户登录凭证信息token
     * 3.解析token取出登录用户信息
     */
    @Around(value = "@annotation(com.farm.core.config.LoginCheck)")
    public Object check(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 从token中获取用户信息
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            throw new UserException(BaseException.ERROR_CODE_MISSING_TOKEN);
        }
        String userId = jwtUtil.parseJWT(token);
        if (StringUtils.isBlank(userId)) {
            throw new UserException(BaseException.ERROR_CODE_MISSING_TOKEN);
        }

        //根据用户id 获取redis中的token
        Object tokenObj = redisUtil.get(userId);
        if(tokenObj!=null){
            if(!StringUtils.equals(token,tokenObj.toString())){
                //校验不通
                LOGGER.warn("无效的token", userId);
                throw new UserException(UserException.LOGIN_COMMON_ERROR);
            }
        }
        final User user = userMapper.findById(userId);
        if (user == null) {
            LOGGER.warn("当前用户未找到[{}]", userId);
            throw new UserException(UserException.LOGIN_USWENAME_ERROE);
        }
        UserUtils.setUser(user);
        try {
            return pjp.proceed();
        } finally {
            UserUtils.setUser(null);
        }
    }
}
