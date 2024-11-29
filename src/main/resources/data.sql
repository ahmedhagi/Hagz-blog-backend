INSERT INTO roles(id,name) VALUES(1,'ROLE_USER');
INSERT INTO roles(id,name) VALUES(2,'ROLE_MODERATOR');
INSERT INTO roles(id,name) VALUES(3,'ROLE_ADMIN');

INSERT INTO topics (id,name) VALUES (1,'News');
INSERT INTO topics (id,name) VALUES (2,'Entertainment');
INSERT INTO topics (id,name) VALUES (3,'Sports');
INSERT INTO topics (id,name) VALUES (4,'Food');
INSERT INTO topics (id,name) VALUES (5,'Gaming');

INSERT INTO tags(id,name) VALUES(1,'global');
INSERT INTO tags(id,name) VALUES(2,'local');
INSERT INTO tags(id,name) VALUES(3,'elections');

INSERT INTO tags(id,name) VALUES(4,'movies');
INSERT INTO tags(id,name) VALUES(5,'TV');
INSERT INTO tags(id,name) VALUES(6,'music');

INSERT INTO tags(id,name) VALUES(7,'basketball');
INSERT INTO tags(id,name) VALUES(8,'soccer');
INSERT INTO tags(id,name) VALUES(9,'football');

INSERT INTO tags(id,name) VALUES(10,'restaurant');
INSERT INTO tags(id,name) VALUES(11,'delivery');
INSERT INTO tags(id,name) VALUES(12,'review');

INSERT INTO tags(id,name) VALUES(13,'single-player');
INSERT INTO tags(id,name) VALUES(14,'multiplayer');

INSERT INTO topic_tags(topic_id,tag_id) VALUES(1,1);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(1,2);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(1,3);

INSERT INTO topic_tags(topic_id,tag_id) VALUES(2,4);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(2,5);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(2,6);

INSERT INTO topic_tags(topic_id,tag_id) VALUES(3,7);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(3,8);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(3,9);

INSERT INTO topic_tags(topic_id,tag_id) VALUES(4,10);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(4,11);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(4,12);

INSERT INTO topic_tags(topic_id,tag_id) VALUES(5,13);
INSERT INTO topic_tags(topic_id,tag_id) VALUES(5,14);







