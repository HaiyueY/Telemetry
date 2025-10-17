package com.hit.telemetry_parser.config;

import com.hit.telemetry_parser.utils.VisiableThreadPoolTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yang_Haiyue
 * @author 卫星技术研究所
 * @since 2023/6/25 17:08
 */
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {

	@Value("${async.executor.thread.core_pool_size}")
	private int corePoolSize;
	@Value("${async.executor.thread.max_pool_size}")
	private int maxPoolSize;
	@Value("${async.executor.thread.queue_capacity}")
	private int queueCapacity;
	@Value("${async.executor.thread.name.prefix}")
	private String namePrefix;

	@Bean(name = "asyncServiceExecutor")
	public Executor asyncServiceExecutor() {
		log.info("AsyncServiceExecutor - Starting...");
		//在这里修改
		ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
		//配置核心线程数
		executor.setCorePoolSize(corePoolSize);
		//配置最大线程数
		executor.setMaxPoolSize(maxPoolSize);
		//配置队列大小
		executor.setQueueCapacity(queueCapacity);
		//配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix(namePrefix);

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		//执行初始化
		executor.initialize();
		log.info("AsyncServiceExecutor - Start completed.");
		return executor;
	}

}

