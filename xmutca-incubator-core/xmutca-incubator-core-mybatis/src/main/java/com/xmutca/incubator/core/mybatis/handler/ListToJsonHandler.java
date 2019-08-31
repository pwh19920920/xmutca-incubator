package com.xmutca.incubator.core.mybatis.handler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * 封装通用的mybatis枚举映射处理器
 * @author Peter
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListToJsonHandler extends BaseTypeHandler<List<Object>> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Object> ts, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(ts));
    }

    @Override
    public List<Object> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSON.parseObject(resultSet.getString(s), List.class);
    }

    @Override
    public List<Object> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSON.parseObject(resultSet.getString(i), List.class);
    }

    @Override
    public List<Object> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSON.parseObject(callableStatement.getString(i), List.class);
    }
}