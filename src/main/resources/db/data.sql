
-- Password 9l68{FoG|H#9qEI]

INSERT INTO users(username, password, enabled, creation_date) values("ManhattanAdmin", "$2a$10$/8vKyAcD4KzWPyOIPG4kMezPiZO7feWxMC0FADuca/X8s8oJcuYvu", 1, "2024-01-15 14:30:00");
INSERT INTO users(username, password, enabled, creation_date) values("ManhattanAdmin2", "$2a$10$/8vKyAcD4KzWPyOIPG4kMezPiZO7feWxMC0FADuca/X8s8oJcuYvu", 1, "2024-01-15 14:30:01");
INSERT INTO users(username, password, enabled, creation_date) values("ManhattanAdmin3", "$2a$10$/8vKyAcD4KzWPyOIPG4kMezPiZO7feWxMC0FADuca/X8s8oJcuYvu", 1, "2024-01-15 14:30:02");
INSERT INTO users(username, password, enabled, creation_date) values("ManhattanAdmin4", "$2a$10$/8vKyAcD4KzWPyOIPG4kMezPiZO7feWxMC0FADuca/X8s8oJcuYvu", 1, "2024-01-15 14:30:03");

INSERT INTO authorities(username, authority) values(1, "Admin");
INSERT INTO authorities(username, authority) values(2, "Admin");
INSERT INTO authorities(username, authority) values(3, "Admin");
INSERT INTO authorities(username, authority) values(4, "Admin");

INSERT INTO friendships(accepted, user1_id, user2_id) values(1, 1, 2);
INSERT INTO friendships(accepted, user1_id, user2_id) values(1, 3, 1);

INSERT INTO player_details(color, playing, position, ready, score, username, version) VALUES(0,0,1,0,0,"ManhattanAdmin",0);

INSERT INTO lobby(privacy_status, name, password, owner_id, version) VALUES("FRIENDS", "TestLobby", "$2a$10$u6xox6e25F7.jBnLsOKbceJ5Y8zkKoqHKPuf34bqxXgMgrpG6QT12", 1, 0);
