package jprobe.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

public class LogTest extends junit.framework.TestCase{
	
	private static final String TEMP_LOG_TEST = ".tempLogTest";

	public void testRootLogInitAfterAcquiringChildLog(){
		org.slf4j.Logger child = LoggerFactory.getLogger(LogTest.class);
		
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

		LoggerContext context = rootLogger.getLoggerContext();
		context.reset();
		
		File test = new File(TEMP_LOG_TEST + System.currentTimeMillis());
		test.deleteOnExit();

		FileAppender<ILoggingEvent> rfAppender = new FileAppender<ILoggingEvent>();
		rfAppender.setName("TestFileAppender");
		rfAppender.setContext(context);
		
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern("%d %-5level [%thread] %logger: %msg%n");
		encoder.start();
		
		rfAppender.setEncoder(encoder);
		rfAppender.setFile(test.getAbsolutePath());
		rfAppender.start();

		rootLogger.addAppender(rfAppender);
		rootLogger.setLevel(Level.INFO);

		rootLogger.info("Root log info");
		child.info("Child log info");
		child.debug("Child log debug");
		
		if(!test.exists()){
			throw new RuntimeException("Test file does not exist.");
		}
		
		boolean success = true;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(test));
			String line;
			int count = 0;
			while((line = reader.readLine()) != null){
				switch(++count){
				case 1:
					success = success && line.contains("Root log info");
					break;
				case 2:
					success = success && line.contains("Child log info");
					break;
				default:
					if(!line.equals("")){
						throw new RuntimeException("Too many messages in log file: "+count+", "+line);
					}
				}
			}
			if(count == 0){
				throw new RuntimeException("No lines in log file.");
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					//derp
				}
			}
		}
		
		assertTrue(success);
		
	}
	
}
