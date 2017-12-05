package com.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 切换数据源 通过设置DataSourceKey来切换
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}