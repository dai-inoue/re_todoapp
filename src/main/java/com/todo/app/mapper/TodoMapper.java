package com.todo.app.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todo.app.entity.Todo;
@Mapper
public interface TodoMapper {
    public List<Todo> selectAll();
    public List<Todo> selectIncomplete(); // 未完了タスクのみ表示
    public List<Todo> selectComplete(); // ★これを追加：完了タスクのみ表示
    public void add(Todo todo); // タスク追加
    public void update(Todo todo); // タスク更新
    public void delete(); //タスク削除
}