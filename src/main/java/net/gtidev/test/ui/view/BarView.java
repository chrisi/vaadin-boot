package net.gtidev.test.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BarView extends VerticalLayout implements View {

  public BarView() {
    setMargin(true);

    Label h1 = new Label("Bar View");
    h1.addStyleName("h1");
    addComponent(h1);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
