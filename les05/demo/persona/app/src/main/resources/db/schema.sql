CREATE TABLE game_characters (
    id IDENTITY PRIMARY KEY, -- Уникальный ID персонажа
    name VARCHAR(100) NOT NULL, -- Имя персонажа
    game_title VARCHAR(100) NOT NULL, -- Название игры
    role VARCHAR(50), -- Роль персонажа (например, главный герой)
    level INT CHECK (level >= 1 AND level <= 99), -- Уровень персонажа
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Дата создания записи
);


CREATE TABLE personas (
    id IDENTITY PRIMARY KEY, -- Уникальный ID персоны
    name VARCHAR(100) NOT NULL, -- Имя персоны
    arcana VARCHAR(50) NOT NULL, -- Аркана персоны
    level INT CHECK (level >= 1 AND level <= 99), -- Уровень персоны
    strength INT CHECK (strength >= 1 AND strength <= 99), -- Сила
    magic INT CHECK (magic >= 1 AND magic <= 99), -- Магия
    endurance INT CHECK (endurance >= 1 AND endurance <= 99), -- Выносливость
    agility INT CHECK (agility >= 1 AND agility <= 99), -- Ловкость
    luck INT CHECK (luck >= 1 AND luck <= 99), -- Удача
    character_id INT UNIQUE, -- Внешний ключ на персонажа
    FOREIGN KEY (character_id) REFERENCES game_characters(id) ON DELETE CASCADE -- Связь с персонажем (один к одному)
);