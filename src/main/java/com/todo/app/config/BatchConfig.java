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
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
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
  @Bean
  // 部品の作成(Step)tasklet
  public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
      TodoRepository Repository) {
    return new StepBuilder("step1", jobRepository).tasklet((contribution, chunkContext) -> {

      List<Todo> items = Repository.findAll();

      System.out.println("=== 【Tasklet開始】一気に全件（" + items.size() + "件）表示します ===");

      for (Todo item : items) {
        System.out.println("taskletで処理中" + item.getTitle());
      }
      System.out.println("=== 【Tasklet終了】一括処理が終わりました ===");
      return RepeatStatus.FINISHED;
    }, transactionManager).build();
  }

  /*
   * 井上メモ TodoRepository=業務データ」の貯蓄 JobRepository=「バッチの履歴」の貯蓄 TodoRepository
   * repositoryはDBに繋げるための引数→他引数もあるので検索が必要 やりたいことを調べる方法Spring Boot [やりたいこと] DI(例)Spring Boot メール送信
   * DIなど contribution, chunkContext→ラムダ式書く際の作法
   */


  // ------------------------------tasklet↑---------------------------------------------------

  // 1. 読み込み担当 (Reader)
  @Bean
  public RepositoryItemReader<Todo> reader(TodoRepository repository) {
    return new RepositoryItemReaderBuilder<Todo>().name("TodoReader").repository(repository)
        .methodName("findAll").pageSize(10).sorts(Map.of("id", Sort.Direction.ASC)).build();
  }

  // 2. 加工担当(Processor)
  @Bean
  public ItemProcessor<Todo, Todo> processor() {
    return item -> {
      return item;
    };
  }

  // 3. 保管担当(Writer)
  @Bean
  public ItemWriter<Todo> itemWriter(TodoRepository repository) {
    // RepositoryItemWriterBuilder は使わずに、自分で中身を定義します
    return new ItemWriter<Todo>() {
      @Override
      public void write(org.springframework.batch.item.Chunk<? extends Todo> chunk)
          throws Exception {

        // --- ここが 10 件まとまった瞬間に 1 回だけ実行されるエリア ---
        System.out.println("==========================================");
        System.out.println("📦 【Chunk実行中】今から " + chunk.size() + " 件まとめてDBに保存（移動）します");

        for (Todo item : chunk) {
          System.out.println("   -> 対象データ: " + item.getTitle());
          // ビルダーが裏でやっていた「save」を自分で呼び出す
          repository.save(item);
        }

        // DBに確実に反映させる
        repository.flush();

        System.out.println("✅ 10件のコミットが完了しました！");
        System.out.println("==========================================");
      }
    };
  }

  @Bean
  public Step myChunkstep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager, RepositoryItemReader<Todo> reader,
      ItemProcessor<Todo, Todo> processor, ItemWriter<Todo> itemWriter) {

    return new StepBuilder("chunkstep", jobRepository).<Todo, Todo>chunk(10, transactionManager)
        .reader(reader).processor(processor).writer(itemWriter).build();
  }



  // ------------------------------Chank↑---------------------------------------------------

  // パターンA：Step1（Tasklet）だけで終わるジョブ

  @Bean
  public Job myJob(JobRepository jobRepository, Step step1, Step myChunkstep) {
    // start(step1) で Tasklet を動かし、
    // その後に .next(myChankstep) で Chunk を動かす設定にする
    return new JobBuilder("myJob", jobRepository).start(step1) // ← ここで step1 を指定！
        .next(myChunkstep) // ← ここで Chunk を繋げる！
        .build();
  }
}
