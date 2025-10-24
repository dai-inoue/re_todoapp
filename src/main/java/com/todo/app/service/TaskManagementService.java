package com.todo.app.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;

import jakarta.transaction.Transactional;

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
}