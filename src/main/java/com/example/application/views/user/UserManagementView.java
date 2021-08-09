package com.example.application.views.user;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.example.application.views.user.components.CreateUserForm;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
    private final UserService userService;

    private Grid.Column<User> idColumn;
    private Grid.Column<User> nameColumn;
    private Grid.Column<User> adminColumn;
    private Grid.Column<User> phoneColumn;
    private Grid.Column<User> emailColumn;
    private Grid.Column<User> activeColumn;
    private Grid.Column<User> toolsColumn;

    @Id("gridLayout")
    private Element gridLayout;
    @Id("usersGrid")
    private Grid<User> grid;
    @Id("controlPanelLayout")
    private Element controlPanelLayout;
    @Id("createUserBtn")
    private Button createUserBtn;

    @Value("${adminLogin}")
    private String adminLogin;

    /**
     * Creates a new UserManagementView.
     */
    public UserManagementView(@Autowired UserService userService) {
        this.userService = userService;
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        createGrid();

        createUserBtn.addClickListener(event -> {
            createUser();
        });
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        List<User> sensors = getUsers();
        grid.addItemClickListener(event -> {
            grid.select(event.getItem());
        });

        gridListDataView = grid.setItems(sensors);
    }

    private List<User> getUsers() {
        return userService.findAll();
    }

    private void addColumnsToGrid() {
        createIdColumn();
        createNameColumn();
        createAdminColumn();
        createPhoneColumn();
        createEmailColumn();
        createActiveColumn();
        createToolsColumn();
    }

    private void createIdColumn() {
        idColumn = grid.addColumn(User::getId, "id").setHeader("ID").setWidth("100px").setFlexGrow(0);
    }

    private void createNameColumn() {
        nameColumn = grid.addColumn(User::getFullName, "name").setHeader("Full name")
                .setComparator(User::getFullName).setWidth("280px").setFlexGrow(0);
    }

    private void createAdminColumn() {
        adminColumn = grid.addComponentColumn(user -> {
            Span span = new Span();
            span.setText(user.getAdmin() ? "Admin" : "User");
            span.getElement().setAttribute("theme", user.getAdmin() ? "badge primary" : "badge success primary");
            span.setWidth("80px");
            return span;
        }).setHeader("Type").setWidth("130px").setFlexGrow(0);
        ;
    }

    private void createPhoneColumn() {
        phoneColumn = grid.addColumn(User::getPhone, "phone").setHeader("Phone")
                .setComparator(User::getPhone).setWidth("200px").setFlexGrow(0);
    }

    private void createEmailColumn() {
        emailColumn = grid.addColumn(User::getEmail, "email").setHeader("Email")
                .setComparator(User::getEmail).setWidth("280px").setFlexGrow(0);
    }

    private void createActiveColumn() {
        activeColumn = grid.addComponentColumn(user -> {
            Span span = new Span();
            span.setText(user.getActive() ? "Active" : "Inactive");
            span.getElement().setAttribute("theme", user.getActive() ? "badge success secondary" : "badge error secondary");
            span.setWidth("120px");
            return span;
        }).setHeader("Active").setWidth("130px").setFlexGrow(0);
        ;
    }

    private void createToolsColumn() {
        toolsColumn = grid.addComponentColumn(user -> {
            Icon permissionsBtn;
            if (!user.getAdmin()) {
                permissionsBtn = VaadinIcon.ANGLE_DOUBLE_UP.create();
                permissionsBtn.getElement().setAttribute("theme", "badge primary");
                permissionsBtn.addClickListener(event -> {
                    createConfirmationPromoteDialog(user);
                });
            } else {
                permissionsBtn = VaadinIcon.ANGLE_DOUBLE_DOWN.create();
                permissionsBtn.getElement().setAttribute("theme", "badge secondary");
                permissionsBtn.addClickListener(event -> {
                    createConfirmationDemoteDialog(user);
                });
            }

            Icon deleteBtn = VaadinIcon.CLOSE_SMALL.create();
            deleteBtn.getElement().setAttribute("theme", "badge error primary");
            deleteBtn.addClickListener(event -> {
                if (userService.userOwnsAnyHW(user.getId())) {
                    openModalWindow("User can't be deleted, because he owns a hardware. " +
                            "You need to change the ownership and then you'll be able to delete this user.");
                } else {
                    createConfirmationDeleteDialog(user);
                }
            });

            return new HorizontalLayout() {
                {
                    addComponentAsFirst(permissionsBtn);
                    setSpacing(true);
                    addComponentAtIndex(1, deleteBtn);
                }
            };
        }).setHeader("Promote / delete").setWidth("100px");
    }

    private void createConfirmationPromoteDialog(User user) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setText("Are you sure you want to PROMOTE user [" + user.getFullName() + "] to admin?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setCancelButtonTheme("secondary error");
        confirmDialog.setCancelable(true);
        confirmDialog.setConfirmText("Promote to admin");
        confirmDialog.addConfirmListener(event -> {
            user.setAdmin(true);
            userService.update(user);
            gridListDataView.refreshAll();
        });
        confirmDialog.open();
    }

    private void createConfirmationDemoteDialog(User user) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setText("Are you sure you want to DEMOTE user [" + user.getFullName() + "] to user?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setCancelButtonTheme("secondary error");
        confirmDialog.setCancelable(true);
        confirmDialog.setConfirmText("Demote to user");
        confirmDialog.addConfirmListener(event -> {
            user.setAdmin(false);
            userService.update(user);
            gridListDataView.refreshAll();
        });
        confirmDialog.open();
    }

    private void createConfirmationDeleteDialog(User user) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setText("Are you sure you want to DELETE user [" + user.getFullName() + "]?");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setCancelButtonTheme("primary");
        confirmDialog.setCancelable(true);
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme("primary error");
        confirmDialog.addConfirmListener(event -> {
            User loggedUser = VaadinSession.getCurrent().getAttribute(User.class);
            if (!loggedUser.getLogin().equals(adminLogin) && user.getAdmin()) {
                openModalWindow("You're not allowed to delete another admin account.");
            } else {
                userService.delete(user.getId());
                gridListDataView.removeItem(user);
                gridListDataView.refreshAll();
            }
        });
        confirmDialog.open();
    }

    private void createUser() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCancelable(true);
        dialog.setCancelText("Cancel");
        dialog.setCancelButtonTheme("primary error");
        CreateUserForm createUserForm = new CreateUserForm();
        dialog.add(createUserForm);
        Button btn = new Button();
        btn.setThemeName("primary");
        btn.setText("Create user");
        btn.addClickListener(event -> {
            if (createUserForm.areFieldsValid() && createUserForm.arePasswordsValid()) {
                userService.update(createUserForm.createUser());
                Notification.show("New user created.");
                dialog.close();
            } else {
                if (!createUserForm.arePasswordsValid()) {
                    openModalWindow("New passwords do not match. Try again.");
                }
                if (!createUserForm.areFieldsValid()) {
                    openModalWindow("You need to fill all required fields.");
                }
            }
        });
        dialog.setConfirmButton(btn);
        dialog.open();
    }

    private void openModalWindow(String s) {
        Dialog dialog1 = new Dialog();
        dialog1.setModal(true);
        dialog1.add(s);
        dialog1.open();
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
