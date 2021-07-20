package com.example.application.views.user;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserRepository;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A Designer generated component for the user-management-view template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("user-management-view")
@JsModule("./views/user/user-management-view.ts")
@CssImport("./views/user/user-management-view.css")
@PageTitle("Users management")
public class UserManagementView extends LitTemplate {

    private GridListDataView<User> gridListDataView;
    private final UserRepository userRepository;

    private Grid.Column<User> idColumn;
    private Grid.Column<User> nameColumn;
    private Grid.Column<User> adminColumn;
    private Grid.Column<User> phoneColumn;
    private Grid.Column<User> emailColumn;
    private Grid.Column<User> activeColumn;


    @Id("gridLayout")
    private Element gridLayout;
    @Id("usersGrid")
    private Grid<User> grid;
    @Id("controlPanelLayout")
    private Element controlPanelLayout;
    @Id("createUserBtn")
    private Button createUserBtn;

    /**
     * Creates a new UserManagementView.
     */
    public UserManagementView(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        createGrid();
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        //grid.setHeight("100%");

        List<User> sensors = getUsers();
        grid.addItemClickListener(event -> {
            grid.select(event.getItem());
        });

        gridListDataView = grid.setItems(sensors);
        GridContextMenu<User> contextMenu = grid.addContextMenu();

    }

    private List<User> getUsers() {
        return userRepository.findAll();
    }

    private void addColumnsToGrid() {
        createIdColumn();
        createNameColumn();
        createAdminColumn();
        createPhoneColumn();
        createEmailColumn();
        createActiveColumn();
    }

    private void createIdColumn() {
        idColumn = grid.addColumn(User::getId, "id").setHeader("ID").setWidth("100px").setFlexGrow(0);
    }


    private void createNameColumn() {
        nameColumn = grid.addColumn(User::getFullName, "name").setHeader("Full name")
                .setComparator(User::getFullName).setWidth("300px").setFlexGrow(0);
    }


    private void createAdminColumn() {
        adminColumn = grid.addComponentColumn(user -> {
            Span span = new Span();
            span.setText(user.getAdmin() ? "Admin" : "User");
            span.getElement().setAttribute("theme", user.getAdmin() ? "badge primary" : "badge success primary");
            span.setWidth("115px");
            return span;
        }).setHeader("Type").setWidth("150px").setFlexGrow(0);;
    }

    private void createPhoneColumn() {
        phoneColumn = grid.addColumn(User::getPhone, "phone").setHeader("Phone")
                .setComparator(User::getPhone).setWidth("200px").setFlexGrow(0);
    }

    private void createEmailColumn() {
        emailColumn = grid.addColumn(User::getEmail, "email").setHeader("Email")
                .setComparator(User::getEmail).setWidth("300px").setFlexGrow(0);
    }

    private void createActiveColumn() {
        activeColumn = grid.addComponentColumn(user -> {
            Span span = new Span();
            span.setText(user.getActive() ? "Active" : "Inactive");
            span.getElement().setAttribute("theme", user.getActive() ? "badge success secondary" : "badge error secondary");
            span.setWidth("115px");
            return span;
        }).setHeader("Active").setWidth("150px").setFlexGrow(0);;
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField idFilter = getIdFilter();
        filterRow.getCell(idColumn).setComponent(idFilter);

        TextField nameFilter = getNameFilter();
        filterRow.getCell(nameColumn).setComponent(nameFilter);

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.setItems(Arrays.asList("Admin", "User"));
        typeFilter.setPlaceholder("Filter");
        typeFilter.setClearButtonVisible(true);
        typeFilter.setWidth("100%");
        typeFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(user -> areTypesEqual(user, typeFilter)));
        filterRow.getCell(adminColumn).setComponent(typeFilter);

        TextField emailFilter = getEmailFilter();
        filterRow.getCell(emailColumn).setComponent(emailFilter);

        TextField phoneFilter = getPhoneFilter();
        filterRow.getCell(phoneColumn).setComponent(phoneFilter);

        ComboBox<String> activeFilter = new ComboBox<>();
        activeFilter.setItems(Arrays.asList("Active", "Inactive"));
        activeFilter.setPlaceholder("Filter");
        activeFilter.setClearButtonVisible(true);
        activeFilter.setWidth("100%");
        activeFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(user -> areActiveEqual(user, activeFilter)));
        filterRow.getCell(activeColumn).setComponent(activeFilter);
    }

    private TextField getNameFilter() {
        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Filter");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setWidth("100%");
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(user -> StringUtils.containsIgnoreCase(user.getFullName(), nameFilter.getValue())));
        return nameFilter;
    }

    private TextField getIdFilter() {
        TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> gridListDataView.addFilter(
                client -> StringUtils.containsIgnoreCase(Integer.toString(client.getId()), idFilter.getValue())));
        return idFilter;
    }

    private TextField getEmailFilter() {
        TextField emailFilter = new TextField();
        emailFilter.setPlaceholder("Filter");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setWidth("100%");
        emailFilter.setValueChangeMode(ValueChangeMode.EAGER);
        emailFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(user -> StringUtils.containsIgnoreCase(user.getEmail(), emailFilter.getValue())));
        return emailFilter;
    }

    private TextField getPhoneFilter() {
        TextField phoneFilter = new TextField();
        phoneFilter.setPlaceholder("Filter");
        phoneFilter.setClearButtonVisible(true);
        phoneFilter.setWidth("100%");
        phoneFilter.setValueChangeMode(ValueChangeMode.EAGER);
        phoneFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(user -> StringUtils.containsIgnoreCase(user.getPhone(), phoneFilter.getValue())));
        return phoneFilter;
    }

    private boolean areTypesEqual(User user, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return Objects.equals(statusFilterValue, user.getAdmin() ? "Admin" : "User");
        }
        return true;
    }

    private boolean areActiveEqual(User user, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return Objects.equals(statusFilterValue, user.getActive() ? "Active" : "Inactive");
        }
        return true;
    }
}
