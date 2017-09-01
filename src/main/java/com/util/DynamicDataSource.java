package com.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 切换数据源 通过设置DataSourceKey来切换
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();

    public static void setDataSourceKey(DynamicDataSourceGlobal dataSource) {
        dataSourceKey.set(dataSource.toString());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}