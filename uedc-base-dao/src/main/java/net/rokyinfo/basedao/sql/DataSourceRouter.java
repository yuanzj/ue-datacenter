package net.rokyinfo.basedao.sql;

/**
 * @author
 */
public class DataSourceRouter {

    public static ThreadLocal<RouterStrategy> currentRouterStrategy = new ThreadLocal<RouterStrategy>();

    /**
     * 设置MultiDataSource的路由策略
     * 
     * @param type
     * @param key
     * @param routeFactor
     */
    public static void setRouterStrategy(String type, String key,
                                         int routeFactor) {
        if (type == null) {
            throw new IllegalArgumentException(
                    "RouterStrategy Type must not be null");
        }
        RouterStrategy rs = currentRouterStrategy.get();
        if (rs == null) {
            rs = new RouterStrategy();
            currentRouterStrategy.set(rs);
        }
        rs.setType(type);
        rs.setKey(key);
        rs.setRouteFactor(routeFactor);
    }
    
    /**
     * 设置默认的路由策略
     */
    public static void setDefaultRouterStrategy() {
    	
    	// 默认的路由策略
        DataSourceRouter.setRouterStrategy(RouterStrategy.SRATEGY_TYPE_MAP,
                "system", 0);
    }

    /**
     * 数据源路由策略
     * 
     * @author
     */
    public static class RouterStrategy {

        public static final String SRATEGY_TYPE_MAP = "MAP";
        public static final String SRATEGY_TYPE_CLUSTER = "CLUSTER";
        /*
         * 可选值 “MAP” ， “CLUSTER” MAP ： 根据key从DataSourceMap中选中DS CLUSTER :
         * 根据routeFactor参数，通过算法获取群集
         */
        private String type;
        /*
         * “MAP” ROUTE 中的key
         */
        private String key;
        /*
         * "CLUSTER" ROUTE时的参数
         */
        private int routeFactor;
        /*
         * True表示RouterStrategy更新过 False表示没有更新
         */
        private boolean refresh;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            if (this.type != null && !this.type.equals(type)) {
                this.type = type;
                this.refresh = true;
            } else if (this.type == null && type != null) {
                this.type = type;
                this.refresh = true;
            }
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            if (this.key != null && !this.key.equals(key)) {
                this.key = key;
                this.refresh = true;
            } else if (this.key == null && key != null) {
                this.key = key;
                this.refresh = true;
            }
        }

        public int getRouteFactor() {
            return routeFactor;
        }

        public void setRouteFactor(int routeFactor) {
            if (this.routeFactor != routeFactor) {
                this.routeFactor = routeFactor;
                this.refresh = true;
            }
        }

        public boolean isRefresh() {
            return refresh;
        }

        public void setRefresh(boolean refresh) {
            this.refresh = refresh;
        }
    }

}
