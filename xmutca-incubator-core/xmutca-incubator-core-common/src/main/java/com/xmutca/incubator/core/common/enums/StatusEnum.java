package com.xmutca.incubator.core.common.enums;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
public enum StatusEnum implements BaseEnum {

    /**
     * 正常状态
     */
    STATUS_NORMAL("00"),

    /**
     * 删除状态
     */
    STATUS_DELETE("01"),

    /**
     * 关闭状态
     */
    STATUS_CLOSE("11");

    /**
     * 代码code
     */
    private String code;

    StatusEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
