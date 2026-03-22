package com.todo.app.config;

import java.time.LocalDate;
import java.util.Collections;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
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
  public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
      ItemReader<Todo> reader, ItemProcessor<Todo, Todo> processor,
      RepositoryItemWriter<Todo> writer) { // ← ここを「RepositoryItemWriter」に！

    return new StepBuilder("step1", jobRepository).<Todo, Todo>chunk(10, transactionManager)
        .reader(reader).processor(processor).writer(writer).build();
  }

  // 部品の作成(job)
  @Bean
  public Job taskUpdateJob(JobRepository jobRepository, Step step1) {
    return new JobBuilder("taskUpdateJob", jobRepository).start(step1).build();
  }

  // ------------------↑tasklet------------------↓Chunk----------------------------

  // 1. 読み込み担当 (Reader)
  @Bean
  public RepositoryItemReader<Todo> reader(TodoRepository repository) {
    // 1件だけ、テスト用のTask（荷物）を作って入れる
    return new RepositoryItemReaderBuilder<Todo>().name("todoResder").repository(repository)
        .methodName("findAll").pageSize(10)
        .sorts(Collections.singletonMap("id", Sort.Direction.ASC)).build();
  }

  // 2. 加工担当
  @Bean
  public ItemProcessor<Todo, Todo> Processor() {
    return item -> {
      LocalDate today = LocalDate.now();
      // 期限(time_limit)が今日より前、かつ 未完了(done_flgが0) の場合
      if (item.getTime_limit() != null && item.getTime_limit().isBefore(today)
          && item.getDone_flg() == 0) {
        item.setExpiredFlg(1); // 期限切れフラグを立てる
        System.out.println("期限切れを発見しました: " + item.getTitle());
      }
      return item;
    };
  }

  // 3. 書き出し担当
  @Bean
  public RepositoryItemWriter<Todo> itemWriter(TodoRepository repository) { // ← ここも
                                                                            // RepositoryItemWriter
                                                                            // に！
    return new RepositoryItemWriterBuilder<Todo>().repository(repository).methodName("save")
        .build();
  }

  @Bean
  public Step myChunkStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager, RepositoryItemReader<Todo> reader, // 追加
      ItemProcessor<Todo, Todo> processor, // 追加
      ItemWriter<Todo> ItemWriter) { // 追加

    return new StepBuilder("chunkstep", jobRepository).<Todo, Todo>chunk(10, transactionManager) // <String,
                                                                                                 // String>
                                                                                                 // から変更
        .reader(reader) // () を取って、引数の reader を使う
        .processor(processor) // () を取って、引数の processor を使う
        .writer(ItemWriter) // () を取って、引数の writer を使う
        .build();
  }

  @Bean
  public Job myJob(JobRepository jobRepository, Step myChunkStep) {
    return new JobBuilder("myJob", jobRepository).start(myChunkStep).build();
  }
}
