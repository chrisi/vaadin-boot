package net.gtidev.test.composites;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;

public class MyCompositeComponent extends CustomComponent {


  private Label label;

  public MyCompositeComponent(Resource imgRes) {
    // A layout structure used for composition
    //Panel panel = new Panel("My Custom Component");
    HorizontalLayout panelContent = new HorizontalLayout();
    //panelContent.setMargin(true); // Very useful
    //panel.setContent(panelContent);
    panelContent.setMargin(true);


    // Compose from multiple components
    label = new Label();
    label.setWidth("200px");
    panelContent.addComponent(label);

    Image img = new Image(null, imgRes);
    img.setHeight("24px");

    panelContent.addComponent(img);

    // Set the size as undefined at all levels
    panelContent.setSizeUndefined();
    //panel.setSizeUndefined();
    setSizeUndefined();

    // The composition root MUST be set
    setCompositionRoot(panelContent);
  }

  public void setTitel(String titel) {
    label.setValue(titel);
  }
}
