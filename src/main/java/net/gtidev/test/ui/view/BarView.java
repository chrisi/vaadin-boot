package net.gtidev.test.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.gtidev.test.MemoryEventListener;
import net.gtidev.test.MemoryService;
import net.gtidev.test.components.MyComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringView
public class BarView extends VerticalLayout implements View, MemoryEventListener {

  @Autowired
  private MemoryService memoryService;

  private MyComponent comp = new MyComponent();

  @PostConstruct
  private void init() {
    memoryService.addListener(this);
  }

  @PreDestroy
  private void destroy() {
    memoryService.removeListener(this);
  }

  public BarView() {
    setMargin(true);

    Label h1 = new Label("Bar View");
    h1.addStyleName("h1");
    addComponent(h1);

    comp.setCaption("Hallo Welt!");
    comp.setImmediate(true);
    addComponent(comp);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }

  @Override
  public void valueChanged(int value) {
    comp.setCaption("Hallo Welt: " + value);
  }
}
