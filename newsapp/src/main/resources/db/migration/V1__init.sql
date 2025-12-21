CREATE EXTENSION IF NOT EXISTS unaccent;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  email TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  display_name TEXT,
  avatar_url TEXT,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS articles (
  id BIGSERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  slug TEXT NOT NULL UNIQUE,
  author_id BIGINT NOT NULL REFERENCES users(id),
  content_html TEXT NOT NULL,
  content_plain TEXT NOT NULL,
  published_at TIMESTAMPTZ,
  -- Use a normal tsvector column instead of a GENERATED column because
  -- the generation expression used unaccent(...) which is not considered
  -- immutable by PostgreSQL and causes "generation expression is not immutable".
  tsv tsvector
);
CREATE INDEX IF NOT EXISTS idx_articles_tsv ON articles USING GIN (tsv);
CREATE INDEX IF NOT EXISTS idx_articles_published_at ON articles(published_at DESC);

CREATE TABLE IF NOT EXISTS article_stats (
  article_id BIGINT PRIMARY KEY REFERENCES articles(id) ON DELETE CASCADE,
  views BIGINT NOT NULL DEFAULT 0,
  likes BIGINT NOT NULL DEFAULT 0,
  comments_count BIGINT NOT NULL DEFAULT 0,
  hot_score DOUBLE PRECISION NOT NULL DEFAULT 0
);
