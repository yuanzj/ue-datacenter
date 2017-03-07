package net.rokyinfo.basedao.sql;


import com.alibaba.druid.pool.DruidDataSource;
import net.rokyinfo.basedao.dao.DBMetaDao;
import net.rokyinfo.basedao.entity.DBMeta;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 复合多数据源（Alpha）
 *
 * @author
 */
public class MultiDataSource implements DataSource, ApplicationContextAware {

//    static Logger logger = Logger.getLogger(MultiDataSource.class);

    // 当前线程对应的实际DataSource
    private ThreadLocal<DataSource> currentDataSourceHolder = new ThreadLocal<DataSource>();
    // 使用Key-Value映射的DataSource
    private Map<String, DataSource> mappedDataSources;

    private ApplicationContext context;

    private DBMetaDao dbMetaDao;

    private String driverClass;

    private String user;

    private String password;

    private int minPoolSize;

    private int maxPoolSize;

    private int initialPoolSize;

    private int idleConnectionTestPeriod;

    public MultiDataSource() {

        mappedDataSources = new HashMap<String, DataSource>(4);
    }

    public void init() throws PropertyVetoException {

        if (this.dbMetaDao == null) {

            this.dbMetaDao = (DBMetaDao) context.getBean("dbMetaDao");
        }

        List<DBMeta> allDBs = this.dbMetaDao.getAllDB();

        for (DBMeta dbMeta : allDBs) {

            if ("system".equals(dbMeta.getDbName())) continue;

            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(dbMeta.getDbAddress());
            druidDataSource.setUsername(user);
            druidDataSource.setPassword(password);
            druidDataSource.setInitialSize(initialPoolSize);
            druidDataSource.setMinIdle(minPoolSize);
            druidDataSource.setMaxActive(maxPoolSize);

            mappedDataSources.put(dbMeta.getDbKey(), druidDataSource);
        }
    }

    /**
     * 数据库连接池初始化 该方法通常在web 应用启动时调用
     */
    public void initialMultiDataSource() {

        Collection<DataSource> dsCollection = mappedDataSources.values();
        for (DataSource ds : dsCollection) {
            if (ds != null) {
                Connection conn = null;
                try {
                    conn = ds.getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        conn = null;
                    }
                }
            }
        }
    }

    /**
     * 获取当前线程绑定的DataSource
     *
     * @return
     */
    public DataSource getCurrentDataSource() {
        // 如果路由策略存在，且更新过，则根据路由算法选择新的DataSource
        DataSourceRouter.RouterStrategy strategy = DataSourceRouter.currentRouterStrategy.get();
        if (strategy == null) {
//            throw new IllegalArgumentException(
//                    "DataSource RouterStrategy No found.");
            // 默认的路由策略
            DataSourceRouter.setRouterStrategy(DataSourceRouter.RouterStrategy.SRATEGY_TYPE_MAP,
                    "system", 0);

            strategy = DataSourceRouter.currentRouterStrategy.get();
//        	DataSourceRouter.setRouterStrategy(RouterStrategy.SRATEGY_TYPE_MAP, "system", 0);
//        	strategy = DataSourceRouter.currentRouterStrategy.get();
        }
        if (strategy != null && strategy.isRefresh()) {
            if (DataSourceRouter.RouterStrategy.SRATEGY_TYPE_MAP.equals(strategy.getType())) {
                this.choiceMappedDataSources(strategy.getKey());

            }
            strategy.setRefresh(false);
        }
        return currentDataSourceHolder.get();
    }

    public Map<String, DataSource> getMappedDataSources() {
        return mappedDataSources;
    }

    public void setMappedDataSources(Map<String, DataSource> mappedDataSources) {
        this.mappedDataSources = mappedDataSources;
    }

    /**
     * 使用Key选择当前的数据源
     *
     * @param key
     */
    public void choiceMappedDataSources(String key) {
        DataSource ds = this.mappedDataSources.get(key);
        if (ds == null) {
            throw new IllegalStateException("No Mapped DataSources Exist!");
        }
        this.currentDataSourceHolder.set(ds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sql.DataSource#getConnection()
     */
    public Connection getConnection() throws SQLException {
        DataSource dataSource = getCurrentDataSource();
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sql.DataSource#getConnection(java.lang.String,
     * java.lang.String)
     */
    public Connection getConnection(String username, String password)
            throws SQLException {
        if (getCurrentDataSource() != null) {
            return getCurrentDataSource().getConnection(username, password);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sql.CommonDataSource#getLogWriter()
     */
    public PrintWriter getLogWriter() throws SQLException {
        if (getCurrentDataSource() != null) {
            return getCurrentDataSource().getLogWriter();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sql.CommonDataSource#getLoginTimeout()
     */
    public int getLoginTimeout() throws SQLException {
        if (getCurrentDataSource() != null) {
            return getCurrentDataSource().getLoginTimeout();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter out) throws SQLException {
        if (getCurrentDataSource() != null) {
            getCurrentDataSource().setLogWriter(out);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.sql.CommonDataSource#setLoginTimeout(int)
     */
    public void setLoginTimeout(int seconds) throws SQLException {
        if (getCurrentDataSource() != null) {
            getCurrentDataSource().setLoginTimeout(seconds);
        }
    }

    /*
     * (non-Javadoc) 该接口方法since 1.6 不是所有的DataSource都实现有这个方法
     * 
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {

        // if(getCurrentDataSource() != null){
        // return getCurrentDataSource().isWrapperFor(iface);
        // }
        return false;
    }

    /*
     * (non-Javadoc) 该接口方法since 1.6 不是所有的DataSource都实现有这个方法
     * 
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // if(getCurrentDataSource() != null){
        // return getCurrentDataSource().unwrap(iface);
        // }
        return null;
    }

    public java.util.logging.Logger getParentLogger()
            throws SQLFeatureNotSupportedException {

        return null;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context = applicationContext;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public void setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public int getIdleConnectionTestPeriod() {
        return idleConnectionTestPeriod;
    }

    public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
        this.idleConnectionTestPeriod = idleConnectionTestPeriod;
    }
}
