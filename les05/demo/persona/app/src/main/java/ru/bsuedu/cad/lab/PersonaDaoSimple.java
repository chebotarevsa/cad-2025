package ru.bsuedu.cad.lab;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("personaDaoSimple")
public class PersonaDaoSimple implements PersonaDao{

    private static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    final private DataSource dataSource;
    

    public PersonaDaoSimple(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Persona getPersonaById(Long id) {

        try {
           var connection =   dataSource.getConnection();
           var statement = connection.prepareStatement("SELECT * FROM personas WHERE id = ?");
           statement.setLong(1, id);
           var resultSet = statement.executeQuery();
           LOGGER.info(resultSet.toString());
           while (resultSet.next()) {
            Long personaId = resultSet.getLong(1);
            String name = resultSet.getString(2);
            String arcana = resultSet.getString(3);
            int level = resultSet.getInt(4);
            int strength = resultSet.getInt(5);
            int magic = resultSet.getInt(6);
            int endurance = resultSet.getInt(7);
            int agility = resultSet.getInt(8);
            int luck = resultSet.getInt(9);
            long characterId = resultSet.getLong(10);

            return new Persona(personaId, name,arcana, level,strength,magic,endurance,agility,luck, characterId);
        }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return new Persona();

    }
}
