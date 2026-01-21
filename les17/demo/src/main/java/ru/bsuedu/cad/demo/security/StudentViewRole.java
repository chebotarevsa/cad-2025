package ru.bsuedu.cad.demo.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;
import ru.bsuedu.cad.demo.entity.Group;
import ru.bsuedu.cad.demo.entity.Student;

@ResourceRole(name = "student-view", code = StudentViewRole.CODE)
public interface StudentViewRole {
    String CODE = "student-view";

    @EntityAttributePolicy(entityClass = Student.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Student.class, actions = EntityPolicyAction.READ)
    void student();

    @EntityAttributePolicy(entityClass = Group.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Group.class, actions = EntityPolicyAction.READ)
    void group();

    @MenuPolicy(menuIds = {"Student.list", "Group_.list"})
    @ViewPolicy(viewIds = {"Student.detail", "Student.list", "Group_.list"})
    void screens();
}