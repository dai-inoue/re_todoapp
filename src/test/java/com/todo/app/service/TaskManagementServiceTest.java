package com.todo.app.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.todo.app.entity.TaskGroup;
import com.todo.app.entity.Todo;

@SpringBootTest
@Transactional
public class TaskManagementServiceTest {
  // @Autowired
  // private TaskManagementService service;
  //
  // @Test
  // @DisplayName("markExpiredTasksメソッド実行時に１が返るかのテスト")
  // public void TaskManagementService() {
  // int result = service.markExpiredTasks();
  // assertTrue(result >= 0);
  // }

  @Test
  @DisplayName("タスク自動分類処理（categorizeTasks）期限が過去の未完了タスクのテスト")
  public void categorizeTasks() {
    LocalDate today = LocalDate.of(2026, 8, 13); // 基準日
    Todo todo = new Todo();
    todo.setTime_limit(LocalDate.of(2026, 8, 12)); // 期限日（過去）
    todo.setDone_flg(0); // 未完了 (int型)

    List<Todo> allTasks = new ArrayList<>();
    allTasks.add(todo);

    TaskManagementService taskManagementService = new TaskManagementService();
    // ② Act（テスト対象メソッドの実行）
    TaskGroup result = taskManagementService.categorizeTasks(allTasks, today);
    // ③ 検証（今度は ExpiredTasks に入っていることを確認する！）
    assertEquals(1, result.getExpiredTasks().size());
    assertEquals(0, result.getMyTasks().size()); // ★ここが 1 になる
    assertEquals(0, result.getCompletedTasks().size());
  }
}
