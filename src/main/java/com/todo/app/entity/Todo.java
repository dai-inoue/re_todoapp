package com.todo.app.entity;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "todo_items")
// getter setterを記入する必要がなくなる
@Data

/**
 * todoアイテムを表すエンティティクラス DBのテーブルに値する
 */

// Comparable比較できる
public class Todo implements Comparable<Todo> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private long id;
  @NotBlank(message = "タスクを入力してください")
  private String title;

  // 時間や日付のフォーマットの指定
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "日付を入れてください")
  @Column(name = "time_limit")
  private LocalDate time_limit;

  @Column(name = "done_flg")
  private int done_flg;

  @Column(name = "expired_flg")
  private int expiredFlg;

  // このクラスではあくまでもソートのルールを定義している
  @Override
  // compareTo比較する
  public int compareTo(Todo otherTodo) {

    // 日付がどちらも未記入な場合(ID)で比較
    if (this.time_limit == null && otherTodo.time_limit == null) {
      return Long.compare(id, otherTodo.id);
      // 自分の期限が未記入な時
    } else if (this.time_limit == null) {
      return 1;
      // 相手の期限が未記入の場合(自分を前に)
    } else if (otherTodo.time_limit == null) {
      return -1;
      // 日付がどちらも同じ場合
    } else {
      int dateComparison = this.time_limit.compareTo(otherTodo.time_limit);
      // (ID)で比較
      if (dateComparison != 0) {
        return dateComparison;
      } else {
        return Long.compare(id, otherTodo.id);
        // 期限が同じの場合IDで比較
      }
    }
  }
}
