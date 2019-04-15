package com.xmutca.incubator.core.mybatis;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.pagehelper.PageRowBounds;
import com.xmutca.incubator.core.common.response.BasePaging;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Getter
@Setter
public class Paging extends BasePaging {

    /**
     * 分页对象
     */
    private PageRowBounds pageRowBounds;

    @Override
    public Long getTotal() {
        if (null == pageRowBounds || null == pageRowBounds.getTotal()) {
            return super.getTotal();
        }
        return pageRowBounds.getTotal();
    }

    @JSONField(serialize = false)
    public PageRowBounds pgeRowBounds() {
        pageRowBounds = new PageRowBounds(getCurrent(), getSize());
        return pageRowBounds;
    }
}
