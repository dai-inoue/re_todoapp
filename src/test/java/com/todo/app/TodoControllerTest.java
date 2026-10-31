package com.todo.app;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

public class TodoControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TodoMapper todoMapper; // データベースに任意のデータを直接入れるために使う

  @Test
  void testIndexWithCustomData() throws Exception {
    // 1. 自分で任意のデータ（牛乳を飲む）を作る
    Todo customTodo = new Todo();
    customTodo.setTitle("牛乳を飲む");
    customTodo.setTime_limit(LocalDate.parse("2026-09-13"));
    // ※もし「完了フラグ」やステータスを分けるカラムがある場合は、ここにセット処理を追加します

    // 2. 作ったデータをデータベースにINSERTする
    todoMapper.add(customTodo);

    // 3. ブラウザの代わりに "/" にアクセス（これでコントローラーの index() が動く！）
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }

  @Autowired
  private MockMvc mockMvc1;

  @MockBean
  private TodoMapper todoMapper1;

  @Test
  void test2() throws Exception {
    List<Todo> completeList = new ArrayList<>();
    Todo todo = new Todo();
    todo.setTitle("テスト用データ");
    completeList.add(todo);


    when(todoMapper.selectComplete()).thenReturn(completeList);

    // 3. ブラウザの代わりに "/" にアクセス（これでコントローラーの index() が動く！）
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }

  @Test
  void test3() throws Exception {
    List<Todo> incompleteList = new ArrayList<>();
    Todo todo = new Todo();
    todo.setTitle("テスト用データ2");
    incompleteList.add(todo);


    when(todoMapper.selectIncomplete()).thenReturn(incompleteList);

    // 3. ブラウザの代わりに "/" にアクセス（これでコントローラーの index() が動く！）
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }

  @Test
  void test4() throws Exception {
    List<Todo> incompleteList = new ArrayList<>();
    Todo todo = new Todo();

    when(todoMapper.selectIncomplete()).thenReturn(incompleteList);

    // 3. ブラウザの代わりに "/" にアクセス（これでコントローラーの index() が動く！）
    mockMvc.perform(get("/")).andExpect(status().isOk());

  }
}
