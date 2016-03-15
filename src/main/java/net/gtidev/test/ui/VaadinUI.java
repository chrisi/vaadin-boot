package net.gtidev.test.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.gtidev.test.ui.view.BarView;
import net.gtidev.test.ui.view.FooView;
import net.gtidev.test.ui.view.IndexView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringUI
//@Theme("tests-valo-metro")
@Theme("tests-valo-reindeer")
public class VaadinUI extends UI {

  @Autowired
  private SpringViewProvider viewProvider;

  private final ValoMenuLayout root = new ValoMenuLayout();
  private final ComponentContainer viewDisplay = root.getContentContainer();
  private final Navigator navigator = new Navigator(this, viewDisplay);
  private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<>();
  private final CssLayout menu = new CssLayout();
  private final CssLayout menuItemsLayout = new CssLayout();

  @Override
  protected void init(VaadinRequest request) {
    setContent(root);
    root.setWidth("100%");
    root.addMenu(buildMenu());
    addStyleName(ValoTheme.UI_WITH_MENU);

    navigator.addProvider(viewProvider);
    navigator.addView("index", IndexView.class);
    navigator.addView("foo", FooView.class);
    navigator.addView("bar", BarView.class);

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

    setNavigator(navigator); // wozu dass, war in der demo nicht drin, ging aber trotzdem...
  }

  private CssLayout buildMenu() {
    // Add items
    menuItems.put("index", "Index Page");
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

    return menu;
  }

}
