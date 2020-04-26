package org.gamestate;

import engine.gamestate.*;
import engine.network.MPNetworkManager;

public class MainGameState implements IGameState {

	@Override
	public void Update() {

	}

	@Override
	public void Stop() {

	}

	@Override
	public void Init() {
		MPNetworkManager.gameStateAfterInitiation = new GamingGameState();
		// MPNetworkManager.InitServerClientByPanel();
		// delete this if intended to multiplayer
		GameState.SwitchGameState(MPNetworkManager.gameStateAfterInitiation);
	}
}
