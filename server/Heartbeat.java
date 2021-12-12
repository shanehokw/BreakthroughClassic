import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// Implementation of a timeOut heartbeat that won't make the interfaces feel unresponsive
public class Heartbeat extends Observable implements Runnable {
	
	private AtomicInteger currentTimeout = new AtomicInteger();
	private int maxAttempts;
	private int sleepMS;
	private AtomicBoolean isDead = new AtomicBoolean();
	
	// The default spec wound up doing a heartbeat every 1/2 second, and timing out after 5 seconds
	// This replicates the functionality in a different way, without the lag
	/**
	 * 
	 * @param maxAttempts
	 * @param sleepTimeMS
	 */
	public Heartbeat(int maxAttempts, int sleepTimeMS) {
		this.maxAttempts = maxAttempts;
		this.sleepMS = sleepTimeMS;
		currentTimeout.set(maxAttempts);
		new Thread(this).start();
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
					// Decrement the heartbeat (will be reset eventually, maybe)
					currentTimeout.decrementAndGet();
					
					// Notify observer to send heartbeat
					System.out.println("sending a new heartbeat, maybe, in theory");
					updateObservers();
				}
			}
			catch (InterruptedException e)
			{
				// This really shouldn't ever happen (when we'd actually care about it happening)
			}
		}
	}
}