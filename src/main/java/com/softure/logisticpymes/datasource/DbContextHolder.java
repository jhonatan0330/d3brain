package com.softure.logisticpymes.datasource;

public class DbContextHolder {
	
	private static final ThreadLocal<DbType> CONTEXT_HOLDER = new ThreadLocal<DbType>() {

        @Override
        protected DbType initialValue() {
            return DbType.MASTER;
        }
    };


    public static void setDataSourceType(DbType type) {
        CONTEXT_HOLDER.set(type);
    }

    public static DbType getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    public static void resetDataSourceType() {
        CONTEXT_HOLDER.set(DbType.MASTER);
    }
    
}
