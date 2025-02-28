INSERT INTO game_characters (name, game_title, role, level)
VALUES
('Joker', 'Persona 5', 'Main Protagonist', 99),
('Ryuji Sakamoto', 'Persona 5', 'Party Member', 70),
('Ann Takamaki', 'Persona 5', 'Party Member', 68),
('Makoto Niijima', 'Persona 5', 'Party Member', 69),
('Yusuke Kitagawa', 'Persona 5', 'Party Member', 67);

INSERT INTO personas (name, arcana, level, strength, magic, endurance, agility, luck, character_id)
VALUES
('Arsene', 'Fool', 1, 2, 2, 1, 3, 1, 1), -- Привязка к Joker
('Captain Kidd', 'Chariot', 16, 12, 8, 10, 11, 9, 2), -- Привязка к Ryuji
('Carmen', 'Lovers', 18, 9, 15, 10, 12, 10, 3), -- Привязка к Ann
('Johanna', 'Priestess', 25, 14, 17, 14, 12, 13, 4), -- Привязка к Makoto
('Goemon', 'Emperor', 26, 16, 12, 15, 14, 10, 5); -- Привязка к Yusuke