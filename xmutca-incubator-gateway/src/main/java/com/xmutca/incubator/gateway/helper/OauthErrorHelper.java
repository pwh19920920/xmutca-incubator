package com.xmutca.incubator.gateway.helper;

import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.gateway.dto.TokenResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

/**
 * 授权错误描述
 *
 * @author pwh
 */
public enum OauthErrorHelper {

    /**
     * 非法请求
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "invalid_request", "非法请求"),

    /**
     * 非法授权
     */
    INVALID_GRANT(HttpStatus.BAD_REQUEST, "invalid_grant", "非法授权"),

    /**
     * 不支持的授权类型
     */
    UNSUPPORTED_GRANT_TYPE(HttpStatus.BAD_REQUEST, "unsupported_grant_type", "不支持的授权类型"),

    /**
     * 令牌过期
     */
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "expired_token", "令牌过期"),

    /**
     * 非法令牌
     */
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "invalid_token", "非法令牌"),

    /**
     * 用户名或者密码错误
     */
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "invalid_user", "用户名或者密码错误"),

    /**
     * 非法客户端或者令牌
     */
    INVALID_CLIENT(HttpStatus.UNAUTHORIZED, "invalid_client", "非法客户端或者令牌");

    /**
     * 状态
     */
    HttpStatus status;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误秒数
     */
    private String msg;

    OauthErrorHelper(HttpStatus status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public ResponseEntity getResp() {
        return new ResponseEntity(new Result(this.status.value(), this.msg, new TokenResponseDto(this.code, this.msg)), this.status);
    }

    public ResponseEntity getResp(String msg) {
        return new ResponseEntity(new Result<>(this.status.value(), msg, new TokenResponseDto(this.code, msg)), this.status);
    }

    public Mono<ResponseEntity> getMonoResp() {
        return Mono.just(getResp());
    }

    public Mono<ResponseEntity> getMonoResp(String msg) {
        return Mono.just(getResp(msg));
    }
}