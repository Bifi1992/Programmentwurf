import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by y.brisch on 11.05.17.
 */
public class NeuralNetworkThreadPoolThread extends Thread{
  private AtomicBoolean execute;
  private ConcurrentLinkedQueue<Runnable> runnables;

  public NeuralNetworkThreadPoolThread(String name, AtomicBoolean execute, ConcurrentLinkedQueue<Runnable> runnables) {
    super(name);
    this.execute = execute;
    this.runnables = runnables;
  }

  @Override
  public void run() {
    try {
      // Continue to execute when the execute flag is true, or when there are runnables in the queue
      while (execute.get() || !runnables.isEmpty()) {
        Runnable runnable;
        // Poll a runnable from the queue and execute it
        while ((runnable = runnables.poll()) != null) {
          runnable.run();
        }
        // Sleep in case there wasn't any runnable in the queue. This helps to avoid hogging the CPU.
        Thread.sleep(1);
      }
    } catch (RuntimeException | InterruptedException e) {
      throw new ThreadPoolException(e);
    }
  }
}
