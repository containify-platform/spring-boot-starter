CREATE TABLE visits (
    id bigserial PRIMARY KEY,
    path text NOT NULL,
    visited_at timestamptz NOT NULL DEFAULT now()
);
