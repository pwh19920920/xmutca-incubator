package com.xmutca.incubator.gateway.filter.protect;

import cn.hutool.crypto.digest.DigestUtil;
import com.xmutca.incubator.gateway.util.ResultUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 地址保护
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-24
 */
@Component
public class ProtectGatewayFilterFactory extends AbstractGatewayFilterFactory<ProtectGatewayFilterFactory.Config> {

    public ProtectGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 获取路径判断
            String requestUri = exchange.getRequest().getPath().value();
            String nonce = exchange.getRequest().getHeaders().getFirst(config.getNonceKey());
            String timestamp = exchange.getRequest().getHeaders().getFirst(config.getTimestampKey());
            String signature = exchange.getRequest().getHeaders().getFirst(config.getSignatureKey());

            // 判断空值
            if (StringUtils.isAnyEmpty(nonce, timestamp, signature)) {
                return ResultUtils.build400Result(exchange, "gateway tip, bad request");
            }

            // 超时判断
            if (checkTimeout(timestamp, config)) {
                return ResultUtils.build400Result(exchange, "gateway tip, request timeout");
            }

            // 签名判断
            String sign = DigestUtil.md5Hex(requestUri + nonce + timestamp);
            if (!sign.equals(signature)) {
                return ResultUtils.build400Result(exchange, "gateway tip, sign error");
            }
            return chain.filter(exchange);
        };
    }

    /**
     * 检查是否超时
     * @param timestampStr
     * @param config
     * @return
     */
    public boolean checkTimeout(String timestampStr, Config config) {
        try {
            long timestamp = Long.parseLong(timestampStr);
            Date timeout = Date.from(LocalDateTime.now().minusSeconds(config.getTimeout()).atZone(ZoneId.systemDefault()).toInstant());
            return timestamp - timeout.getTime() > 0;
        } catch (Exception ex) {
            return true;
        }
    }

    /**
     * 配置对象
     */
    @Getter
    @Setter
    public static class Config {

        public static final String DEFAULT_NONCE_KEY = "X-Ca-Nonce";
        public static final String DEFAULT_TIMESTAMP_KEY = "X-Ca-Timestamp";
        public static final String DEFAULT_SIGNATURE_KEY = "X-Ca-Signature";
        public static final Long DEFAULT_TIMEOUT = 15 * 60L;

        /**
         * 随机key
         */
        private String nonceKey = DEFAULT_NONCE_KEY;

        /**
         * 时间戳key
         */
        private String timestampKey = DEFAULT_TIMESTAMP_KEY;

        /**
         * 签名key
         */
        private String signatureKey = DEFAULT_SIGNATURE_KEY;

        /**
         * 超时时间
         */
        private long timeout;
    }
}
