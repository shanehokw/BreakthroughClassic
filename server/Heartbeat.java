import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Heartbeat extends Observable implements Runnable {
	
	private AtomicInteger currentTimeout = new AtomicInteger();
	private int maxAttempts;
	private int sleepMS;
	private AtomicBoolean isDead = new AtomicBoolean();

	/**
	 * 
	 * @param maxAttempts
	 * @param sleepTimeMS
	 */
	public Heartbeat(int maxAttempts, int sleepTimeMS) {
		this.maxAttempts = maxAttempts;
		this.sleepMS = sleepTimeMS;
		currentTimeout.set(maxAttempts);
		Thread heartbeatThread = new Thread(this);
		heartbeatThread.setPriority(Thread.MAX_PRIORITY);
		heartbeatThread.start();
	}
	
	private void updateObservers() {
		setChanged();
		notifyObservers(currentTimeout.get());
	}
	
	public boolean isDead() {
		return isDead.get();
	}
	
	public synchronized void resetTimeout() {
		System.out.println("Resetting timeout");
		synchronized(currentTimeout) {
			currentTimeout.set(maxAttempts);
		}
	}
	
	public void run() {

		for (int timeout = currentTimeout.get(); timeout >= 0; timeout = currentTimeout.get()) {
			
			System.out.println("Timeout is "  + timeout);
			try
			{
				Thread.sleep(sleepMS);
				currentTimeout.get();
			
				if(timeout == 0) {
					isDead.set(true);
					updateObservers();
				}
				else {
					// Decrement the heartbeat 
					currentTimeout.decrementAndGet();
					
					// Notify observer to send heartbeat
					System.out.println("sending a new heartbeat");
					updateObservers();
				}
			}
			catch (InterruptedException e){}
		}
	}
}