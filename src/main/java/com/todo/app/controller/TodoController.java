package com.todo.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;
import jakarta.validation.Valid;

/**
 * Todoアイテムに関するリクエストを処理するコントローラークラスです。
 */
@Controller
public class TodoController {

  // DBを使えるようにするために使うもの!
  @Autowired
  TodoMapper todoMapper;

  // 目的のURLに対して直接アクセスするメソッド
  @RequestMapping(value = "/")
  // controllerで作成されたデータをviewに渡す
  public String index(Model model) {
    // ArrayListの作成
    ArrayList<String> List = new ArrayList<>();

    // TodoMapper.javaインタフェースのselectIncomplete( )メソッドを実行
    // 未完了のTodoアイテムのリストをデータベースから取得
    List<Todo> incompleteList = todoMapper.selectIncomplete();
    // 取得した未完了リストを "todos" という名前でモデルに追加.その後viewで使用
    model.addAttribute("todos", incompleteList);

    // TodoMapper.javaインタフェースのselectComplete( )メソッドを実行
    // 完了のTodoアイテムのリストをデータベースから取得
    List<Todo> completeList = todoMapper.selectComplete();
    // 取得した完了リストを "todos" という名前でモデルに追加.その後viewで使用
    model.addAttribute("doneTodos", completeList);

    // 新しいタスクを入力するためのフォームを、初期化状態で表示
    model.addAttribute("todo", new Todo());

    // TodoクラスのcompareToメソッドに基づいてincompleteListをソートします
    Collections.sort(incompleteList);

    return "index";


  }

  // 追加処理
  @PostMapping(value = "/add")
  // TodoMapper.javaインタフェースのadd( )メソッドを実行
  // index.htmlで入力されたデータがTodoへ入る
  public String add(@Valid Todo todo, BindingResult bindingResult, Model model) {
    // バリデーション機能でエラーがあった際
    if (bindingResult.hasErrors()) {
      // データベースから未完了リストのtodoリストを取得して(incompleteList)に格納される
      List<Todo> incompleteList = todoMapper.selectIncomplete();
      // 未完了リストをソートする
      Collections.sort(incompleteList);
      // 未完了リストを"todos"という名前でviewへ渡す
      model.addAttribute("todos", incompleteList);

      List<Todo> completeList = todoMapper.selectComplete();
      // 取得した完了リストを "todos" という名前でモデルに追加.その後viewで使用
      model.addAttribute("doneTodos", completeList);

      return "index";
    }
    // エラーなければTodoアイテムをデータベースに保存する
    todoMapper.add(todo);
    return "redirect:/";
  }


  // 完了済処理
  // 目的のURLに対して直接アクセスするためのメソッド
  @RequestMapping(value = "/update")
  // TodoMapper.javaインタフェースのupdate( )メソッドを実行
  public String update(Todo todo) {
    // データベースのTodoアイテムを完了済とする
    todoMapper.update(todo);
    return "redirect:/";
  }

  // マイタスク削除メソッド todo
  @PostMapping("/delete-done")
  public String deleteDoneTodos() {
    todoMapper.deleteDoneTodos();
    return "redirect:/";
  }

  // 検索機能の追加
  // webブラウザからアクセスしたとき
  @GetMapping(value = "/search")
  public String serch(@RequestParam(value = "keyword", required = false) String keyword,
      // modelで引数をHTML側へ渡す
      Model model) {
    // エラー画面時、再表示させるもの
    if (keyword == null || keyword.isBlank()) {
      model.addAttribute("error", "検索キーワードを入れてください");
      model.addAttribute("todos", todoMapper.selectComplete());
      model.addAttribute("donetodos", todoMapper.selectIncomplete());
      model.addAttribute("todo", new Todo());
      return "index";
    }
    // 正しいkeywordが入力された際searchResultに格納される
    List<Todo> searchResults = todoMapper.searchBytitle(keyword);

    // 検索結果から未完了のタスク分ける
    List<Todo> incompleteResults =
        // stream データを効率的に処理するjavaの機能 ->(各Todoオブジェクトに対してこの処理を行う)
        // Collectors.toList()): フィルターを通過したすべてのTodoオブジェクトを再度Listとして収集
        searchResults.stream().filter(todo -> todo.getDone_flg() == 0).collect(Collectors.toList());
    // 検索結果から完了済のタスク分ける
    List<Todo> completeResults =
        searchResults.stream().filter(todo -> todo.getDone_flg() == 1).collect(Collectors.toList());

    // それぞれのリストをモデルに渡す
    model.addAttribute("todos", incompleteResults);
    model.addAttribute("doneTodos", completeResults);

    // 新しいタスクを追加フォーム用に、新しい空のTodoオブジェクトを渡す
    model.addAttribute("todo", new Todo());

    // コメントtodo
    model.addAttribute("isSearched", true);
    return "index";
  }

  // todo 日付・タスク更新処理
  @PostMapping(value = "/task/update")
  public String updateItem(@RequestParam("id") int id, @RequestParam("title") String title,
      @RequestParam("time_limit") String time_limit) {
    todoMapper.updateItem(id, title, time_limit);

    return "redirect:/";
  }

  // タスク完了から未完了更新処理 コメントtodo
  @PostMapping("/task/restore")
  public String restoreTask(@RequestParam("id") int id) {
    todoMapper.markAsIncomplete(id);
    return "redirect:/"; // タスクを戻した後、トップページにリダイレクト
  }
}
