
CREATE TABLE IF NOT EXISTS `spring_session` (
	`PRIMARY_ID` CHAR(36) NOT NULL COLLATE 'latin1_swedish_ci',
	`SESSION_ID` CHAR(36) NOT NULL COLLATE 'latin1_swedish_ci',
	`CREATION_TIME` BIGINT(20) NOT NULL,
	`LAST_ACCESS_TIME` BIGINT(20) NOT NULL,
	`MAX_INACTIVE_INTERVAL` INT(11) NOT NULL,
	`EXPIRY_TIME` BIGINT(20) NOT NULL,
	`PRINCIPAL_NAME` VARCHAR(100) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	PRIMARY KEY (`PRIMARY_ID`) USING BTREE,
	UNIQUE INDEX `SPRING_SESSION_IX1` (`SESSION_ID`) USING BTREE,
	INDEX `SPRING_SESSION_IX2` (`EXPIRY_TIME`) USING BTREE,
	INDEX `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
ROW_FORMAT=DYNAMIC
;

CREATE TABLE IF NOT EXISTS `spring_session_attributes` (
	`SESSION_PRIMARY_ID` CHAR(36) NOT NULL COLLATE 'latin1_swedish_ci',
	`ATTRIBUTE_NAME` VARCHAR(200) NOT NULL COLLATE 'latin1_swedish_ci',
	`ATTRIBUTE_BYTES` BLOB NOT NULL,
	PRIMARY KEY (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`) USING BTREE,
	CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON UPDATE RESTRICT ON DELETE CASCADE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
ROW_FORMAT=DYNAMIC
;

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

INSERT INTO lobby(privacy_status, name, password, available, version) VALUES("FRIENDS", "TestLobby", "$2a$10$u6xox6e25F7.jBnLsOKbceJ5Y8zkKoqHKPuf34bqxXgMgrpG6QT12", 1, 0);

INSERT INTO player_details(color, has_selected_blocks, playing, position, ready, is_lobby_owner, isaicontrolled, score, username, lobby_id, version) VALUES(0,0,0,1,1,1,0,0,"ManhattanAdmin",1,0);
INSERT INTO player_details(color, has_selected_blocks, playing, position, ready, is_lobby_owner, isaicontrolled, score, username, lobby_id, version) VALUES(1,0,0,2,1,0,0,0,"ManhattanAdmin2",1,0);
INSERT INTO player_details(color, has_selected_blocks, playing, position, ready, is_lobby_owner, isaicontrolled, score, username, lobby_id, version) VALUES(2,0,0,3,1,0,0,0,"ManhattanAdmin3",1,0);
INSERT INTO player_details(color, has_selected_blocks, playing, position, ready, is_lobby_owner, isaicontrolled, score, username, lobby_id, version) VALUES(3,0,0,4,1,0,0,0,"ManhattanAdmin4",1,0);

