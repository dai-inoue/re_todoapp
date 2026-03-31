package com.todo.app.config;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import com.todo.app.TodoRepository;
import com.todo.app.entity.Todo;

// 最初の設計を定義
@Configuration
public class BatchConfig {

  // 部品の作成(Step)
  @Bean
  public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("step1", jobRepository).tasklet((contribution, chunkContext) -> {

      // 👈 ここに「一括でやりたいこと」を全部書く！
      System.out.println("データを一括で更新したよ！");

      return RepeatStatus.FINISHED; // 「お仕事終わり！」の合図
    }, transactionManager).build();
  }
  // ------------------------------tasklet↑---------------------------------------------------

  // 1. 読み込み担当 (Reader )
  @Bean
  public RepositoryItemReader<Todo> reader(TodoRepository repository) {
    // 1件だけ、テスト用のTask（荷物）を作って入れる
    return new RepositoryItemReaderBuilder<Todo>().name("todoReader").repository(repository)
        .methodName("findAll").pageSize(10).sorts(Map.of("id", Sort.Direction.ASC)).build();
  }

  // 2. 加工担当
  @Bean
  public ItemProcessor<Todo, Todo> Processor() {
    return item -> {

      LocalDate now = LocalDate.now();
      if (item.getTime_limit() != null && item.getTime_limit().isBefore(now)
          && item.getDone_flg() == 0) {
        item.setExpiredFlg(1);
        System.out.println("期限が切れてます" + item.getTitle());
      }
      return item;
    };
  }

  // 3. 保管担当
  @Bean
  public RepositoryItemWriter<Todo> ItemWriter(TodoRepository repository) {
    return new RepositoryItemWriterBuilder<Todo>().repository(repository).methodName("save")
        .build();
  }

  @Bean
  public Step myChankstep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager, RepositoryItemReader<Todo> reader,
      ItemProcessor<Todo, Todo> processor, ItemWriter<Todo> itemWriter) {
    return new StepBuilder("chunkStep", jobRepository).<Todo, Todo>chunk(10, transactionManager)
        .reader(reader).processor(processor).writer(itemWriter).build();
  }

  // ------------------------------Chank↑---------------------------------------------------

  // tasklet chankでstep部分変更
  @Bean
  public Job myjob(JobRepository jobRepository, Step step1) {
    return new JobBuilder("myjob", jobRepository).start(step1).build();
  }
}
