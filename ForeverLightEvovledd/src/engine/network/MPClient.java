package engine.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.*;

import engine.network.Packet.*;

public class MPClient extends Listener {
	public Client client;

	public MPClient(String IPAdress) {
		client = new Client();
		Packet.RegisterAll(client.getKryo());

		client.addListener(this);

		client.start();

		try {
			client.connect(5000, IPAdress, 55545);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client.stop();
		}
	}

	@Override
	public void connected(Connection arg) {
		System.out.println("[CLIENT]" + "Client has connected to: " + arg.getRemoteAddressTCP());
		MPNetworkManager.ConnectionID = arg.getID();
	}

	@Override
	public void disconnected(Connection arg) {
		System.out.println("[CLIENT]" + "Client has disconnected fraom: " + arg.getRemoteAddressTCP());
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof Packet0LoginRequest) {
			Packet1LoginAnswer loginAnswer = new Packet1LoginAnswer();
			loginAnswer.accepted = true;
			connection.sendTCP(loginAnswer);
		}

		if (object instanceof Packet2Message) {
			String message = ((Packet2Message) object).message;
			// if the server send a message to the client that the client should
			// now start running
			if (message.equals("CLIENT_SHOULD_RUN")) {
				MPNetworkManager.SHOULD_LOCK = false;
			}
		}

		if (object instanceof Packet4Function) {
			int FunctionID = ((Packet4Function) object).functionID;
			Object[] objects = ((Packet4Function) object).objects;
			MPNetworkManager.ID_Function_Dictionary.get(FunctionID).run(objects);
		}
	}
}
