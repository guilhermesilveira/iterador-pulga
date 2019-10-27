package br.usp.iterador;

import java.io.PrintWriter;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Log4j config.
 * 
 * @author Guilherme Silveira
 */
public class Log4JConfig {

	private static final Logger LOG = Logger.getLogger(Log4JConfig.class);

	private static final IteratorConsoleAppender appender = new IteratorConsoleAppender();

	static {
		appender.setName("debug-stdout");
		appender.setWriter(new PrintWriter(System.out));
		appender.setLayout(new PatternLayout(
				"%d{HH:mm:ss,SSS} %5p [%-20c{1}] %m%n"));
	}

	public void changeState() {
		if (appender.active) {
			uninstall();
		} else {
			install();
		}
	}

	private void install() {
		LOG.debug("installing debug log");
		Logger logger = Logger.getLogger("br.usp");
		logger.removeAppender(logger.getAppender("library-stdout"));
		logger.setLevel(Level.DEBUG);
		logger.addAppender(appender);
		appender.active = true;
	}

	private void uninstall() {
		LOG.debug("uninstalling debug log");
		Logger logger = Logger.getLogger("br.usp");
		logger.removeAppender(appender);
		logger.setLevel(Level.WARN);
		logger.addAppender(logger.getAppender("library-stdout"));
		appender.active = false;
	}

	static class IteratorConsoleAppender extends ConsoleAppender {

		private boolean active = false;

	}

}
