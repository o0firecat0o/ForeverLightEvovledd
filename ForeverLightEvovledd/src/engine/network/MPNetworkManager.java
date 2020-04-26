package engine.network;

import java.io.IOException;
import java.util.*;

import engine.gamestate.IGameState;
import engine.network.Packet.*;
import engine.network.jPanel.*;

/**
 * 
 * The class for multiplayer networking instantiation! prevent using MPClient or
 * MPServer Independently.
 * 
 * @author Ringo
 *
 */
public class MPNetworkManager {

	public static MPServer server;
	public static MPClient client;

	private static boolean connectionState = false;

	/**
	 * Has the NetworkServer been initialized?
	 * 
	 * @return
	 */
	public static boolean getConnectionState() {
		return connectionState;
	}

	public static int ConnectionID = -1;

	// originally the should lock should be true
	// locking both server and client waiting for initiation
	public static boolean SHOULD_LOCK = true;
	public static boolean SHOULD_SEND = false;

	// Frame Counter
	public static int Frame = 0;

	public static IGameState gameStateAfterInitiation;

	// The connection that was require to start the game
	public static int RequireConnection = 1;

	public static Map<Integer, INetworkFunction> ID_Function_Dictionary = new HashMap<>();

	public static void ClientToServerOnlySend(Object object) {
		if (client != null) {
			client.client.sendTCP(object);
		}
	}

	public static void ServerToClientOnlySend(Object object) {
		if (server != null) {
			server.server.sendToAllTCP(object);
		}
	}

	public static void Send(Object object) {
		if (server != null) {
			server.server.sendToAllTCP(object);
		}
		if (client != null) {
			client.client.sendTCP(object);
		}
	}

	public static void InitServer() {
		try {
			server = new MPServer();
			connectionState = true;
			System.out.println("serveropened!");
			ConnectionID = 0;
			SHOULD_SEND = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void InitClient(String ip) {
		client = new MPClient(ip);
		connectionState = true;
	}

	public static void InitServerClientByPanel() {
		new JPanelNetworkSelectServerClient().hookedInterface = new IJPanelNetworkSelectServerClient() {
			@Override
			public void run(boolean isServer) {
				if (isServer) {
					InitServer();
				} else {
					InitClientByPanel();
				}
			}
		};
	}

	/*
	 * get the ip address by jPanel
	 */
	private static void InitClientByPanel() {
		new JPanelNetworkIPInputField().hookedInterface = new IJPanelNetworkIPInputField() {

			@Override
			public void run(String ip) {
				InitClient(ip);
			}
		};
	}

	// This update function is directly provided in the network thread
	public static void Update() {
		// if SHOULD_LOCK <current turn has finished running, now send to client
		// to execute their turn>
		// if SHOULD_SEND <never send the message in this turn, send once only>
		// if server <I am the server>
		// if server.connectionCount <Actually there is connection in the
		// server>
		if (SHOULD_LOCK && SHOULD_SEND && server != null
				&& server.connectionCount == MPNetworkManager.RequireConnection) {
			SHOULD_SEND = false;

			// Sending message back to client
			Packet2Message message = new Packet2Message();
			message.message = "CLIENT_SHOULD_RUN";
			ServerToClientOnlySend(message);
		}

		if (SHOULD_LOCK && SHOULD_SEND && client != null) {
			SHOULD_SEND = false;
			Packet2Message message = new Packet2Message();
			message.message = "SERVER_SHOULD_RUN";
			ClientToServerOnlySend(message);
		}
	}

	private static int FunctionIDCount = 0;

	private static int genNewFunctionID() {
		FunctionIDCount++;
		return FunctionIDCount;
	}

	public static int RegisterFunction(INetworkFunction function) {
		int ID = genNewFunctionID();
		ID_Function_Dictionary.put(ID, function);
		return ID;
	}

	public static void UnRegisterFunction(int functionID) {
		ID_Function_Dictionary.remove(ID_Function_Dictionary.get(functionID));
	}

	public static ArrayList<Packet4Function> functionList = new ArrayList<Packet4Function>();

	public static void sendFunction(int FunctionID, Object[] objects) {
		// if I am a client
		if (client != null) {
			Packet4Function functionPackage = new Packet4Function();
			functionPackage.functionID = FunctionID;
			functionPackage.objects = objects;
			ClientToServerOnlySend(functionPackage);
		}
		// if I am a server
		if (server != null) {
			// no need to send to client now, just save it
			// it will be sent to client later
			Packet4Function functionPackage = new Packet4Function();
			functionPackage.functionID = FunctionID;
			functionPackage.objects = objects;
			functionList.add(functionPackage);
		}
		// if i am neither any, singleplayer
		if (client == null && server == null) {
			ID_Function_Dictionary.get(FunctionID).run(objects);
		}
	}
}
