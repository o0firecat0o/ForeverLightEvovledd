package engine.main;

import engine.network.MPNetworkManager;

public class Network implements Runnable {

	private boolean running = false;
	public static int nps;

	@Override
	public void run() {
		running = true;

		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		double delta = 0.0;
		int updates = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				nps = updates;
				updates = 0;
			}
			if (Render.running == false && Render.finishInit == true) {
				running = false;
			}
		}
	}

	public void update() {
		MPNetworkManager.Update();
	}

}
