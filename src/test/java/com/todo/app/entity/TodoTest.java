package com.todo.app.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TodoTest {

  @Test
  @DisplayName("期限切れフラグ（1）が正常に保持されることを確認")
  void setInsert() {

    // テストデータの準備
    Todo todo = new Todo();
    // 期限切れフラグ（1）をセット
    todo.setExpiredFlg(1);

    // セットした値がGetterで正しく取り出せるか検証
    assertEquals(1, todo.getExpiredFlg());

  }
}
