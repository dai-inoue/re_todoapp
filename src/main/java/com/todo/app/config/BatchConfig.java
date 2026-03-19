package com.todo.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.todo.app.service.TaskManagementService;

// 最初の設計を定義
@Configuration
public class BatchConfig {

  // 部品の作成(Step)
  @Bean
  public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
      TaskManagementService service) {
    return new StepBuilder("step1", jobRepository).tasklet((contribution, chunkContext) -> {
      System.out.println("バッチ処理を開始しました！");
      int updatedCount = service.markExpiredTasks();
      System.out.println("★期限切れタスクを " + updatedCount + " 件更新しました！");
      return RepeatStatus.FINISHED;
    }, transactionManager).build();
  }

  // 部品の作成(job)
  @Bean
  public Job taskUpdateJob(JobRepository jobRepository, Step step1) {
    return new JobBuilder("taskUpdateJob", jobRepository).start(step1).build();
  }
}
