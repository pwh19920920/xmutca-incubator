package com.xmutca.incubator.core.sequence.snowflake;

import com.xmutca.incubator.core.sequence.facade.Generator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
public class SnowflakeGenerator implements Generator {

    private Snowflake sequence;

    public SnowflakeGenerator(SnowflakeGeneratorProperties properties) {
        this.sequence = new Snowflake(properties.getWorkerId(), properties.getDataCenterId());
    }

    @Override
    public Long getUid() {
        return sequence.nextId();
    }

    @Override
    public List<Long> batchGetUid(int batchSize) {
       return LongStream.range(0, batchSize).boxed().map(id -> sequence.nextId()).collect(Collectors.toList());
    }
}
