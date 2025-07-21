package com.todo.app.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// getter setterを記入する必要がなくなる
@Data

/**
 * todoアイテムを表すエンティティクラス
 * DBのテーブルに値する
 */

public class Todo {

    private long id;
    private String title;
    private int done_flg;
  
    // 時間や日付のフォーマットの指定
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time_limit;
}
