package com.xmutca.incubator.core.common.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * 列表问题
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
@Getter
@Setter
public class Results<T> extends Result<Collection<T>> {

    /**
     * 分页对象
     */
    private Paging paging;

    @Getter
    @Setter
    public static class Paging {

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

    public Results(Integer status, String message, Collection<T> data) {
        super(status, message, data);
    }

    public Results(Integer status, String message) {
        super(status, message);
    }

    public Results(String message) {
        super(message);
    }

    public Results() {}

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
