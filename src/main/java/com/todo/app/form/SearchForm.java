package com.todo.app.form;

import jakarta.validation.constraints.NotBlank;

public class SearchForm {

  @NotBlank(message = "{NotBlank.todo.keyword}")
  private String kuyword;
  // todo
}
