package managers;

import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

/**
 * A logging system that keeps track of all the changes of this program and
 * keeps a log and save it to a certain file.
 * 
 */
public class MyLogingSystem {
	private static final Logger logger = Logger.getLogger(MyLogingSystem.class.getName());
	private static FileHandler fh = null;
	// Singleton pattern.
	private static final MyLogingSystem instance = new MyLogingSystem();

	/**
	 * Return an instance for MyLogingSystem.
	 * 
	 * @return an instance for MyLogingSystem
	 */
	public static MyLogingSystem getInstance() {
		return instance;
	}

	/**
	 * Return a Logger.
	 * 
	 * @return a Logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * A MyLogingSystem.
	 * 
	 */
	private MyLogingSystem() {
		try {
			fh = new FileHandler("PhotoRenamer.log", true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fh.setFormatter(new MyLogingSystemHander());
		logger.addHandler(fh);
		logger.setLevel(Level.ALL);
	}

	/**
	 * A formatter that formats the log's output.
	 */
	private class MyLogingSystemHander extends Formatter {

		/**
		 * Specify the format of the log output file.
		 * 
		 * @param record
		 *            a LogRecord
		 * @return a String that will be saved to log out put file.
		 */
		@Override
		public String format(LogRecord record) {
			Date time = new Date();
			time.setTime(record.getMillis());
			return time + "\r\n" + record.getMessage() + "\r\n" + "\r\n";
		}
	}
}