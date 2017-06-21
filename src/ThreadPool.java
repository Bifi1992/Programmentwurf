import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by y.brisch on 11.05.17.
 */
public class ThreadPool {
  private ConcurrentLinkedQueue<Runnable> mRocketPool;

  // Count of threadpools created
  private static AtomicInteger poolCount = new AtomicInteger(0);
  // Queue of runnables
  private ConcurrentLinkedQueue<Runnable> runnables;
  // Flag to control the SimpleThreadpoolThread objects
  private AtomicBoolean execute;
  // Holds the "pool" of threads
  private List<ThreadPoolThread> threads;

  /**
   * Private constructor to control the creation of threadpools. Increments the poolcount whenever a new pool is created.
   *
   * @param threadCount Number of SimpleThreadpoolThreads to add to the pool
   */
  private ThreadPool(int threadCount) {
    // Increment pool count
    poolCount.incrementAndGet();
    this.runnables = new ConcurrentLinkedQueue<>();
    this.execute = new AtomicBoolean(true);
    this.threads = new ArrayList<>();
    for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
      ThreadPoolThread thread = new ThreadPoolThread("Threadpool" +
          poolCount.get() + "Thread" + threadIndex, this.execute, this.runnables);
      thread.start();
      this.threads.add(thread);
    }
  }

  /**
   * Gets a new threadpool instance with number of threads equal to the number of processors (or CPU threads) available
   *
   * @return new SimpleThreadpool
   */
  public static ThreadPool getInstance() {
    return getInstance(Runtime.getRuntime().availableProcessors());
  }

  /**
   * Gets a new threadpool instance with the number of threads specified
   *
   * @param threadCount Threads to add to the pool
   * @return new SimpleThreadpool
   */
  public static ThreadPool getInstance(int threadCount) {
    return new ThreadPool(threadCount);
  }

  /**
   * Adds a runnable to the queue for processing
   *
   * @param runnable Runnable to be added to the pool
   */
  public void execute(Runnable runnable) {
    if (this.execute.get()) {
      runnables.add(runnable);
    } else {
      throw new IllegalStateException("Threadpool terminating, unable to execute runnable");
    }
  }

  /**
   * Awaits up to <b>timeout</b> ms the termination of the threads in the threadpool
   *
   * @param timeout Timeout in milliseconds
   * @throws TimeoutException      Thrown if the termination takes longer than the timeout
   * @throws IllegalStateException Thrown if the stop() or terminate() methods haven't been called before awaiting
   */
  public void awaitTermination(long timeout) throws TimeoutException {
    if (this.execute.get()) {
      throw new IllegalStateException("Threadpool not terminated before awaiting termination");
    }
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime <= timeout) {
      boolean flag = true;
      for (Thread thread : threads) {
        if (thread.isAlive()) {
          flag = false;
          break;
        }
      }
      if (flag) {
        return;
      }
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        throw new ThreadPoolException(e);
      }
    }
    throw new TimeoutException("Unable to terminate threadpool within the specified timeout (" + timeout + "ms)");
  }

  /**
   * Awaits the termination of the threads in the threadpool indefinitely
   *
   * @throws IllegalStateException Thrown if the stop() or terminate() methods haven't been called before awaiting
   */
  public void awaitTermination() throws TimeoutException {
    if (this.execute.get()) {
      throw new IllegalStateException("Threadpool not terminated before awaiting termination");
    }
    while (true) {
      boolean flag = true;
      for (Thread thread : threads) {
        if (thread.isAlive()) {
          flag = false;
          break;
        }
      }
      if (flag) {
        return;
      }
    }
  }

  /**
   * Clears the queue of runnables and stops the threadpool. Any runnables currently executing will continue to execute.
   */
  public void terminate() {
    runnables.clear();
    stop();
  }

  /**
   * Stops addition of new runnables to the threadpool and terminates the pool once all runnables in the queue are executed.
   */
  public void stop() {
    execute.set(false);
  }
}
