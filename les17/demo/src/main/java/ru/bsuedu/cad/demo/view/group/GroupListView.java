package ru.bsuedu.cad.demo.view.group;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import ru.bsuedu.cad.demo.entity.Group;
import ru.bsuedu.cad.demo.view.main.MainView;


@Route(value = "groups", layout = MainView.class)
@ViewController(id = "Group_.list")
@ViewDescriptor(path = "group-list-view.xml")
@LookupComponent("groupsDataGrid")
@DialogMode(width = "64em")
public class GroupListView extends StandardListView<Group> {
}