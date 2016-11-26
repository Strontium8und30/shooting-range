package utilities.log;

import java.io.*;

import utilities.*;

/**
 * Das Log Objekt ist zuständig für das Fehlermanagement
 * Ausgabe von Informationen (Debug)
 * 		   von Warnungen (Warning)
 *         von Fehlern (Error)
 *         und evtl. Speichern (je nach logLevel)
 *         
 * @author Thorben
 * 	
 */
public class Log {
	
	public static enum LogLevel {
		INFO(0, "[INFO]   "),
		DEBUG(1, "[DEBUG]  "),
		WARNING(2, "[WARNING]"),
		ERROR(3, "[ERROR]  "),
		FATAL(4, "[FATAL]  "),
		NONE(5, "[NONE]   ");
		
		/** Der Code/ID des LogLevels */
		int logCode;
		
		/** Die Bezeichnung des LogLevels */
		String logDescription;
		
		private LogLevel(int logCode, String logDescription) {
			this.logCode = logCode;
			this.logDescription = logDescription;
		}
		
		public int getLogCode() {
			return logCode;
		}
		
		public String getDescrition() {
			return logDescription;
		}
	}

	/** Speicher das Loglevel für alle Loggings */
	static LogLevel logLevel = LogLevel.INFO;
	
	/** Speichert die Datei in die gelogt wird */
	static String logFile = "log/logging.txt";
	
	
	/** Die Klasse für die geloggt wird */
	String clazz = null;
	
	public Log(String clazz) {
		this.clazz = clazz;
	}
	
	public void info(Object message) {
		String logMsg = createLogMessage(LogLevel.INFO, message);
		if(LogLevel.INFO.getLogCode() >= logLevel.getLogCode()) {
			System.out.println(logMsg);
			log(logMsg);
		}
	}
	
	public void debug(Object message) {
		String logMsg = createLogMessage(LogLevel.DEBUG, message);
		if(LogLevel.DEBUG.getLogCode() >= logLevel.getLogCode()) {
			System.out.println(logMsg);
			log(logMsg);
		}
	}
	
	public void warning(Object message) {
		String logMsg = createLogMessage(LogLevel.WARNING, message);
		if(LogLevel.WARNING.getLogCode() >= logLevel.getLogCode()) {
			System.out.println(logMsg);
			log(logMsg);
		}
	}
	
	public void error(Object message) {
		String logMsg = createLogMessage(LogLevel.ERROR, message);
		if(LogLevel.ERROR.getLogCode() >= logLevel.getLogCode()) {
			System.err.println(logMsg);
			log(logMsg);
		}
	}
	
	public void error(Object message, Exception e) {
		String logMsg = createLogMessage(LogLevel.ERROR, message);
		if(LogLevel.ERROR.getLogCode() >= logLevel.getLogCode()) {
			log(logMsg);
			e.printStackTrace();
		}
	}
	
	public void fatal(Object message) {
		String logMsg = createLogMessage(LogLevel.FATAL, message);
		if(LogLevel.FATAL.getLogCode() >= logLevel.getLogCode()) {
			System.err.println(logMsg);
			log(logMsg);
			
		}
	}
	
	public String createLogMessage(LogLevel logLevel, Object message) {
		return Utilities.getSystemDate("dd:MM:yyyy HH:mm:ss:mmm") + " " +
			   logLevel.getDescrition() +
			   " <" + clazz + "> " +
			   message;
	}
	
	private void log(String message) {
		BufferedWriter hFile;
		try {
			hFile = new BufferedWriter(new FileWriter(logFile, true));
			hFile.write(message);
			hFile.newLine();
			hFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setLogLevel(LogLevel level) {
		logLevel = level;
	}
	
	public static LogLevel getLogLevel() {
		return logLevel;
	}
	
	public static void setLogDirectory(String file) {
		logFile = file;
	}
	
	public static String getLogDirectory() {
		return logFile;
	}
}
