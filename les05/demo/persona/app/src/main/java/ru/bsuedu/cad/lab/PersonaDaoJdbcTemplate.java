package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("personaDaoJdbcTemplate")
public class PersonaDaoJdbcTemplate implements PersonaDao {

    final private JdbcTemplate jdbcTemplate;
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


     public PersonaDaoJdbcTemplate(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
         this.jdbcTemplate = jdbcTemplate;
         this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
     }

    // Добавление персоны в базу данных
    public void addPersona(Persona persona) {
        String sql = "INSERT INTO personas (name, arcana, level, strength, magic, endurance, agility, luck, character_id) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, persona.getName(), persona.getArcana(), persona.getLevel(), persona.getStrength(),
                persona.getMagic(), persona.getEndurance(), persona.getAgility(), persona.getLuck(),
                persona.getCharacterId());
    }

    // Получение персоны по ID
    @Override
    public Persona getPersonaById(Long id) {
        String sql = "SELECT * FROM personas WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] { id }, personaRowMapper());
    }

    public Persona getPersonaByIdNamed(Long id) {
        String sql = "SELECT * FROM personas WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, personaRowMapper());
    }

    // Получение всех персон
    public List<Persona> getAllPersonas() {
        String sql = "SELECT * FROM personas";
        return jdbcTemplate.query(sql, personaRowMapper());
    }

    // Обновление данных персоны
    public void updatePersona(Persona persona) {
        String sql = "UPDATE personas SET name = ?, arcana = ?, level = ?, strength = ?, magic = ?, endurance = ?, " +
                "agility = ?, luck = ?, character_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, persona.getName(), persona.getArcana(), persona.getLevel(), persona.getStrength(),
                persona.getMagic(), persona.getEndurance(), persona.getAgility(), persona.getLuck(),
                persona.getCharacterId(), persona.getId());
    }


    public void updatePersonaNamed(Persona persona) {
        String sql = "UPDATE personas SET name = :name, arcana = ?, level = ?, strength = ?, magic = ?, endurance = ?, " +
                "agility = ?, luck = ?, character_id = ? WHERE id = ?";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", persona.getName())
                .addValue("arcana", persona.getArcana())
                .addValue("strength", persona.getStrength())
                .addValue("magic", persona.getMagic())
                .addValue("agility", persona.getAgility())
                .addValue("luck", persona.getLuck())
                .addValue("character_id", persona.getCharacterId())
                .addValue("id", persona.getId());
        namedParameterJdbcTemplate.update(sql, params);
    }

    // Удаление персоны по ID
    public void deletePersona(Long id) {
        String sql = "DELETE FROM personas WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // RowMapper для преобразования результата запроса в объект Persona
    private RowMapper<Persona> personaRowMapper() {
        return (rs, rowNum) -> new Persona(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("arcana"),
                rs.getInt("level"),
                rs.getInt("strength"),
                rs.getInt("magic"),
                rs.getInt("endurance"),
                rs.getInt("agility"),
                rs.getInt("luck"),
                rs.getLong("character_id"));
    }
}