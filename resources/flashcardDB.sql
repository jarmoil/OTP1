DROP DATABASE flashcardDB;
CREATE DATABASE flashcardDB;
USE flashcardDB;

CREATE TABLE user_accounts (
                               user_id INT NOT NULL AUTO_INCREMENT,
                               user_name VARCHAR(20) NOT NULL,
                               user_password VARCHAR(50) NOT NULL,
                               role ENUM('student', 'teacher') NOT NULL,
                               PRIMARY KEY (user_id)
);

CREATE TABLE sets (
                      sets_id INT NOT NULL AUTO_INCREMENT,
                      user_id INT NOT NULL,
                      description TEXT NOT NULL,
                      sets_correct_percentage INT,
                      PRIMARY KEY (sets_id),
                      FOREIGN KEY (user_id) REFERENCES user_accounts(user_id)
                          ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE flashcards (
                            flashcard_id INT NOT NULL AUTO_INCREMENT,
                            sets_id INT NOT NULL,
                            times_answered INT DEFAULT 0,
                            times_correct INT DEFAULT 0,
                            question TEXT NOT NULL,
                            answer TEXT NOT NULL,
                            PRIMARY KEY (flashcard_id),
                            FOREIGN KEY (sets_id) REFERENCES sets(sets_id)
                                ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE statistics (
                            stats_id INT NOT NULL AUTO_INCREMENT,
                            user_id INT NOT NULL,
                            sets_id INT NOT NULL,
                            stats_correct_percentage INT,
                            PRIMARY KEY (stats_id),
                            FOREIGN KEY (user_id) REFERENCES user_accounts(user_id)
                                ON DELETE CASCADE ON UPDATE CASCADE,
                            FOREIGN KEY (sets_id) REFERENCES sets(sets_id)
                                ON DELETE CASCADE ON UPDATE CASCADE
);
