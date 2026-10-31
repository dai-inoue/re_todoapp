package com.todo.app.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;


@Service
public class CsvImportService {

  @Autowired
  private TodoMapper todoMapper;

  // 指定したフォルダ配下のCSVファイ ルを読み込んでDBに登録する
  public void importFromDirectory(String dirPath) throws IOException {
    Path targetDir = Path.of(dirPath);

    if (!Files.exists(targetDir) || !Files.isDirectory(targetDir)) {
      throw new IllegalArgumentException("指定されたフォルダが見つかりません: " + dirPath);
    }

    // フォルダ配下のファイルをすべてチェック
    try (var paths = Files.list(targetDir)) {
      paths.filter(Files::isRegularFile).filter(filePath -> filePath.toString().endsWith(".csv")) // .csvファイルだけを対象にする
          .forEach(filePath -> {
            try {
              // 1. CSVファイルの全行をリストとして読み込む
              List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

              // 2. 1行ずつ処理する（i = 1 から始めて、1行目のヘッダー "title,time_limit" を飛ばす！）
              for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank())
                  continue; // 空行があればスキップ

                // カンマ (,) で文字を左右に分割する
                // 例: "牛乳を買う,2026-06-30" → [0] "牛乳を買う", [1] "2026-06-30"
                String[] values = line.split(",");
                String title = values[0];
                LocalDate timeLimit = LocalDate.parse(values[1]);

                // 3. Todoオブジェクトに詰める
                Todo todo = new Todo();
                todo.setTitle(title);
                todo.setTime_limit(timeLimit);
                todo.setDone_flg(0); // 未完了状態でセット

                // 4. データベースに登録！
                todoMapper.add(todo);
              }

              System.out.println("CSV読み込み成功: " + filePath.getFileName());

            } catch (Exception e) {
              e.printStackTrace();
            }
          });
    }
  }
}
