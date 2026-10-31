package com.todo.app;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;

@SpringBootTest
public class TodoMapperCsvTest {

  @Autowired
  private TodoMapper todoMapper;

  @ParameterizedTest
  @CsvFileSource(resources = "/csv/todo_import.csv", numLinesToSkip = 1)
  void testSearchByCsvData(String title, String timeLimitStr) {

    // 1. CSVから読み込んだ「title」をキーワードにして、Mapperの検索を実行する
    List<Todo> searchResult = todoMapper.searchBytitle(title);

    // 2. 条件分岐：データが「当てはまっている（結果が見つかった）」場合だけ出力する
    if (searchResult != null && !searchResult.isEmpty()) {
      System.out.println("【出力あり（ヒット）】CSVの「" + title + "」に一致するデータが見つかりました！");

      // ヒットした詳細をコンソールに表示
      for (Todo todo : searchResult) {
        System.out.println("  → 取得タイトル: " + todo.getTitle() + " / 期限: " + todo.getTime_limit());
      }
    } else {
      // 3. 当てはまっていなければ【未出力】（コンソールには何も文字を出さない）
      // あえて何も書かないことで、ヒットしなかったデータはスルーされます
    }
  }
}
