package com.juliuskrah.quartz;

import org.quartz.SchedulerException;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableConfigurationProperties(QuartzProperties.class)
public class  Application {

    @Autowired
    private QuartzProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
//	@Bean
//	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) throws SchedulerException {
//		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//
//		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//		jobFactory.setApplicationContext(applicationContext);
//		factoryBean.setJobFactory(jobFactory);
//		factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
//		return factoryBean;
//	}

    @Bean
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext,
                                                 DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
//        schedulerFactoryBean.setTaskExecutor(taskExecutor);
        schedulerFactoryBean.setJobFactory(new AutowireCapableBeanJobFactory(applicationContext.getAutowireCapableBeanFactory()));
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactoryBean;
    }

//    @Override
//    @Bean(name = "taskExecutor")
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(6);
//        executor.setQueueCapacity(100);
//        executor.setThreadNamePrefix("email-");
//        return executor;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new SimpleAsyncUncaughtExceptionHandler();
//    }
}
