package com.chargeingpile.netty.chargeingpilenetty.util;

/**
 * ***************************************************
 *
 * @Auther: zianY
 * @Descipion: TODO
 * @CreateDate: 2020-01-07
 * ****************************************************
 */
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
@EnableAutoConfiguration
public class ApplicationContextUtils implements ApplicationContextAware{
    public static ApplicationContext applicationContext=null;//可写成单利模式，这里为了方便


    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext=arg0;
        System.out.println("设置ApplicationContext成功！");
    }




    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }


}