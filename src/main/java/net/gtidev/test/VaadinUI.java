package net.gtidev.test;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.gtidev.test.dbaccess.CustomerRepository;
import net.gtidev.test.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringUI
//@Theme("tests-valo-metro")
@Theme("tests-valo-reindeer")
public class VaadinUI extends UI {

  private final CustomerRepository repo;

  private final CustomerEditor editor;

  private final Grid grid;

  private final TextField filter;

  private final Button addNewBtn;

  private final ValoMenuLayout root = new ValoMenuLayout();
  private Navigator navigator;
  private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<>();
  private CssLayout menu = new CssLayout();
  private CssLayout menuItemsLayout = new CssLayout();

  @Autowired
  public VaadinUI(CustomerRepository repo, CustomerEditor editor) {
    this.repo = repo;
    this.editor = editor;
    this.grid = new Grid();
    this.filter = new TextField();
    this.addNewBtn = new Button("New customer", FontAwesome.PLUS);
  }

  private MenuBar initMennu() {

    MenuBar barmenu = new MenuBar();
    barmenu.setWidth("100%");


    // Define a common menu command for all the menu items.
    MenuBar.Command mycommand = (MenuBar.Command) selectedItem -> {
    };

    // A top-level menu item that opens a submenu
    MenuBar.MenuItem drinks = barmenu.addItem("Beverages", null, null);

// Submenu item with a sub-submenu
    MenuBar.MenuItem hots = drinks.addItem("Hot", null, null);
    hots.addItem("Tea", /* new ThemeResource("icons/tea-16px.png") */ null, mycommand);
    hots.addItem("Coffee",/* new ThemeResource("icons/coffee-16px.png") */ null, mycommand);

// Another submenu item with a sub-submenu
    MenuBar.MenuItem colds = drinks.addItem("Cold", null, null);
    colds.addItem("Milk", null, mycommand);
    colds.addItem("Weissbier", null, mycommand);

// Another top-level item
    MenuBar.MenuItem snacks = barmenu.addItem("Snacks", null, null);
    snacks.addItem("Weisswurst", null, mycommand);
    snacks.addItem("Bratwurst", null, mycommand);
    snacks.addItem("Currywurst", null, mycommand);

// Yet another top-level item
    MenuBar.MenuItem servs = barmenu.addItem("Services", null, null);
    servs.addItem("Car Service", null, mycommand);

    return barmenu;
  }

  @Override
  protected void init(VaadinRequest request) {
    // build layout

    HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
    VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
    //VerticalLayout wrapper = new VerticalLayout(initMennu(), mainLayout);

    setContent(root);
    root.setWidth("100%");
    root.addMenu(buildMenu());
    addStyleName(ValoTheme.UI_WITH_MENU);


    // Configure layouts and components
    actions.setSpacing(true);
    mainLayout.setMargin(true);
    mainLayout.setSpacing(true);

    grid.setHeight(300, Unit.PIXELS);
    grid.setColumns("id", "firstName", "lastName");

    filter.setInputPrompt("Filter by last name");

    // Hook logic to components

    // Replace listing with filtered content when user changes filter
    filter.addTextChangeListener(e -> listCustomers(e.getText()));

    // Connect selected Customer to editor or hide if none is selected
    grid.addSelectionListener(e -> {
      if (e.getSelected().isEmpty()) {
        editor.setVisible(false);
      } else {
        editor.editCustomer((Customer) e.getSelected().iterator().next());
      }
    });

    // Instantiate and edit new Customer the new button is clicked
    addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));

    // Listen changes made by the editor, refresh data from backend
    editor.setChangeHandler(() -> {
      editor.setVisible(false);
      listCustomers(filter.getValue());
    });

    // Initialize listing
    listCustomers(null);
  }

  private CssLayout buildMenu() {
    // Add items
    menuItems.put("common", "Common UI Elements");
    menuItems.put("labels", "Labels");
    menuItems.put("buttons-and-links", "Buttons & Links");
    menuItems.put("textfields", "Text Fields");
    menuItems.put("datefields", "Date Fields");
    menuItems.put("comboboxes", "Combo Boxes");

    menuItemsLayout.setPrimaryStyleName("valo-menuitems");
    menu.addComponent(menuItemsLayout);
    for (final Map.Entry<String, String> item : menuItems.entrySet()) {
      final Button b = new Button(item.getValue(), new Button.ClickListener() {
        @Override
        public void buttonClick(final Button.ClickEvent event) {
          navigator.navigateTo(item.getKey());
        }
      });
      b.setHtmlContentAllowed(true);
      b.setPrimaryStyleName("valo-menu-item");
      menuItemsLayout.addComponent(b);
    }

    return menu;
  }

  // tag::listCustomers[]
  private void listCustomers(String text) {
    if (StringUtils.isEmpty(text)) {
      grid.setContainerDataSource(new BeanItemContainer(Customer.class, repo.findAll()));
    } else {
      grid.setContainerDataSource(new BeanItemContainer(Customer.class, repo.findByLastNameStartsWithIgnoreCase(text)));
    }
  }
  // end::listCustomers[]
}
