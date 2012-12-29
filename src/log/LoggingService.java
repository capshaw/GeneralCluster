package log;

/**
 * A logging service to log text to some output.
 * @author capshaw
 *
 */
public class LoggingService {
	
	/**
	 * Log the parameterized text to some output.
	 * @param text The text to log
	 */
	public void log(String text) {
		System.out.println("[General Cluster] " + text);
	}
}