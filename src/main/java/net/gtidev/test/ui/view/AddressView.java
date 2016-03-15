package net.gtidev.test.ui.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import net.gtidev.test.dbaccess.CustomerRepository;
import net.gtidev.test.model.Customer;
import net.gtidev.test.ui.CustomerEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@SpringView
public class AddressView extends VerticalLayout implements View {

  @Autowired
  private CustomerRepository repo;
  @Autowired
  private CustomerEditor editor;

  private final Grid grid = new Grid();
  private final TextField filter = new TextField();
  private final Button addNewBtn = new Button("New customer", FontAwesome.PLUS);

  @PostConstruct
  public void init() throws Exception {
    HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

    Label h1 = new Label("Address View");
    h1.addStyleName("h1");
    addComponent(h1);
    addComponent(actions);
    addComponent(grid);
    addComponent(editor);

    // Configure layouts and components
    actions.setSpacing(true);
    setMargin(true);
    setSpacing(true);

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

  private void listCustomers(String text) {
    if (StringUtils.isEmpty(text)) {
      grid.setContainerDataSource(new BeanItemContainer(Customer.class, repo.findAll()));
    } else {
      grid.setContainerDataSource(new BeanItemContainer(Customer.class, repo.findByLastNameStartsWithIgnoreCase(text)));
    }
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }

}
