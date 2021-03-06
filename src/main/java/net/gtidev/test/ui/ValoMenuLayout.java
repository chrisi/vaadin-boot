package net.gtidev.test.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class ValoMenuLayout extends HorizontalLayout {

  private CssLayout contentArea = new CssLayout();

  private CssLayout menuArea = new CssLayout();

  public ValoMenuLayout() {
    setSizeFull();

    menuArea.setPrimaryStyleName("valo-menu");

    contentArea.setPrimaryStyleName("valo-content");
    contentArea.addStyleName("v-scrollable");
    contentArea.setSizeFull();

    addComponents(menuArea, contentArea);
    setExpandRatio(contentArea, 1);
  }

  public ComponentContainer getContentContainer() {
    return contentArea;
  }

  public void addMenu(Component menu) {
    menu.addStyleName("valo-menu-part");
    menuArea.addComponent(menu);
  }

}
