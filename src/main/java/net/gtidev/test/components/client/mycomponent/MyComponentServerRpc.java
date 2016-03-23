package net.gtidev.test.components.client.mycomponent;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

public interface MyComponentServerRpc extends ServerRpc {

	// TODO example API
	void clicked(MouseEventDetails mouseDetails);

}
