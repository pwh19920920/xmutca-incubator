package com.xmutca.incubator.core.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
@Getter
@Setter
public class BasePaging {

    /**
     * 最大行数限制
     */
    public static final int MAX_LINE = 500;

    /**
     * 排序方式
     */
    public enum SortType {

        /**
         * 降序
         */
        DESC("DESC"),

        /**
         * 升序
         */
        ASC("ASC");

        /**
         * 排序方式
         */
        private String type;

        SortType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 总条数
     */
    private Long total = 0L;

    /**
     * 排序名称
     */
    private String sortName;

    /**
     * 排序方式
     */
    private SortType sortType = SortType.DESC;

    /**
     * 页面大小
     */
    private int size = 10;

    /**
     * 当前页面
     */
    private int current = 1;

    /**
     * 开始的id
     */
    private Long startId;

    /**
     * 起始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 防止size过大问题
     *
     * @param size
     */
    public void setSize(int size) {
        if (size > MAX_LINE) {
            size = MAX_LINE;
        }
        this.size = size;
    }

    @JSONField(serialize = false)
    public String getSortName() {
        return this.sortName;
    }

    @JSONField(serialize = false)
    public String getSortType() {
        return sortType.getType();
    }
}