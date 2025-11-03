# re_todoapp

# 目的
今回のプロジェクトは、個人的な技術開発と、転職活動用のポートフォリオとして作成したものです。

# 概要
re_todoappは、日々のタスクを効率的に管理するためのシンプルなタスク管理アプリケーションです。<br>プロジェクト名の "(re)" は、再度書き直したことを意味します。

# デモ
https://github.com/user-attachments/assets/1578e7f5-aaea-49af-8bb5-8600db4785f7

# 主な機能
- CRUD機能（登録、参照、更新、削除）
- 入力値の検証(バリデーション機能)
- タスクの検索機能
- タスクの戻り機能（完了したタスクを未完了に戻す）
- 日付優先機能（登録した日付の前後で並び順が変化）
- 期限切れタスクの自動状態遷移機能

# 技術的特徴
- **言語**: Java
- **フレームワーク**: Spring Boot
- **データベース**: MySQL
- **ビルドツール**: Maven

# 使用フレームワーク・ライブラリー
- spring-boot-starter-web: Webアプリケーションを開発するための基本的な機能をつけるため。
- spring-boot-starter-thymeleaf: HTMLをテンプレートとして使うため。
- spring-boot-starter-validation: 入力されたデータが正しい形式かどうかをチェックするため。
- mybatis-spring-boot-starter 3.0.3: データベースと連携するためまた、JavaのコードからSQL文を扱いやすくするため。
- mysql-connector-j: MySQLデータベースに接続するため。
- lombok: getterやsetterなどのコードを自動生成してくれるため。
- quartz: バッチ処理などのスケジューリングのため。

# インストール方法
- 必要なもの
    - Git
    - Java Development Kit (JDK) 21
    - apache-maven-3.9.11
    - MySQL Workbench 8.0 CE
- セットアップ
   (以下のコマンドでプロジェクトをローカルにクローンします。)
  - git clone https://github.com/dai-inoue/re_todoapp.git
- ビルド方法
  - cd re_todoapp <br>(プロジェクトのディレクトリに移動します)
  - mvn packageプロジェクトをビルドします。<br>(このコマンドが成功すると、`target` フォルダ内に `re_todoapp-0.0.1-SNAPSHOT.jar` という実行可能なファイルが生成されます。)
- 起動方法
  - java -jar target/re_todoapp-0.0.1-SNAPSHOT.jar](http://localhost:8080)

- MySQL インストール方法【Windows 11 Homeの場合】
  - インストーラのダウンロード
      - 以下のMySQL公式ウェブサイトにアクセス
      - MySQL Installer for Windows
      - ページ下部にある「Download」ボタンをクリックして、インストーラをダウンロード
        
  - インストーラの実行
      - ダウンロードした.exeファイルを実行
      - **「Choose a Setup Type」**という画面が表示されたら、「Developer Default」を選択して、Nextをクリック
      - 要なコンポーネントがいくつかリストアップされるので、そのままNextをクリック

   - rootユーザーのパスワード設定
      - インストール中に **「Authentication Method」** という画面で、rootユーザーのパスワード設定を行う。
      - **「MySQL Root Password」** の欄に、任意のパスワードを入力。このパスワードはMySQLに接続する際に必要になるため、<br> 必ずメモ必要です
      - パスワードの強度を確認し、問題なければNextをクリックしてインストールを続行します
  
   - インストール完了
      - すべてのコンポーネントのインストールが完了したら、Finishをクリックしてセットアップを終了.
  
  
# 作成者

* 作成者: 井上
* 貢献者: 坂本
* 所属: ジャパニアス株式会社
* GItHubプロフィール: https://github.com/dai-inoue
* GItHubプロフィール: https://github.com/SS0227
<div align="center">
    <img width="320" alt="スクリーンショット 2025-09-21 213107" src="https://github.com/user-attachments/assets/07830a09-8d0b-4c42-8c2e-4fc6ec09374f" />
    <img width="312" height="423" alt="image" src="https://github.com/user-attachments/assets/7db4b64b-d04d-4b22-9996-c031f0c0faa8" />
</div>

# ライセンス
このプロジェクトは、[**MITライセンス**](https://opensource.org/licenses/MIT)に基づいている。詳細については、[LICENSE](LICENSE)ファイルを参照してください。
