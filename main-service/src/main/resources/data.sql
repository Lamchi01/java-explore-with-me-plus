DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;
DELETE FROM locations;

ALTER TABLE events ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 1;
ALTER TABLE locations ALTER COLUMN id RESTART WITH 1;