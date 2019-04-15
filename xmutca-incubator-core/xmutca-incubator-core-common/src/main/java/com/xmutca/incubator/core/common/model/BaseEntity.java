package com.xmutca.incubator.core.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建用户
     */
    @TableField("create_person")
    private Long createPerson;

    /**
     * 更新用户
     */
    @TableField("update_person")
    private Long updatePerson;
}
