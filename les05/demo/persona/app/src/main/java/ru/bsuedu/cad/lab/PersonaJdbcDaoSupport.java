package ru.bsuedu.cad.lab;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;


@Repository("PersonaJdbcDaoSupport")
public class PersonaJdbcDaoSupport  extends JdbcDaoSupport implements PersonaDao {

   
    public PersonaJdbcDaoSupport(JdbcTemplate jdbcTemplate) {
         setJdbcTemplate(jdbcTemplate);
     }

    // Получение персоны по ID
    @Override
    public Persona getPersonaById(Long id) {
        String sql = "SELECT * FROM personas WHERE id = ?";
        return getJdbcTemplate().queryForObject(sql, new Object[] { id }, personaRowMapper());
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