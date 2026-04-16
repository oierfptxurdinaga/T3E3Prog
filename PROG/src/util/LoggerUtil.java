package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger eta salbuespenak kudeatzeko utilitate klasea. Aplikazio osoan
 * erabiltzen da logak gordetzeko eta salbuespenak kontrolatzeko.
 */
public class LoggerUtil {
	private static PrintWriter logger;
	private static final String LOG_FITXATEGIA = "log.txt";

	/**
	 * Loggterra hasieratzen du beharrezkoa bada.
	 */
	private static void initLogger() {
		if (logger == null) {
			try {
				File logFile = new File(LOG_FITXATEGIA);
				FileWriter fw = new FileWriter(logFile, true);
				logger = new PrintWriter(fw, true);
			} catch (IOException e) {
				System.err.println("Errorea loggerra hasteratzerakoan: " + e.getMessage());
			}
		}
	}

	/**
	 * Mezu bat log fitxategian idazten du data eta orduarekin.
	 * 
	 * @param mezua Log-ean gorde beharreko mezua.
	 */
	public static void log(String mezua) {
		initLogger();
		if (logger != null) {
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			logger.println("[" + timestamp + "] " + mezua);
		}
	}

	/**
	 * Loggerra ixten du.
	 */
	public static void closeLogger() {
		if (logger != null) {
			logger.close();
			logger = null;
		}
	}

	// ==================== SALBUESPEN PERTSONALIZATUA ====================

	/**
	 * Datu-basearekin arazoak daudenean erabiltzen den salbuespena.
	 */
	public static class DataAccessException extends Exception {
		public DataAccessException(String mezua) {
			super(mezua);
		}

		public DataAccessException(String mezua, Throwable kausa) {
			super(mezua, kausa);
		}
	}
}