package net.rokyinfo.basecommon.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.ShardedJedisPool;

public class SpringContainer {

    private static SpringContainer instance = null;

    private ApplicationContext ctx;

    private SpringContainer() {

        this.ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static SpringContainer getSpringContainer() {

        if (instance == null) {

            instance = new SpringContainer();
        }

        return instance;
    }

    public ApplicationContext getApplicationContext() {

        return this.ctx;
    }

    /**
     * 获取redis的连接池
     *
     * @return
     */
    public ShardedJedisPool getShardedJedisPool() {

        return (ShardedJedisPool) this.ctx.getBean("shardedJedisPool");
    }

    public Object getBean(String beanName) {

        return this.ctx.getBean(beanName);
    }

    public static void main(String[] args) {

        SpringContainer.getSpringContainer();
    }
}
