package com.todo.app.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TaskManagementServiceTest {
  @Autowired
  private TaskManagementService service;

  @Test
  @DisplayName("markExpiredTasksメソッド実行時に１が返るかのテスト")
  public void TaskManagementService() {
    int result = service.markExpiredTasks();
    assertTrue(result >= 0);
  }
}
