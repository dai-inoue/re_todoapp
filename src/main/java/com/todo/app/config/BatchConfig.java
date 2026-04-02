package com.todo.app.config;

import java.util.List;
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
  public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
      TodoRepository repository) {
    return new StepBuilder("step1", jobRepository).tasklet((contribution, chunkContext) -> {

      List<Todo> items = repository.findAll();

      repository.saveAll(items);

      for (Todo item : items) {
        // 🔍 ここで1件ずつ Item の「中身」を取り出して確認している！
        System.out.println("いま処理中の Item はこれだ: " + item.getTitle());
      }
      return RepeatStatus.FINISHED;
    }, transactionManager).build();
  }
  // ------------------------------tasklet↑---------------------------------------------------

  // 1. 読み込み担当 (Reader)
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

      // 🔍 ここでベルトコンベアの上を流れる Item を1件ずつ覗き見る！
      System.out.println("【Chunk】いま流れてきた Item はこれだ: " + item.getTitle());

      // 何も加工せずにそのまま Writer（次の工程）へ流す
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
  public Job myjob(JobRepository jobRepository, Step myChankstep) {
    return new JobBuilder("myjob", jobRepository).start(myChankstep).build();
  }
}
