package com.todo.app.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpiredTaskMarkJob implements Job {

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private org.springframework.batch.core.Job taskUpdateJob;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      // ここで「BatchConfig」で定義したバッチを起動します！
      JobParameters params =
          new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

      jobLauncher.run(taskUpdateJob, params);

    } catch (Exception e) {
      System.err.println("バッチの起動に失敗しました。");
      e.printStackTrace();
    }
  }
}
