-- Création de la table "difficulty"
CREATE TABLE difficulty (
                            id SERIAL PRIMARY KEY,
                            label VARCHAR(255) NOT NULL,
                            lives INT NOT NULL,
                            timer_limit INT NOT NULL
);

-- Création de la table "game"
CREATE TABLE game (
                      id SERIAL PRIMARY KEY,
                      difficulty_id INT NOT NULL,
                      topic VARCHAR(255) NOT NULL,
                      lives INT NOT NULL,
                      timer_limit INT NOT NULL,
                      final_status INT DEFAULT 0, -- PENDING par défaut
                      actual_status INT DEFAULT 0, -- PENDING par défaut
                      FOREIGN KEY (difficulty_id) REFERENCES difficulty(id) ON DELETE CASCADE
);

-- Création de la table "action"
CREATE TABLE action (
                        id SERIAL PRIMARY KEY,
                        value_id INT NOT NULL,
                        label VARCHAR(255) NOT NULL,
                        action_status INT DEFAULT 0, -- NA par défaut (ENUM ordinals)
                        game_id INT NOT NULL,
                        FOREIGN KEY (game_id) REFERENCES game(id) ON DELETE CASCADE
);

INSERT INTO difficulty (label, lives, timer_limit) VALUES
                                                       ('Easy', 5, 60000),
                                                       ('Medium', 3, 45000),
                                                       ('Hard', 1, 30000);

INSERT INTO game (difficulty_id, topic, lives, timer_limit, final_status, actual_status) VALUES
                                                                                             (1, 'game_topic_easy', 5, 60000, 0, 1), -- PENDING / STARTED
                                                                                             (2, 'game_topic_medium', 3, 45000, 0, 1), -- PENDING / STARTED
                                                                                             (3, 'game_topic_hard', 1, 30000, 0, 1); -- PENDING / STARTED


INSERT INTO action (value_id, label, action_status, game_id) VALUES
                                                                 (1, 'Appuie !', 0, 1), -- NA status pour le jeu 1
                                                                 (2, 'Penche à Droite !', 0, 1), -- NA status pour le jeu 1
                                                                 (3, 'Penche à Gauche !', 0, 2), -- NA status pour le jeu 2
                                                                 (4, 'Tape 2 fois dans tes Mains !', 0, 2), -- NA status pour le jeu 2
                                                                 (5, 'Tourne le Joystick à Droite !', 0, 3); -- NA status pour le jeu 3
