package net.gtidev.test.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.gtidev.test.components.MyComponent;

public class BarView extends VerticalLayout implements View {

  public BarView() {
    setMargin(true);

    Label h1 = new Label("Bar View");
    h1.addStyleName("h1");
    addComponent(h1);

    MyComponent comp = new MyComponent();
    comp.setCaption("Hallo Welt!");
    addComponent(comp);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
