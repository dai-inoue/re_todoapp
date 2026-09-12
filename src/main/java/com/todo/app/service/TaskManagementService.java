package com.todo.app.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.todo.app.entity.TaskGroup;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;

@Service
public class TaskManagementService {
  @Autowired

  private TodoMapper todoMapper;

  /**
   * 実行時の日付以前の未完了タスクを期限切れとマークする
   *
   * @author SS2230
   * @return 期限切れとマークされたタスクの数
   */
  @Transactional
  public int markExpiredTasks() {
    List<Todo> expiredTodos = todoMapper.searchExpiredTodos(LocalDate.now());
    for (Todo todo : expiredTodos) {
      todo.setExpiredFlg(1);
      todoMapper.update(todo);
    }
    return expiredTodos.size();
  }

  public TaskGroup categorizeTasks(List<Todo> allTasks, LocalDate today) {

    TaskGroup group = new TaskGroup();

    for (Todo todo : allTasks) {
      if (todo.getDone_flg() == 1) {
        // 完了済
        group.getCompletedTasks().add(todo);
      } else if (todo.getTime_limit() != null && todo.getTime_limit().isBefore(today)) {
        // 期限切れ（未完了 かつ 期限日が存在し今日より前）
        group.getExpiredTasks().add(todo);
      } else {
        // マイタスク（上記以外：今日以降、または期限日なし）
        group.getMyTasks().add(todo);
      }
    }
    return group;
  }
}

