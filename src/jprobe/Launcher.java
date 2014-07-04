package jprobe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

public class Launcher {
	
	private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Launcher.class);;
	
	public static void main(String[] args){
		
		//first set the log to write to std.err instead of std.out
		directLogToStdErr();
		//initialize a directory for user specific files
		File jprobeDir = initializeUserDirectory();
		File propertiesDir = initializePropertiesDirectory(jprobeDir);
		File logDir = initializeLogDirectory(jprobeDir);
		//read the properties
		Properties props = readProperties(propertiesDir);
		//get the log level from the properties
		Level logLevel = Level.valueOf(props.getProperty(Constants.PROPERTY_KEY_LOG_LEVEL));
		//init the log using the logDir
		initializeLog(logDir, logLevel);
		
		File cacheDir = initializeFelixCacheDirectory(jprobeDir);
		File userPluginsDir = initializeUserPluginDirectory(jprobeDir);
		
		LOG.info("Building {}", JProbe.class);
		JProbe core = new JProbe(jprobeDir.getAbsolutePath(), logDir.getAbsolutePath(), propertiesDir.getAbsolutePath());
		LOG.info("Starting core...");
		core.start(userPluginsDir, cacheDir, args);
		LOG.info("Core started.");
		try {
			core.waitForShutdown();
		} catch (InterruptedException e) {
			LOG.error("{}",e);
			throw new Error(e);
		}
		LOG.info("Exiting.");
	}

	private static Properties readProperties(File propertiesDir){
		Properties props = new Properties(Constants.DEFAULT_PROPERTIES);
		File corePropsFile = new File(propertiesDir, Constants.PROPERTIES_FILE_NAME);
		//try reading the file if it exists
		if(corePropsFile.exists()){
			//don't log info, because this happens before the log file is initialized
			//LOG.info("Attempting to read core properties from {}", corePropsFile);
			try{
				InputStream in = new BufferedInputStream(new FileInputStream(corePropsFile));
				props.load(in);
				//LOG.info("Read properties from {}", corePropsFile);
			}catch(Exception e){
				//LOG.info("Unable to read properties from {}", corePropsFile);
			}
		}else{
			//the file doesn't exist, so try writing it
			try {
				Constants.DEFAULT_PROPERTIES.store(new BufferedOutputStream(new FileOutputStream(corePropsFile)), Constants.getDefaultPropertiesComment());
			} catch (Exception e){
				LOG.warn("Unable to create properties file {}. {}", corePropsFile, e);
			}
		}
		
		return props;
	}
	
	private static File initializeUserPluginDirectory(File userDir){
		File logDir = new File(userDir, Constants.USER_PLUGINS_DIR_NAME);
		if(!logDir.exists() && !logDir.mkdir()){
			LOG.warn("Unable to initialize user plugins directory {}", logDir);
		}
		return logDir;
	}
	
	private static File initializePropertiesDirectory(File userDir){
		File logDir = new File(userDir, Constants.PROPERTIES_DIR_NAME);
		if(!logDir.exists() && !logDir.mkdir()){
			LOG.warn("Unable to initialize properties directory {}", logDir);
		}
		return logDir;
	}
	
	private static File initializeFelixCacheDirectory(File userDir){
		//assign the cache directory a unique name, because it will be deleted when the program exits
		File cacheDir = createUniqueFile(userDir, Constants.FELIX_CACHE_DIR_NAME);
		if(!cacheDir.mkdir()){
			LOG.warn("Unable to initialize temporary felix bundle-cache {}, trying in working directory...", cacheDir);
			cacheDir = createUniqueFile(Constants.FELIX_CACHE_DIR_NAME);
			if(!cacheDir.mkdir()){
				LOG.error("Unable to initialize felix bundle-cache in {} or working directory. Exiting.", userDir);
				System.exit(1);
			}else{
				LOG.info("Created temporary felix bundle-cache {}", cacheDir);
			}
		}else{
			LOG.info("Created temporary felix bundle-cache {}", cacheDir);
		}
		cacheDir.deleteOnExit();
		return cacheDir;
	}
	
	private static File initializeLogDirectory(File userDir){
		File logDir = new File(userDir, Constants.LOG_DIR_NAME);
		if(!logDir.exists() && !logDir.mkdir()){
			LOG.warn("Unable to initialize log directory {}", logDir);
		}
		return logDir;
	}

	private static File initializeUserDirectory() {
		File jprobeDir = new File(Constants.USER_HOME_DIR + File.separator + Constants.USER_DIR_NAME);
		if(!jprobeDir.exists()){
			LOG.info("Initializing user directory {}", jprobeDir);
			if(!jprobeDir.mkdir()){
				LOG.warn("Unable to initialize user directory {}, trying in working directory...", jprobeDir);
				jprobeDir = new File(Constants.USER_DIR_NAME);
				if(!jprobeDir.exists() && !jprobeDir.mkdir()){
					LOG.warn("Unable to initialize user directory {}, either.", jprobeDir);
					System.exit(1);
				}else{
					LOG.info("Created user directory {}", jprobeDir);
				}
			}else{
				LOG.info("Created user directory {}", jprobeDir);
			}
		}
		return jprobeDir;
	}
	
	private static void directLogToStdErr(){
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

		LoggerContext context = rootLogger.getLoggerContext();
		context.reset();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern(Constants.LOG_PATTERN);
		encoder.start();
		
		ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
		appender.setName("StdErrAppender");
		appender.setEncoder(encoder);
		appender.setTarget("System.err");
		appender.start();
		
		rootLogger.addAppender(appender);
	}
	
	private static File createUniqueFile(String name){
		File f = new File(name);
		if(f.exists()){
			int count = 0;
			f = new File(name + count);
			while(f.exists()){
				f = new File(name + (++count));
			}
		}
		return f;
	}
	
	private static File createUniqueFile(File parent, String name){
		File f = new File(parent, name);
		if(f.exists()){
			int count = 0;
			f = new File(name + count);
			while(f.exists()){
				f = new File(parent, name + (++count));
			}
		}
		return f;
	}
	
	private static boolean canWriteDirectory(File dir){
		String testName = ".testFile";
		File test = createUniqueFile(dir, testName);
		try{
			test.createNewFile();
			test.delete();
			return true;
		}catch(Exception e){
			return false;
		}
	}

	private static void initializeLog(File logDir, Level level) {
		
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(level);
		
		if(!logDir.exists()){
			LOG.warn("Log directory {} does not exist. Logs will be written to std.err instead.", logDir);
			return;
		}
		if(!canWriteDirectory(logDir)){
			LOG.warn("Unable to write to log directory {}. Logs will be written to std.err instead.", logDir);
			return;
		}
		
		LoggerContext context = rootLogger.getLoggerContext();
		context.reset();
		
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(context);
		encoder.setPattern(Constants.LOG_PATTERN);
		encoder.start();
		
		RollingFileAppender<ILoggingEvent> rfAppender = new RollingFileAppender<ILoggingEvent>();
		rfAppender.setName("JProbeLogFileAppender");
		rfAppender.setContext(context);
		rfAppender.setEncoder(encoder);
		rfAppender.setFile(logDir.getAbsolutePath() + File.separator + Constants.LOG_NAME);
		
		//might want to change this policy in the future.
		FixedWindowRollingPolicy fwrPolicy = new FixedWindowRollingPolicy();
		fwrPolicy.setContext(context);
		fwrPolicy.setFileNamePattern(logDir.getAbsolutePath() + File.separator + Constants.LOG_NAME_PATTERN);
		fwrPolicy.setMinIndex(1);
		fwrPolicy.setMaxIndex(Constants.MAX_LOGS);
		fwrPolicy.setParent(rfAppender);
		fwrPolicy.start();
		
		SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();
		triggeringPolicy.setMaxFileSize(Constants.MAX_LOG_FILE_SIZE);
		triggeringPolicy.start();
		
		rfAppender.setRollingPolicy(fwrPolicy);
		rfAppender.setTriggeringPolicy(triggeringPolicy);
		rfAppender.start();
		
		rootLogger.addAppender(rfAppender);
		
		rootLogger.debug("Root log initialized.");
		
	}
	
	
}


