package com.taesan.tikkle.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import jakarta.annotation.PreDestroy;

@Configuration
@EnableScheduling
public class SchedulerConfiguration implements SchedulingConfigurer {

	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.setThreadNamePrefix("scheduler-");
		threadPoolTaskScheduler.initialize();

		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}

	@PreDestroy
	public void preDestroy() {
		if (threadPoolTaskScheduler != null) {
			threadPoolTaskScheduler.shutdown();
		}
	}
}
