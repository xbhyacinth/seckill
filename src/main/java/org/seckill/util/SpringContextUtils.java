package org.seckill.util;

import org.seckill.dao.cache.KillCacheDao;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created with IntelliJ IDEA.
 * User: tang
 * Date: 16-7-27
 * Time: 下午6:06
 * To change this template use File | Settings | File Templates.
 */
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * Spring上下文
     */
    private ApplicationContext applicationContext;

    /**
     * Spring容器初始化bean时调用该方法初始化属性
     */
    public void init() {
        // 注入缓存模板工具中的缓存工具实例
        TKillCacheUtils cacheUtils = (TKillCacheUtils) applicationContext.getBean("jshopCacheUtils");
        KillCacheDao.settKillCacheUtils(cacheUtils);

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}