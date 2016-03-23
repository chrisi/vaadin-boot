package net.gtidev.test.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.gtidev.test.ui.view.FooView;
import net.gtidev.test.ui.view.IndexView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringUI
@Theme("tests-valo")
@Widgetset("net.gtidev.test.components.GtiWidgetset")
//@Theme("tests-valo-metro")
//@Theme("tests-valo-reindeer")
public class VaadinUI extends UI {

  @Autowired
  private SpringViewProvider viewProvider;

  private final CssLayout menu = new CssLayout();
  private final CssLayout menuItemsLayout = new CssLayout();
  private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<>();

  @Override
  protected void init(VaadinRequest request) {
    setPollInterval(5000);
    ValoMenuLayout root = new ValoMenuLayout();
    Navigator navigator = new Navigator(this, root.getContentContainer());

    setContent(root);

    root.setWidth("100%");
    root.addMenu(buildMenu(navigator));
    addStyleName(ValoTheme.UI_WITH_MENU);

    navigator.addProvider(viewProvider); // fuer die SpringViews
    navigator.addView("index", IndexView.class);
    navigator.addView("foo", FooView.class);
    //navigator.addView("bar", BarView.class); wird nun ueber SpringView gezogen

    final String f = Page.getCurrent().getUriFragment();
    if (f == null || f.equals("")) {
      navigator.navigateTo("index");
    }

    navigator.setErrorView(IndexView.class);
    navigator.addViewChangeListener(new ViewChangeListener() {
      @Override
      public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
      }

      @Override
      public void afterViewChange(ViewChangeEvent event) {
        for (Component aMenuItemsLayout : menuItemsLayout) {
          aMenuItemsLayout.removeStyleName("selected");
        }
        for (final Map.Entry<String, String> item : menuItems.entrySet()) {
          if (event.getViewName().equals(item.getKey())) {
            for (final Component c : menuItemsLayout) {
              if (c.getCaption() != null && c.getCaption().startsWith(item.getValue())) {
                c.addStyleName("selected");
                break;
              }
            }
            break;
          }
        }
        menu.removeStyleName("valo-menu-visible");
      }
    });

    menu.setWidth("240px");

    setNavigator(navigator); // wozu das, war in der demo nicht drin, ging aber trotzdem...
  }

  private CssLayout buildMenu(Navigator navigator) {

    final HorizontalLayout top = new HorizontalLayout();
    top.setWidth("100%");
    top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
    top.addStyleName("valo-menu-title");
    final Label title = new Label("<h3>DigiTron <strong>Online</strong></h3>", ContentMode.HTML);
    title.setSizeUndefined();
    top.addComponent(title);
    top.setExpandRatio(title, 1);
    menu.addComponent(top);

    menuItems.put("index", "Index Page");
    menuItems.put("calendar", "Calendar Page");
    menuItems.put("simpleCal", "Simple Calendar");
    menuItems.put("address", "Address Page");
    menuItems.put("foo", "Foo Page");
    menuItems.put("bar", "Bar Page");

    menuItemsLayout.setPrimaryStyleName("valo-menuitems");
    menu.addComponent(menuItemsLayout);
    for (final Map.Entry<String, String> item : menuItems.entrySet()) {
      final Button b = new Button(item.getValue(), (Button.ClickListener) event -> {
        navigator.navigateTo(item.getKey());
      });
      b.setHtmlContentAllowed(true);
      b.setPrimaryStyleName("valo-menu-item");
      menuItemsLayout.addComponent(b);
    }

    menu.addComponent(createThemeSelect());

    return menu;
  }

  private Component createThemeSelect() {
    LinkedHashMap<String, String> themeVariants = new LinkedHashMap<>();

    themeVariants.put("tests-valo", "Default");
    themeVariants.put("tests-valo-metro", "Metro");
    themeVariants.put("tests-valo-reindeer", "Migrate Reindeer");

    final NativeSelect ns = new NativeSelect();
    ns.setNullSelectionAllowed(false);
    ns.setId("themeSelect");
    ns.addContainerProperty("caption", String.class, "");
    ns.setItemCaptionPropertyId("caption");
    for (final String identifier : themeVariants.keySet()) {
      ns.addItem(identifier).getItemProperty("caption").setValue(themeVariants.get(identifier));
    }

    ns.setValue("tests-valo");
    ns.addValueChangeListener((Property.ValueChangeListener) event -> setTheme((String) ns.getValue()));
    return ns;
  }

}
