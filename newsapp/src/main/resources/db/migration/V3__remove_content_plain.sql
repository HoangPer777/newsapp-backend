-- Đảm bảo extension unaccent được tạo (nếu chưa có)
-- Remove unnecessary columns/tables introduced by earlier migrations.
-- Keep only the columns used by the Article JPA entity: title, content, summary,
-- category, slug, author_id, created_at, updated_at, view_count, like_count

-- Drop fulltext index and generated tsv column (may have been created earlier)
DROP INDEX IF EXISTS idx_articles_tsv;
ALTER TABLE articles DROP COLUMN IF EXISTS tsv;

-- Drop legacy HTML/plain content columns
ALTER TABLE articles DROP COLUMN IF EXISTS content_html;
ALTER TABLE articles DROP COLUMN IF EXISTS content_plain;

-- Drop published_at if you don't use it in the JPA entity
ALTER TABLE articles DROP COLUMN IF EXISTS published_at;

-- If there was a separate stats table, remove it (we now use view_count/like_count in articles)
DROP TABLE IF EXISTS article_stats;

-- Ensure the desired columns exist (safe no-op if already present)
ALTER TABLE articles
  ADD COLUMN IF NOT EXISTS category TEXT,
  ADD COLUMN IF NOT EXISTS content TEXT,
  ADD COLUMN IF NOT EXISTS summary TEXT,
  ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ,
  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ,
  ADD COLUMN IF NOT EXISTS view_count INTEGER DEFAULT 0,
  ADD COLUMN IF NOT EXISTS like_count INTEGER DEFAULT 0;

-- Optionally, populate created_at/updated_at/view_count/like_count from existing data
UPDATE articles SET created_at = COALESCE(created_at, now()) WHERE created_at IS NULL;
UPDATE articles SET updated_at = COALESCE(updated_at, created_at) WHERE updated_at IS NULL;
