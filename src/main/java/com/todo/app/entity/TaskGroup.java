package com.todo.app.entity;

import java.util.ArrayList;
import java.util.List;

public class TaskGroup {

  private List<Todo> myTasks = new ArrayList<>(); // （マイタスク一覧）
  private List<Todo> completedTasks = new ArrayList<>(); // （完了済み一覧）
  private List<Todo> expiredTasks = new ArrayList<>(); // （期限切れ一覧）


  // Getter（値を取得する）
  public List<Todo> getMyTasks() {
    return myTasks;
  }

  // Setter（値をセットする）
  public void setMyTasks(List<Todo> myTasks) {
    this.myTasks = myTasks;
  }

  // Getter（値を取得する）
  public List<Todo> getCompletedTasks() {
    return completedTasks;
  }

  // Setter（値をセットする）
  public void setCompletedTasks(List<Todo> completedTasks) {
    this.completedTasks = completedTasks;
  }

  // Getter（値を取得する）
  public List<Todo> getExpiredTasks() {
    return expiredTasks;
  }

  // Setter（値をセットする）
  public void setExpiredTasks(List<Todo> expiredTasks) {
    this.expiredTasks = expiredTasks;
  }
}
