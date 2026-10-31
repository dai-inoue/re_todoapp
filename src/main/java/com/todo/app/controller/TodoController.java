package com.todo.app.controller;

import java.time.LocalDate;
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
import com.todo.app.entity.TaskGroup;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;
import com.todo.app.service.TaskManagementService;
import jakarta.validation.Valid;

/**
 * Todoアイテムに関するリクエストを処理するコントローラークラスです。
 */
@Controller
public class TodoController {


  @Autowired
  TaskManagementService taskManagementService;

  // DBを使えるようにするために使うもの!
  @Autowired
  TodoMapper todoMapper;

  // 一覧表示用のデータ(マイタスク・完了済み・期限切れ)をModelに詰める共通処理
  private void setTaskLists(Model model) {
    TaskGroup group =
        taskManagementService.categorizeTasks(todoMapper.selectAll(), LocalDate.now());
    Collections.sort(group.getMyTasks());
    Collections.sort(group.getExpiredTasks());
    model.addAttribute("todos", group.getMyTasks());
    model.addAttribute("doneTodos", group.getCompletedTasks());
    model.addAttribute("expiredTodos", group.getExpiredTasks());
  }

  // 目的のURLに対して直接アクセスするメソッド
  @RequestMapping(value = "/")
  // controllerで作成されたデータをviewに渡す
  public String index(Model model) {
    setTaskLists(model);
    // 新しいタスクを入力するためのフォームを、初期化状態で表示
    model.addAttribute("todo", new Todo());
    return "index";
  }

  // 追加処理
  @PostMapping(value = "/add")
  // TodoMapper.javaインタフェースのadd( )メソッドを実行
  // index.htmlで入力されたデータがTodoへ入る
  public String add(@Valid Todo todo, BindingResult bindingResult, Model model) {
    // バリデーション機能でエラーがあった際
    if (bindingResult.hasErrors()) {
      setTaskLists(model); // ← これ1行で、3つのリストを全部Modelに詰める
      return "index";
    }
    // エラーなければTodoアイテムをデータベースに保存する
    todoMapper.add(todo);
    return "redirect:/";


  } // 完了済処理
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
  // @RequestParamデータを受け取る
  // @GetMapping対象の画面見たいとき

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
      model.addAttribute("isSearched", true);
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

    // 検索結果をisSearchedに入れhtmlへ渡す
    model.addAttribute("isSearched", true);
    return "index";

  }

  // 日付・タスク更新処理
  // htmlの/task/updateへリクエストを**受け取る**
  // @PostMapping HTMLから送られてきた値を受け取る

  @PostMapping(value = "/task/update")
  // id title time_limitを更新する
  public String updateItem(@RequestParam("id") int id, @RequestParam("title") String title,
      @RequestParam("time_limit") String time_limit) {
    // 取得した３つのデータを更新する
    todoMapper.updateItem(id, title, time_limit);

    return "redirect:/";
  }// ここまで完了20260621

  // タスク完了から未完了更新処理
  // htmlの/task/restoreへリクエストを**受け取る**
  @PostMapping("/task/restore")
  public String restoreTask(@RequestParam("id") int id) {
    // 取得したデータを未完了にする
    todoMapper.markAsIncomplete(id);
    return "redirect:/"; // タスクを戻した後、トップページにリダイレクト
  }
}
