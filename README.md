# re_todoapp

# 目的
今回のプロジェクトは、個人的な技術開発と、転職活動用のポートフォリオとして作成したものです。

# 概要
re_todoappは、日々のタスクを効率的に管理するためのシンプルなタスク管理アプリケーションです。
プロジェクト名の "(re)" は、再度書き直したことを意味します。

# デモ
https://github.com/user-attachments/assets/1578e7f5-aaea-49af-8bb5-8600db4785f7

# 主な機能
- CRUD機能（登録、参照、更新、削除）
- 入力値の検証(バリデーション機能)
- タスクの検索機能
- タスクの戻り機能（完了したタスクを未完了に戻す）
- 日付優先機能（登録した日付の前後で並び順が変化）

# 技術的特徴
-   **言語**: Java
-   **フレームワーク**: Spring Boot
-   **データベース**: MySQL
-   **ビルドツール**: Maven

# 機能要件
- spring-boot-starter-web: Webアプリケーションを開発するための基本的な機能をつけるため。
- spring-boot-starter-thymeleaf: HTMLをテンプレートとして使うため。
- spring-boot-starter-validation: 入力されたデータが正しい形式かどうかをチェックするため。
- mybatis-spring-boot-starter 3.0.3: データベースと連携するためまた、JavaのコードからSQL文を扱いやすくするため。
- mysql-connector-j: MySQLデータベースに接続するため。
- lombok: getterやsetterなどのコードを自動生成してくれるため。

# インストール方法
- 必要なもの
    - Java Development Kit (JDK) 21
    - apache-maven-3.9.11
    - Git
- セットアップ
   (以下のコマンドでプロジェクトをローカルにクローンします。)
  - https://github.com/dai-inoue/re_todoapp.git
- ビルド方法
  - cd re_todoapp <br>(プロジェクトのディレクトリに移動します)
  - mvn packageプロジェクトをビルドします。<br>(このコマンドが成功すると、`target` フォルダ内に `re_todoapp-0.0.1-SNAPSHOT.jar` という実行可能なファイルが生成されます。)
- 起動方法
  - java -jar target/re_todoapp-0.0.1-SNAPSHOT.jar](http://localhost:8080)

# 作成者

* 作成者: 井上
* 貢献者: 坂本
* 所属: ジャパニアス株式会社
* GItHubプロフィール: https://github.com/dai-inoue
<div align="center">
  <img src="https://github.com/user-attachments/assets/e876c492-cd59-42f0-9352-e97a0e8b4ffa" width="200" alt="プロフィール画像">
</div>

#ライセンス
このプロジェクトは、[**MITライセンス**](https://opensource.org/licenses/MIT)に基づいてライセンスされてる。詳細については、[LICENSE](LICENSE)ファイルを参照してください。
