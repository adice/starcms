INSERT INTO `bg_role` (id, title, name, priority, state) VALUES (1, 'ROLE_admin', '管理员', 1, 1);
INSERT INTO `bg_role` (id, title, name, priority, state) VALUES (2, 'ROLE_entry', '录入人员', 2, 1);
INSERT INTO `bg_role` (id, title, name, priority, state) VALUES (3, 'ROLE_audit', '审核人员', 3, 1);
COMMIT;
-- INSERT INTO `bg_user` (id, name, password, real_name, regist_time, regist_ip, login_count, data_range, state)
--  VALUES (1, 'admin', '{noop}123456', 'administrator', '2022-7-26', '127.0.0.1', 0, 1, 1);
 INSERT INTO `bg_user` (id, name, password, real_name, regist_time, regist_ip, login_count, data_range, state)
 VALUES (1, 'admin', '{bcrypt}$2a$10$sv.ZgyVW3AANy.JB6GyQ/u0ibtaPDm7mGvSyu.V7GMjI/LqyrPMQW', 'administrator', '2022-7-26', '127.0.0.1', 0, 1, 1);
--## $2a$10$sv.ZgyVW3AANy.JB6GyQ/u0ibtaPDm7mGvSyu.V7GMjI/LqyrPMQW
COMMIT;
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (1, '书籍', 'book', 1, null, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (2, '图片', 'pic', 2, null, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (3, '拓片', 'rubbings', 3, null, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (4, '音频', 'audio', 4, null, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (5, '视频', 'video', 5, null, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (6, '3D模型', '3d', 6, null, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (7, '360全景', 'allscene', 7, null, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (8, '壁画', 'mural', 1, 2, 1);
INSERT INTO `ct_channel` (id, title, path, priority, parent_id, state) VALUES (9, '年画', 'painting', 2, 2, 1);
COMMIT;