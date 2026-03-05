-- V1__init_schema.sql
-- Initial schema for User Management Service
-- Author: Giuseppe Cavone

-- ============================================================
-- USERS table
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    username        VARCHAR(50)     NOT NULL,
    email           VARCHAR(255)    NOT NULL,
    codice_fiscale  VARCHAR(16)     NOT NULL,
    nome            VARCHAR(100)    NOT NULL,
    cognome         VARCHAR(100)    NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE'
                                    CHECK (status IN ('ACTIVE', 'DISABLED', 'DELETED')),
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100)
);

-- Unique constraints
-- Email is globally unique (even across deleted users, since it is used as identity)
CREATE UNIQUE INDEX uq_users_email_active
    ON users (LOWER(email))
    WHERE status != 'DELETED';

-- Username uniqueness among non-deleted users only
CREATE UNIQUE INDEX uq_users_username_active
    ON users (LOWER(username))
    WHERE status != 'DELETED';

-- Codice Fiscale uniqueness among non-deleted users only
-- This allows reusing a CF if the original holder is soft-deleted (edge case)
CREATE UNIQUE INDEX uq_users_cf_active
    ON users (UPPER(codice_fiscale))
    WHERE status != 'DELETED';

-- Performance indexes
CREATE INDEX idx_users_status      ON users (status);
CREATE INDEX idx_users_created_at  ON users (created_at DESC);
CREATE INDEX idx_users_search      ON users (LOWER(username), LOWER(email), LOWER(nome), LOWER(cognome));

-- ============================================================
-- USER_ROLES join table
-- ============================================================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role    VARCHAR(20) NOT NULL
                        CHECK (role IN ('OWNER', 'OPERATOR', 'MAINTAINER', 'DEVELOPER', 'REPORTER')),
    PRIMARY KEY (user_id, role)
);

CREATE INDEX idx_user_roles_user_id ON user_roles (user_id);

-- ============================================================
-- updated_at auto-update trigger
-- ============================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();