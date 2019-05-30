package com.farm.core.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.farm.base.user.User;
import com.farm.core.constant.Constants;
import com.farm.core.user.DdUser;
import com.farm.core.user.LoginVO;
import com.farm.core.user.exception.UserException;
import com.farm.core.user.mapper.DdUserMapper;
import com.farm.core.user.mapper.UserMapper;
import com.farm.core.user.service.UserLoginService;
import com.farm.core.util.JWTUtil;
import com.farm.core.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 ** @Date: 2019-04-20 23:45
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);


    private UserMapper userMapper;
    private JWTUtil jwtUtil;
    private RedisUtil redisUtil;
    private DdUserMapper ddUserMapper;

    @Autowired
    @SuppressWarnings("all")
    public UserLoginServiceImpl(UserMapper userMapper, JWTUtil jwtUtil, RedisUtil redisUtil, DdUserMapper ddUserMapper) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.ddUserMapper = ddUserMapper;
    }

    @Override
    public String loginByPassword(LoginVO loginParams) throws UserException {
        if (StringUtils.isBlank(loginParams.getUsername()) || StringUtils.isBlank(loginParams.getPassword())) {
            LOGGER.error("用户名或密码为空");
            throw new UserException(UserException.LOGIN_COMMON_ERROR);

        }
        //TODO 校验验证码是否正确
        //暂时不做 编码处理
//        String username = new String(Base64CoderUtil.decode(loginParams.getUsername()));
//        String pass = new String(Base64CoderUtil.decode(loginParams.getUsername()));

        String username = loginParams.getUsername();
        List<User> userList = this.userMapper.findByMobileNumber(username);
        if (CollectionUtils.isEmpty(userList)) {
            LOGGER.error("用户名不存在");
            throw new UserException(UserException.LOGIN_USWENAME_ERROE);
        }
        User user = userList.get(0);
        String subject = jwtUtil.generalSubject(user);

        checkPassword(user, loginParams.getPassword());

        String token = jwtUtil.createJWT(Constants.JWT_ID, subject, Constants.WEB_JWT_TTL);
        //保存token
        redisUtil.setForTimeMS(user.getId(), token, Constants.WEB_JWT_TTL);


        return StringUtils.isNotBlank(token) ? token : "";
    }

    @Override
    public String ddLogin(OapiUserGetResponse oapiUserGetResponse) throws UserException {

        String ddUserId = oapiUserGetResponse.getUserid();
        String email = oapiUserGetResponse.getEmail();
        String mobile = oapiUserGetResponse.getMobile();
        String name = oapiUserGetResponse.getName();
        String avatar = oapiUserGetResponse.getAvatar();


        String s = JSONObject.toJSONString(oapiUserGetResponse);


        LOGGER.info("钉钉userId :{} ,mobile:{},s;{}", ddUserId, mobile, s);

        User ddUser = this.userMapper.findById(ddUserId);

        if (ddUser == null) {
            //第一次登入
            User user = new User();
            user.setId(ddUserId);
            user.setEmail(email);
            user.setSource("dd");
            user.setPhoto(avatar);
            user.setRealName(name);
            user.setRegisterTime(new Date());
            this.userMapper.insert(user);
            LOGGER.info("保存dd用户信息成功");
        }

        ddUser = this.userMapper.findById(ddUserId);

        String subject = jwtUtil.generalSubject(ddUser);
        //获取token
        String token = jwtUtil.createJWT(Constants.JWT_ID, subject, Constants.WEB_JWT_TTL);

        //保存token
        redisUtil.setForTimeMS(ddUser.getId(), token, Constants.WEB_JWT_TTL);
        return StringUtils.isNotBlank(token) ? token : "";
    }

    /**
     * 生成token
     *
     * @param source
     * @param user
     * @return
     */
    public String createToken(String source, User user) {
        String token;
        String subject = jwtUtil.generalSubject(user);
        token = jwtUtil.createJWT(Constants.JWT_ID, subject, Constants.WEB_JWT_TTL);
        //保存token
        return token;
    }


    //    解析token
    private String parseToken(String token) {
        return jwtUtil.parseJWT(token);
    }

    /**
     * 检查用户密码是否正确
     *
     * @param user 用户
     * @param pass 密码
     */
    private void checkPassword(User user, String pass) throws UserException {
        if (!user.getPassword().equals(pass)) {
            LOGGER.error("用户密码错误");
            throw new UserException(UserException.LOGIN_COMMON_ERROR);
        }
    }


}
