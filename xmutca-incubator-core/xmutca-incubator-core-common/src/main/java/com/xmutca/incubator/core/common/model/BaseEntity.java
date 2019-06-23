package com.xmutca.incubator.core.common.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建用户
     */
    private Long createPerson;

    /**
     * 更新用户
     */
    private Long updatePerson;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
