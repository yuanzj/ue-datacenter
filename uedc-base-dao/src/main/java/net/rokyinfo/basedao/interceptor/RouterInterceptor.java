package net.rokyinfo.basedao.interceptor;


import net.rokyinfo.basedao.dao.UERK600Dao;
import net.rokyinfo.basedao.entity.UERK600;
import net.rokyinfo.basedao.sql.DataSourceRouter;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class RouterInterceptor implements Interceptor, ApplicationContextAware {

    public static ThreadLocal<String> routerThreadLocal = new ThreadLocal<String>();

    private UERK600Dao ueRK600Dao;

    private ApplicationContext context;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (routerThreadLocal.get() == null) {

            return invocation.proceed();
        } else {

            String ueSn = "";

            synchronized (this) {
                ueSn = routerThreadLocal.get();
                routerThreadLocal.remove();
            }

            if (this.ueRK600Dao == null) {

                this.ueRK600Dao = (UERK600Dao) this.context.getBean("ueRK600Dao");
            }

            UERK600 uerk600 = ueRK600Dao.getUERK600BySn(ueSn);

            if (uerk600 != null) {
                String dbKey = uerk600.getDbMeta().getDbKey();

                DataSourceRouter.setRouterStrategy(DataSourceRouter.RouterStrategy.SRATEGY_TYPE_MAP,
                        dbKey, 0);
            }

            Object result = invocation.proceed();

            // 恢复至默认的路由策略
            DataSourceRouter.setDefaultRouterStrategy();

            return result;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {

        this.context = applicationContext;
    }
}
