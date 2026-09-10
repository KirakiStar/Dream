package main;

public class GameLoop implements Runnable {
	private Thread gameThread;
	private Game game;
	private final int FPS_SET = 60;
	private final int UPS_SET = 100;
	
	public GameLoop(Game game) {
		this.game = game;
	}
	
	public void start() {
        gameThread = new Thread(this);
        gameThread.start();
    }

	@Override
	public void run() {
		double timePerFrame = 1000000000.0/FPS_SET;
		double timePerUpdate = 1000000000.0/UPS_SET;
		
		long prevTime = System.nanoTime();
		long lastCheck = System.currentTimeMillis();
		
		double deltaF = 0;
		double deltaU = 0;
		
		int frames = 0;
		int updates = 0;
		
		while(true) {
			long currTime = System.nanoTime();
			
			deltaU += (currTime - prevTime) / timePerUpdate;
			deltaF += (currTime - prevTime) / timePerFrame;
			prevTime = currTime;
			
			if(deltaU >= 1) {
				game.update();
				updates++;
				deltaU--;
			}
			
			if(deltaF >= 1) {
				game.getGamePanel().repaint();
				frames++;
				deltaF--;
			}
			
			if(System.currentTimeMillis() - lastCheck >=1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println(frames+" FPS | "+updates+" UPS");
				frames = 0;
				updates = 0;
			}
		}
	}
	
}
