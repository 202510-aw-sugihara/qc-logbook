# DBスキーマ確認メモ

## 確認した観点
- テーブル: `inspections` / `soak_sessions` / `temp_points`
- 外部キー: `soak_sessions.inspection_id` → `inspections.id`
- 外部キー: `temp_points.session_id` → `soak_sessions.id`
- Unique: `temp_points (session_id, offset_ms)`
- Index: `session_id` / `(session_id, offset_ms)` など

## 主要カラム（簡易）
- inspections
  - `id`, `product_name`, `lot_no`, `created_at`
- soak_sessions
  - `id`, `inspection_id`, `env_type`, `judgement`, `target_temp_c`, `target_voltage_v`
- temp_points
  - `id`, `session_id`, `offset_ms`, `temp_c`, `created_at`

## 気づき / 注意点
- `ddl-auto=update` は既存スキーマとの差分があると反映されない場合がある。
- `temp_points` は大量データ想定のため、`points` への `cascade` は付けていない。
- Unique制約により、同一セッション内で `offset_ms` が重複するCSV取込を防げる。