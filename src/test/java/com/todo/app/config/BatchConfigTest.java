package com.todo.app.config;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.todo.app.TodoRepository;
import com.todo.app.entity.Todo;


@SpringBootTest(
    // テスト用のDBを作成
    properties = {"spring.batch.job.enabled=false", "spring.jpa.hibernate.ddl-auto=update",
        "spring.batch.jdbc.initialize-schema=always"})
@ActiveProfiles("test")
@SpringBatchTest

public class BatchConfigTest {

  @Autowired
  private TodoRepository todoRepository;
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private Job myJob;

  @BeforeEach
  void setJob() {
    this.jobLauncherTestUtils.setJob(myJob);
  }

  @Test
  void 本物のBatchジョブを動かすテスト() throws Exception {
    try {
      todoRepository.deleteAll();
      todoRepository.flush();

      // 1. 下準備
      for (int i = 1; i <= 100; i++) {
        Todo t = new Todo();
        t.setTitle("未処理データ" + i);
        t.setTime_limit(LocalDate.now());
        todoRepository.save(t);
      }
      todoRepository.flush();
      System.out.println("--- 下準備完了（100件） ---");

      // 2. 本番
      System.out.println("★Job起動直前...");
      JobExecution jobExecution = jobLauncherTestUtils.launchJob();
      System.out.println("Jobの実行結果: " + jobExecution.getStatus());

    } catch (Exception e) {
      System.err.println("❌Batch起動失敗！❌");
      e.printStackTrace(); // 👈 ここで本当のエラーが出るはず
    }
  }
}
