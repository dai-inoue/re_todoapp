package com.todo.app;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.DateBuilder.tomorrowAt;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.todo.app.jobs.ExpiredTaskMarkJob;

@SpringBootApplication
public class TodoappApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);
	}

	/***
	 * Quartzのジョブをスケジュール付きで登録する
	 *
	 * @param sfBean SchedulerFactoryBean
	 * @return Scheduler
	 * @throws SchedulerException
	 * @author SS2230
	 */
	@Bean
	public Scheduler schedulerFactoryBean(SchedulerFactoryBean sfBean) throws SchedulerException {
		Scheduler sd = sfBean.getScheduler();
		try {
			JobDetail expiredTaskDeleteJob = newJob(ExpiredTaskMarkJob.class).withIdentity("expiredTaskDeleteJob", "group1").build();
			Trigger expiredTaskDeleteTrigger = newTrigger().withIdentity("trigger1", "group1")
																											.startNow()
																											.startAt(tomorrowAt(0, 0, 30))
																											.withSchedule(dailyAtHourAndMinute(0, 0).inTimeZone(TimeZone.getDefault()))
																											.build();
			sd.scheduleJob(expiredTaskDeleteJob, expiredTaskDeleteTrigger);
			sd.start();
			System.out.println("スケジューリングの初期化に成功しました。");
		} catch (SchedulerException e) {
			System.err.println("スケジューリングの初期化に失敗しました。");
			e.printStackTrace();
			throw e;
		}
		return sd;
	}

}
