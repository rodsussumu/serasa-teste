INSERT INTO tb_roles(role_id, name) VALUES(1, 'ROLE_ADMIN') ON CONFLICT(role_id) DO NOTHING
INSERT INTO tb_roles(role_id, name) VALUES(2, 'ROLE_USER') ON CONFLICT(role_id) DO NOTHING