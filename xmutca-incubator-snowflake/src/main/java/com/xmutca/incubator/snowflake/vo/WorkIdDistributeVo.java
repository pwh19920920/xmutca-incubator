package com.xmutca.incubator.snowflake.vo;

import com.xmutca.incubator.snowflake.common.Constant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 分配workId
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020-04-15
 */
@Getter
@Setter
public class WorkIdDistributeVo {

    /**
     * 数据中心
     */
    @NotNull(message = "数据中心ID不能为空")
    @Min(value = Constant.ZERO, message = "数据中心ID不能小于0")
    @Max(value = Constant.MAX_DATA_CENTER_ID, message = "数据中心ID不能大于" + Constant.MAX_DATA_CENTER_ID)
    private Integer dataCenterId;

    /**
     * 服务名称
     */
    @NotEmpty(message = "服务名称不能为空")
    private String serviceName;

    /**
     * 业务分组
     */
    @NotEmpty(message = "业务分组不能为空")
    private String group;

    /**
     * host地址
     */
    @NotEmpty(message = "host不能为空")
    private String host;

    /**
     * 端口
     */
    @NotEmpty(message = "端口不能为空")
    private String port;
}
