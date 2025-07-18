package com.todo.app.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data

public class Todo {

    private long id;
    private String title;
    private int done_flg;
  

    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time_limit;
}
