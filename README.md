# QC Logbook

Temperature Soak Inspection Management System

## 1. アプリ概要（QC Logbookとは）
- 温度ソーク試験のログを管理するためのWebアプリです。
- CSVで取得した温度データを取り込み、試験結果を判定・記録します。
- 現場の試験記録を標準化し、再現性と追跡性を高めることが目的です。

## 2. 想定ユースケース
- 品質保証部門での温度ソーク試験の記録・確認
- 試験担当者がCSVをアップロードし、結果を即時に確認
- 試験結果の履歴を検索・参照

## 3. 試験仕様（前提条件）
- 高温: 90℃ / 常温: 23℃ / 低温: -10℃
- 各2時間ソーク
- 規定電圧12V到達が t=0ms
- 記録範囲: 前後1分（-60000?+60000ms）
- サンプリング: 20ms

## 4. 温度判定仕様
- 許容範囲: 目標±5℃
- 判定条件
  - PASS: 0ms温度が許容範囲内
  - FAIL: 0ms温度が許容範囲外
  - PENDING: 試験結果の確定前またはデータ不足
- 代表値: 0ms温度

## 5. 機能一覧（MVP）
- 起動確認用のヘルスチェック画面
- CSV取り込み（予定）
- 試験結果の判定（予定）
- 試験結果の一覧表示（予定）

## 6. CSV仕様
- ヘッダー: `offsetMs,tempC`
- 例:
```csv
offsetMs,tempC
-60000,89.8
-59980,89.9
...
0,90.1
...
60000,90.0
```

## 7. 画面構成（MVP）
- /health（起動確認）
- 試験一覧（予定）
- 試験詳細（予定）
- CSVアップロード（予定）

## 8. 技術スタック
- Java 17
- Spring Boot 3.x
- Spring MVC
- Thymeleaf
- Spring Data JPA
- H2 Database
- Maven
- Bootstrap 5

## 9. ローカル起動手順
```
cd qc-logbook
mvn spring-boot:run
```
- 起動後は `http://localhost:8080/health` で動作確認

## 10. ドメイン用語
- Inspection: 試験（検査）単位
- SoakSession: ソーク試験セッション（温度帯ごとの試験）
- Trigger: 規定電圧到達イベント（t=0ms）
- offsetMs: t=0ms からの相対時間（ms）

## 11. 今後の拡張予定
- CSVアップロードとバリデーション
- 判定ロジックの実装と可視化
- 試験履歴の検索・フィルタ
- エクスポート（CSV/PDF）
- 認証・権限管理

## 12. 画面キャプチャ（Screenshots）
- （ここに画面キャプチャを追加予定）
- 例: ![Health](docs/images/health.png)

## 13. ER図（ERD）
- （ここにER図を追加予定）
- 例: ![ERD](docs/images/erd.png)