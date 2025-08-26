BEGIN;

CREATE SEQUENCE IF NOT EXISTS id_sequence_generator
    START WITH 1000
    INCREMENT BY 20
    CACHE 1;

CREATE TABLE IF NOT EXISTS category
(
    id        BIGINT PRIMARY KEY NOT NULL,
    name      VARCHAR(20)        NOT NULL CHECK (LENGTH(name) BETWEEN 3 AND 20),
    parent_id BIGINT,

    CONSTRAINT uk_category_name UNIQUE (name),

    CONSTRAINT fk_parent
        FOREIGN KEY (parent_id)
            REFERENCES category (id)
            ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_category_parent ON category (parent_id);

DROP TRIGGER IF EXISTS category_cycle_trigger ON category;

-- Cyclic references protection
CREATE OR REPLACE FUNCTION check_category_cycle()
    RETURNS TRIGGER AS
$$
DECLARE
    current_id BIGINT := NEW.id;
    current_parent_id  BIGINT := NEW.parent_id;
BEGIN
    WHILE current_parent_id IS NOT NULL
        LOOP
            IF current_parent_id = current_id THEN
                RAISE EXCEPTION 'Cyclic dependency detected';
            END IF;
            SELECT parent_id INTO current_parent_id FROM category WHERE id = current_parent_id;
        END LOOP;
    RETURN NEW;
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER category_cycle_trigger
    BEFORE INSERT OR UPDATE
    ON category
    FOR EACH ROW
EXECUTE FUNCTION check_category_cycle();

COMMIT;
