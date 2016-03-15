package net.gtidev.test.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class IndexView extends VerticalLayout implements View {

  public IndexView() {
    setMargin(true);

    Label h1 = new Label("Index View");
    h1.addStyleName("h1");
    addComponent(h1);

    HorizontalLayout row = new HorizontalLayout();
    row.addStyleName("wrapping");
    row.setSpacing(true);
    addComponent(row);

    TextField tf = new TextField("Custom color");
    tf.setInputPrompt("Email");
    tf.addStyleName("color1");
    row.addComponent(tf);

    tf = new TextField("User Color");
    tf.setInputPrompt("Gender");
    tf.addStyleName("color2");
    row.addComponent(tf);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
