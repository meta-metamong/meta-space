INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('100', NULL, '스포츠시설');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('101', '100', '배드민턴');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('102', '100', '풋살');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('103', '100', '클라이밍');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('104', '100', '탁구장');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('105', '100', '당구장');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('106', '100', '농구장');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('200', NULL, '레크리에이션시설');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('201', '200', '강당');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('202', '200', '운동장');

COMMIT;