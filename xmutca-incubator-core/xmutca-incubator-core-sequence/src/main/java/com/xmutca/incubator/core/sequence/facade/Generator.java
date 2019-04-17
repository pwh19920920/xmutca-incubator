package com.xmutca.incubator.core.sequence.facade;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
public interface Generator {

    /**
     * 获取下一个id
     * @return
     */
    Long getUid();

    /**
     * 批量获取
     * @param batchSize
     * @return
     */
    List<Long> batchGetUid(int batchSize);
}
