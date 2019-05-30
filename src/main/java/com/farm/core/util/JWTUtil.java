package com.farm.core.util;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.user.User;
import com.farm.core.constant.Constants;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JWTUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);

    @Value("${spring.redis.keyPre}")
    private String profiles;

    /**
     * 解密 jwt
     *
     * @param jwt
     * @return 用户id
     */
    public String parseJWT(String jwt) {
        SecretKey key = generalKey();
        String value = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt).getBody();
            JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());
            value = jsonObject.getString("userId");
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT|Expired|{}", jwt);
        } catch (Exception e) {
            LOGGER.error("JWT|ParseError|{}|{}", jwt, e.getMessage());
        }
        return value;
    }

    /**
     * 获取jwt过期时间
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Date getJWTExpirationTime(String jwt) {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims.getExpiration();
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    private SecretKey generalKey() {
        String stringKey = profiles + Constants.JWT_SECRET;
        LOGGER.info("sign_key:{}", stringKey);
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建 jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
//                .setIssuer("farm")
//                .setAudience("config")
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            LOGGER.debug("#======生成token的时效时间为{}======#", exp);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 生成subject信息
     *
     * @param user
     * @return
     */
    public String generalSubject(User user) {
        JSONObject jo = new JSONObject();
        jo.put("userId", user.getId());
        return jo.toJSONString();
    }
}
