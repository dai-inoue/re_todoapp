package com.todo.app.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import com.todo.app.entity.Todo;



public class TodoUnitTest {
  @ParameterizedTest
  @CsvFileSource(resources = "/todo_import.csv", numLinesToSkip = 1)
  void testTodoSetAndGet1(String title, String timeLimitStr) {

    Todo todo = new Todo();
    todo.setTitle(title);
    todo.setTime_limit(LocalDate.parse(timeLimitStr));

    // ちゃんとタイトルがセットされているか確認する！
    assertEquals(title, todo.getTitle(), "タイトルが一致すること");

    // ちゃんと日付がセットされているか確認する！
    assertEquals(LocalDate.parse(timeLimitStr), todo.getTime_limit(), "期限が一致すること");
  }
}

