package engine.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.*;

import engine.network.Packet.*;

public class MPServer extends Listener {
	Server server;

	public int connectionCount = 0;

	public MPServer() throws IOException {
		server = new Server();
		Packet.RegisterAll(server.getKryo());
		server.addListener(this);
		server.bind(55545);
		server.start();
	}

	@Override
	public void connected(Connection arg) {
		System.out.println("[SERVER] Someone has connected.");
		// unlock the lock
		// TODO: change this stuff?
		MPNetworkManager.SHOULD_LOCK = false;
		connectionCount++;
	}

	@Override
	public void disconnected(Connection arg) {
		System.out.println("[SERVER] Someone has disconected.");
		connectionCount--;
	}

	private int recievedconnection = 0;

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof Packet0LoginRequest) {
			Packet1LoginAnswer loginAnswer = new Packet1LoginAnswer();
			loginAnswer.accepted = true;
			connection.sendTCP(loginAnswer);
		}

		if (object instanceof Packet2Message) {
			String message = ((Packet2Message) object).message;
			// the client is sending stuff back telling us that we should run
			// we could count the amount of this message sent if we want to have
			// more then 2 connections
			// TODO: make more then 2 connections
			if (message.equals("SERVER_SHOULD_RUN")) {
				recievedconnection++;
				// This is the end of turn
				// Execute all functions commands that was stored both on the
				// server and client
			}

			if (recievedconnection == MPNetworkManager.RequireConnection) {
				// run all functions on the server
				MPNetworkManager.functionList
						.forEach(x -> MPNetworkManager.ID_Function_Dictionary.get(x.functionID).run(x.objects));

				// send all functions to the client to run
				MPNetworkManager.functionList.forEach(x -> MPNetworkManager.ServerToClientOnlySend(x));

				// clear the function array and use it for next round
				MPNetworkManager.functionList.clear();

				recievedconnection = 0;

				// *************************
				// This is the start of new turn
				MPNetworkManager.SHOULD_LOCK = false;

			}
		}

		if (object instanceof Packet4Function) {
			MPNetworkManager.functionList.add((Packet4Function) object);
		}
	}
}
