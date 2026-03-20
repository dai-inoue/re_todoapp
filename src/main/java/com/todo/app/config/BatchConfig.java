package com.todo.app.config;

import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
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

  // ------------------↑tasklet------------------↓Chunk----------------------------

  // 1. 読み込み担当 (Reader)
  @Bean
  public ListItemReader<String> itemReader() {
    // 1件だけ、テスト用のTask（荷物）を作って入れる
    return new ListItemReader<>(List.of("テストタスク1", "テストタスク2"));
  }

  // 2. 加工担当 (Stringを受け取ってStringを返す)
  @Bean
  public ItemProcessor<String, String> ItemProcessor() {
    return item -> "加工前" + item;
  }

  // 3. 書き出し担当 (Stringを出力)
  @Bean
  public ItemWriter<String> ItemWriter() {
    return items -> {
      for (String taskName : items) {
        System.out.println("保存対象: " + taskName);
      }
    };
  }

  // 4. Stepの組み立て (型を <String, String> にする)
  @Bean
  public Step myChunkStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager) {
    return new StepBuilder("chunkstep", jobRepository).<String, String>chunk(10, transactionManager)
        .reader(itemReader()).processor(itemProcessor()).writer(itemWriter()).build();
  }

  @Bean
  public Job myJob(JobRepository jobRepository, Step myChunkStep) {
    return new JobBuilder("myJob", jobRepository).start(myChunkStep).build();
  }
}
