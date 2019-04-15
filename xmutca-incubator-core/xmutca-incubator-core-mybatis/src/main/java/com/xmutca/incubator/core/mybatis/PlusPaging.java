package com.xmutca.incubator.core.mybatis;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class PlusPaging extends BasePaging {

    /**
     * paging对象
     */
    private Page<?> paging;
}
