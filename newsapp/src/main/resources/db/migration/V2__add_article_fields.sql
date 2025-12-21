-- Add missing columns expected by JPA entity (safe if columns already exist)
ALTER TABLE articles
  ADD COLUMN IF NOT EXISTS category TEXT,
  ADD COLUMN IF NOT EXISTS content TEXT,
  ADD COLUMN IF NOT EXISTS summary TEXT,
  ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ,
  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ,
  ADD COLUMN IF NOT EXISTS view_count INTEGER DEFAULT 0,
  ADD COLUMN IF NOT EXISTS like_count INTEGER DEFAULT 0;

-- Try to populate new columns from existing ones where possible
-- Chỉ UPDATE nếu cột content_html tồn tại
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns 
    WHERE table_name = 'articles' AND column_name = 'content_html'
  ) THEN
    UPDATE articles SET content = content_html 
    WHERE content IS NULL AND content_html IS NOT NULL;
  END IF;
END$$;

-- Chỉ UPDATE nếu cột content_plain tồn tại
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns 
    WHERE table_name = 'articles' AND column_name = 'content_plain'
  ) THEN
    UPDATE articles SET summary = left(content_plain, 255) 
    WHERE summary IS NULL AND content_plain IS NOT NULL;
  END IF;
END$$;

UPDATE articles SET created_at = COALESCE(published_at, now()) WHERE created_at IS NULL;
UPDATE articles SET updated_at = created_at WHERE updated_at IS NULL;

-- If article_stats table exists, seed view_count/like_count from it
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'article_stats') THEN
    UPDATE articles a
    SET view_count = s.views,
        like_count = s.likes
    FROM article_stats s
    WHERE a.id = s.article_id;
  END IF;
END$$;
