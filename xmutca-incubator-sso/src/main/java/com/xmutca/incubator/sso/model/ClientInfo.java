package com.xmutca.incubator.sso.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Getter
@Setter
@TableName("oauth_client_info")
public class ClientInfo extends BaseEntity {

    /**
     * 客户端id
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 密钥
     */
    private String secret;
}
