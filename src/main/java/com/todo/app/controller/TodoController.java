package com.todo.app.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;
@Controller
public class TodoController {
	@Autowired
	TodoMapper todoMapper;
	
	@RequestMapping(value="/")
	public String index(Model model) {
		 List<Todo> incompleteList = todoMapper.selectIncomplete();
		 model.addAttribute("todos", incompleteList);

		 List<Todo> completeList = todoMapper.selectComplete(); // これがあるか確認
		 model.addAttribute("doneTodos", completeList); // これがあるか確認
		
		return "index";
	}
	
	@RequestMapping(value="/add")
	public String add(Todo todo) {
		todoMapper.add(todo);
		return "redirect:/";
	}
	
	@RequestMapping(value="/update")
	public String update(Todo todo) {
		todoMapper.update(todo);
		return "redirect:/";
	}
	
	// 削除処理
	@RequestMapping(value="/delete")
	public String delete() {
		todoMapper.delete();
		return "redirect:/";
			
	}	
}
	