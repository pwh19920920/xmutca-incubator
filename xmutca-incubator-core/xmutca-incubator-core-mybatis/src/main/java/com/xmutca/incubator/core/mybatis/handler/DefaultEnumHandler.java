package com.xmutca.incubator.core.mybatis.handler;

import com.xmutca.incubator.core.common.enums.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * 封装通用的mybatis枚举映射处理器
 * @author Peter
 */
@MappedTypes({BaseEnum.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class DefaultEnumHandler extends BaseTypeHandler<BaseEnum> {

    private Map<String, BaseEnum> enumMap;

    public DefaultEnumHandler(Class<BaseEnum> type) {
        enumMap = new HashMap<>();
        BaseEnum[] constants = type.getEnumConstants();
        if (constants == null) {
            return;
        }

        for (BaseEnum baseEnum:constants) {
            enumMap.put(baseEnum.getCode(), baseEnum);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, BaseEnum stateEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, stateEnum.getCode());
    }

    @Override
    public BaseEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return enumMap.get(resultSet.getString(s));
    }

    @Override
    public BaseEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return enumMap.get(resultSet.getString(i));
    }

    @Override
    public BaseEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String val = callableStatement.getString(i);
        return enumMap.get(val);
    }
}