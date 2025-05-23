package ru.bsuedu.cad.demo.security;

import io.jmix.security.model.RowLevelBiPredicate;
import io.jmix.security.model.RowLevelPolicyAction;
import io.jmix.security.role.annotation.PredicateRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;
import org.springframework.context.ApplicationContext;
import ru.bsuedu.cad.demo.entity.Student;

@RowLevelRole(name = "StudentViewRow", code = StudentViewRowRole.CODE)
public interface StudentViewRowRole {
    String CODE = "student-view-row";

    @PredicateRowLevelPolicy(entityClass = Student.class, actions = RowLevelPolicyAction.READ)
    default RowLevelBiPredicate<Student, ApplicationContext> studentPredicate() {
        return (student, applicationContext) -> {
            return true;
        };
    }
}