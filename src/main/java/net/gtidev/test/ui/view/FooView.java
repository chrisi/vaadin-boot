package net.gtidev.test.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

public class FooView extends VerticalLayout implements View {

  public FooView() {
    setMargin(true);

    Label h1 = new Label("Foo View");
    h1.addStyleName("h1");
    addComponent(h1);
  }


  private MenuBar initNavi() {

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
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
