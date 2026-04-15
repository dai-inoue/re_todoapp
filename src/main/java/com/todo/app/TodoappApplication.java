package com.todo.app;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import com.todo.app.jobs.ExpiredTaskMarkJob;

@SpringBootApplication
@EnableBatchProcessing
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
  @Profile("!test")
  public Scheduler schedulerFactoryBean(SchedulerFactoryBean sfBean) throws SchedulerException {
    Scheduler sd = sfBean.getScheduler();
    try {
      // 1. ジョブの詳細を設定
      JobDetail expiredTaskDeleteJob =
          newJob(ExpiredTaskMarkJob.class).withIdentity("expiredTaskDeleteJob", "group1").build();
      // 2. トリガーを設定（今すぐ実行）
      Trigger expiredTaskDeleteTrigger =
          newTrigger().withIdentity("trigger1", "group1").startNow().build();
      // 3. スケジュールに登録して開始
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
