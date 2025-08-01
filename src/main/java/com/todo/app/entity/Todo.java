package com.todo.app.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// getter setterを記入する必要がなくなる
@Data

/**
 * todoアイテムを表すエンティティクラス
 * DBのテーブルに値する
 */

public class Todo implements Comparable<Todo> { 

	private long id;
	@NotBlank(message = "空白ではだめです")
	private String title;

	// 時間や日付のフォーマットの指定
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "日付を入れてください")
	private LocalDate time_limit;

	private int done_flg;

	// このクラスではあくまでもソートのルールを定義している
	@Override
	public int compareTo(Todo otherTodo) {
		// time_limit(期限日時で昇順ソート)
		// compareToメソッドの戻り値ルール
		// 負の整数を返す　thisオブジェクトがotherTodoオブジェクトよりも「小さい」(ソート順で前に来る)
		// ゼロを返す thisオブジェクトがotherTodoオブジェクトが等しい場合
		// 正の整数を返す thisオブジェクトがotherTodoオブジェクトよりも大きい

		// time_limitがnullの場合（期限が未設定の場合）の扱いを特別に考慮
		// 「期限が設定されているタスクを大きい、未設定のタスクを小さい」というロジックにする

		// 1両方がnull場合(どちらともタスク期限が設定されていない場合)
		if(time_limit == null && otherTodo.time_limit == null) {
			// 両方がnull場合IDで比較
			return Long.compare(this.id, otherTodo.id);

			// 2現タスクがthis.time_limitがnullで比較対象のタスク (otherTodo) には期限が設定されている
		} else if(this.time_limit == null) {
			// thisがnull期限なしなので、otherTodo(期限あり)よりも「大きい」(ソート順で後)と判断
			// これにより、期限が設定されていないタスクは、期限が設定されているタスクの後に並ぶ
			return 1;

			// 3現在のタスク (this) には期限が設定されており、比較対象のタスク (otherTodo) の time_limit が null の場合
		} else if(otherTodo.time_limit == null) {
			// otherTodoがnull（期限なし）なので、this（期限あり）よりも「小さい」（ソート順で前）と判断
			// これにより、期限が設定されているタスクは、期限が設定されていないタスクの前に並ぶ
			return -1;

			// 4両方のタスクのtime_limit が null でない場合（どちらのタスクも期限が設定されている）
		} else {
			// java.util.Date クラスの compareTo メソッドを使用して、日付を直接比較
			// この比較結果は、thisの日付がotherTodoの日付より「前」なら負、「同じ」なら0、「後」なら正の値を返す 
			int dateComparison = this.time_limit.compareTo(otherTodo.time_limit);
			// 日付が異なる場合（比較結果が0でない場合）
			if(dateComparison != 0) {
				// その比較結果（日付の昇順）を最終的なソート順として返す
				return dateComparison;
			} else {
				// long型のidを比較し、その結果を返します。
				// Long.compare(a, b) は、aがbより小さい場合は負、等しい場合は0、大きい場合は正を返します。
				// これにより、idが小さいものがソート順で前に来る（昇順）となります。
				return Long.compare(this.id,otherTodo.id);
			}
		}
	}
}
