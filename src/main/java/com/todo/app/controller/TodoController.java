package com.todo.app.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;

/**
 * Todoアイテムに関するリクエストを処理するコントローラークラスです。
 */
@Controller
public class TodoController {

	// DBを使えるようにするために使うもの
	@Autowired
	TodoMapper todoMapper;

	// 目的のURLに対して直接アクセスするメソッド
	@RequestMapping(value="/")
	// controllerで作成されたデータをviewに渡す
	public String index(Model model) {

		// TodoMapper.javaインタフェースのselectIncomplete( )メソッドを実行 
		// 未完了のTodoアイテムのリストをデータベースから取得
		List<Todo> incompleteList = todoMapper.selectIncomplete();
		// 取得した未完了リストを "todos" という名前でモデルに追加.その後viewで使用
		model.addAttribute("todos", incompleteList);

		// TodoMapper.javaインタフェースのselectComplete( )メソッドを実行 
		// 完了のTodoアイテムのリストをデータベースから取得
		List<Todo> completeList = todoMapper.selectComplete();

		// TodoクラスのcompareToメソッドに基づいてincompleteListをソートします
		Collections.sort(incompleteList);

		// 取得した完了リストを "todos" という名前でモデルに追加.その後viewで使用
		model.addAttribute("doneTodos", completeList);

		return "index";
	}

	// 追加処理
	@RequestMapping(value="/add")
	// TodoMapper.javaインタフェースのadd( )メソッドを実行 
	// index.htmlで入力されたデータがTodoへ入る
	public String add(Todo todo) {
		// Todoアイテムをデータベースに保存する
		todoMapper.add(todo);
		return "redirect:/";
	}

	// 完了済処理
	@RequestMapping(value="/update")
	// TodoMapper.javaインタフェースのupdate( )メソッドを実行 
	public String update(Todo todo) {
		// データベースのTodoアイテムを完了済とする
		todoMapper.update(todo);
		return "redirect:/";
	}

	// 削除処理
	@RequestMapping(value="/delete")
	public String delete() {
		// TodoMapper.javaインタフェースのdelete( )メソッドを実行 
		todoMapper.delete();
		return "redirect:/";

	}	
}
