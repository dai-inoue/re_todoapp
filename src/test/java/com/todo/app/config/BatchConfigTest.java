package com.todo.app.config;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.todo.app.service.TaskManagementService;


@SpringBootTest(
    // テスト用のDBを作成
    properties = {"spring.batch.job.enabled=false", "spring.batch.jdbc.initialize-schema=always"})
// JobLauncherTestUtilsを使えるようにする
@SpringBatchTest
// @ContextConfiguration(classes = {BatchConfig.class, DefaultBatchConfiguration.class
// Batch 5系のSpring Batchを動かすための基盤を読み込む

public class BatchConfigTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils; // お作法：job起動時のルール

  @MockBean
  private TaskManagementService taskManagementService; // お作法：taskManagementServiceの部品を使用

  @Test
  public void testMarkExpiredTasks() throws Exception {
    // 1. もしmarkExpiredTasksを実行した際に１を返す
    Mockito.when(taskManagementService.markExpiredTasks()).thenReturn(1);

    // 2. ジョブ実行 メモを取る
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    // 3. ステータス確認
    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    // 4.検証中(無事通りましたという印)
    Mockito.verify(taskManagementService).markExpiredTasks();

    System.out.println("テスト完了！");
  }
}
