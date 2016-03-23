package net.gtidev.test.components;


import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;
import net.gtidev.test.components.client.mycomponent.MyComponentClientRpc;
import net.gtidev.test.components.client.mycomponent.MyComponentServerRpc;
import net.gtidev.test.components.client.mycomponent.MyComponentState;

public class MyComponent extends AbstractComponent implements MyComponentServerRpc {

  private int clickCount = 0;

  public MyComponent() {
    registerRpc(this, MyComponentServerRpc.class);
  }

  @Override
  public MyComponentState getState() {
    return (MyComponentState) super.getState();
  }

  @Override
  public void clicked(MouseEventDetails mouseDetails) {
    // nag every 5:th click using RPC
    if (++clickCount % 5 == 0) {
      getRpcProxy(MyComponentClientRpc.class).alert("Ok, that's enough!");
    }
    // update shared state
    getState().text = "You have clicked " + clickCount + " times";
  }

  public void setValue(int value) {
    //getRpcProxy(MyComponentClientRpc.class).alert("Value is " + value);
    getState().text = "Value submitted " + value;
  }
}
