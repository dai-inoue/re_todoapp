package com.todo.app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateForm {
	@NotEmpty(message = "タスク欄は必須入力です。")
	String notEmptyField;

}
