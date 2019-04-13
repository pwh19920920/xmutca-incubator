package com.xmutca.incubator.gateway.core;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-11
 */
@Getter
@Setter
public class Gateway {

    private List<Route> routes = Lists.newArrayList();
}
