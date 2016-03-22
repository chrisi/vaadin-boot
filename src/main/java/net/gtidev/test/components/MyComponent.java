package net.gtidev.test.components;


import com.vaadin.shared.MouseEventDetails;
import net.gtidev.test.components.client.mycomponent.MyComponentClientRpc;
import net.gtidev.test.components.client.mycomponent.MyComponentServerRpc;
import net.gtidev.test.components.client.mycomponent.MyComponentState;

public class MyComponent extends com.vaadin.ui.AbstractComponent {

  private MyComponentServerRpc rpc = new MyComponentServerRpc() {
    private int clickCount = 0;

    public void clicked(MouseEventDetails mouseDetails) {
      // nag every 5:th click using RPC
      if (++clickCount % 5 == 0) {
        getRpcProxy(MyComponentClientRpc.class).alert("Ok, that's enough!");
      }
      // update shared state
      getState().text = "You have clicked " + clickCount + " times";
    }
  };

  public MyComponent() {
    registerRpc(rpc);
  }

  @Override
  public MyComponentState getState() {
    return (MyComponentState) super.getState();
  }
}
