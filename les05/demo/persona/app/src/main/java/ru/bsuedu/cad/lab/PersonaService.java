package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class PersonaService {
    final private PersonaDao personaDao;

    public PersonaService(@Qualifier("personaDaoJdbcTemplate") PersonaDao personaDao) {
        this.personaDao = personaDao;
    }

    Persona getPersonaById(Long id){
        return personaDao.getPersonaById(id);
    }
}
