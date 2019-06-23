package com.xmutca.incubator.gateway.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmutca.incubator.core.common.constant.RequestConstant;
import com.xmutca.incubator.core.common.exception.ServiceException;
import io.jsonwebtoken.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-22
 */
public class JwtHelper {

    private JwtHelper() {}

    public static final String PARAM_JTI = "jti";
    public static final String PARAM_CLIENT = "client";
    public static final String TOKEN_KEY = RequestConstant.REQUEST_HEADER_TOKEN;
    public static final String TOKEN_TYPE = "bearer";

    /**
     * 获取用户令牌
     * @param clientId 商户id
     * @param subject 令牌主体/用户id
     * @param tokenId 刷新令牌id
     * @param secret 刷新令牌密钥
     * @param localDateTime 颁发日期
     * @param accessExpireTime 过期时间间隔
     * @return
     */
    public static String getToken(String clientId, String subject, String tokenId, String secret, LocalDateTime localDateTime, Date accessExpireTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setId(tokenId)
                .claim(PARAM_CLIENT, clientId)
                .setIssuedAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(accessExpireTime)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取subject
     * @param token
     * @return
     */
    public static JwtData getDataFromJwtToken(String token) {
        try {
            String encodePlayLoad = token.substring(token.indexOf('.') + 1, token.lastIndexOf('.'));
            String decodePlayLoad = new String(Base64Utils.decodeFromString(encodePlayLoad));
            JSONObject data = JSON.parseObject(decodePlayLoad);
            return JwtData.builder()
                    .clientId(data.getString(PARAM_CLIENT))
                    .tokenId(data.getString(PARAM_JTI))
                    .build();
        } catch (Exception ex) {
            return JwtData.builder().build();
        }
    }

    /**
     * 从authorization中取出真正的JwtData
     * @param authorization
     * @return
     */
    public static JwtData getDataFromAuthorization(String authorization) {
        if (StringUtils.isBlank(authorization) || authorization.length() < TOKEN_TYPE.length() + 1) {
            throw new ServiceException("错误的令牌");
        }
        String token = authorization.substring(TOKEN_TYPE.length() + 1);
        return getDataFromJwtToken(token);
    }

    /**
     * 解析jwt处理
     * @param token
     * @param secret
     * @param func
     * @return
     */
    public static Mono parseJwtMonoHandler(String token, String secret, Func func) {
        // 解析信息
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            String subject = claimsJws.getBody().getSubject();
            return func.execute(subject);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            return OauthErrorHelper.INVALID_TOKEN.getMonoResp();
        } catch (ExpiredJwtException expiredEx) {
            return OauthErrorHelper.EXPIRED_TOKEN.getMonoResp();
        }
    }

    /**
     * 解析jwt处理
     * @param token
     * @param secret
     * @param func
     * @return
     */
    public static String parseJwtHandler(String token, String secret) {
        // 解析信息
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return claimsJws.getBody().getSubject();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new ServiceException("错误的令牌");
        } catch (ExpiredJwtException expiredEx) {
            throw new ServiceException("过期的令牌");
        }
    }

    /**
     * 通用接口方法
     */
    public interface Func {

        /**
         * 执行
         * @param subject
         * @return
         */
        Mono execute(String subject);
    }

    @Getter
    @Setter
    @Builder
    public static class JwtData {

        /**
         * 刷新令牌id
         */
        private String tokenId;

        /**
         * 商户id
         */
        private String clientId;
    }
}
