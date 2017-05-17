/**
 * Created by y.brisch on 11.05.17.
 */
public class ThreadPoolException extends RuntimeException {
  /**
   * Thrown when there's a RuntimeException or InterruptedException when executing a runnable from the queue, or awaiting termination
   */
  public ThreadPoolException(Throwable cause) {
    super(cause);
  }
}