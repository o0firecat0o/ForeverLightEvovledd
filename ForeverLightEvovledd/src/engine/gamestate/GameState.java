package engine.gamestate;

import engine.object.*;
import engine.timer.Timer;

public class GameState {
	public static IGameState currentGameState;

	public static void SwitchGameState(IGameState gameState) {
		if (currentGameState != null) {
			currentGameState.Stop();
			System.out
					.println("Switching GameState from: " + currentGameState.getClass() + "to " + gameState.getClass());
		}
		for (UpdatableObject gameObject : GameObject.AllUpdatableObject) {
			if (!gameObject.stay_on_stage) {
				gameObject.InitDestroy();
			}
		}
		// remove all timers;
		Timer.timerArrays.forEach(x -> x.Destroy());

		System.out.println("Remaing GameObjects: " + GameObject.AllUpdatableObject.size());
		GameState.currentGameState = gameState;
		gameState.Init();

	}
}
