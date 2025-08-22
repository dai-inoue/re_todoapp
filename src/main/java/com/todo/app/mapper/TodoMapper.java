package com.todo.app.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.todo.app.entity.Todo;

@Mapper
// DBに『お願い』をするためのリスト
// TodoController クラスは、ユーザーからのタスクを追加や削除という様々な要求を受けとる
public interface TodoMapper {
  public List<Todo> selectAll();

  public List<Todo> selectIncomplete(); // 未完了タスクのみ表示

  public List<Todo> selectComplete(); // ：完了タスクのみ表示

  public void update(Todo todo); // タスク更新

  public void delete(Long id); // タスク削除 todo

  // タスク追加
  @Insert("INSERT INTO todo_items (title, time_limit, done_flg) VALUES (#{title}, #{time_limit}, 0)")
  public void add(Todo todo);

  // 検索機能
  @Select("select * from todo_items where title LIKE CONCAT('%',#{keyword},'%')")
  List<Todo> searchBytitle(String keyword);

  // 完了済のTodoアイテムをすべて削除する
  @Delete("DELETE from todo_items where done_flg = 1")
  void deleteDoneTodos();
}
