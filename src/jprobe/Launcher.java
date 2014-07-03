package jprobe;

import java.io.File;
import java.io.IOException;

import org.slf4j.LoggerFactory;

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
		
		File jprobeDir = initializeUserDirectory();
		File logDir = initializeLogDirectory(jprobeDir);
		//init the log using the logDir
		initializeLog(logDir);
		
		File cacheDir = initializeFelixCacheDirectory(jprobeDir);
		File userPluginsDir = initializeUserPluginDirectory(jprobeDir);
		File propertiesDir = initializePropertiesDirectory(jprobeDir);
		
		LOG.info("Launching {} - {}", Constants.NAME, Constants.VERSION);
		
		//init the user subdirectories
		initDir(Constants.USER_PLUGINS_DIR);
		initDir(Constants.FELIX_CACHE_DIR);
		initDir(Constants.PREFERENCES_DIR);
		
		
		Configuration config = new Configuration(new File(Constants.CONFIG_FILE), args);
		//System.out.println(JAR_URL);
		//System.out.println(JAR_DIR);
		new JProbe(config);
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

	private static void initializeLog(File logDir) {
		if(!logDir.exists()){
			LOG.warn("Log directory {} does not exist. Logs will be written to std.err instead.", logDir);
			return;
		}
		if(!canWriteDirectory(logDir)){
			LOG.warn("Unable to write to log directory {}. Logs will be written to std.err instead.", logDir);
			return;
		}
		
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		
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
	
	
	
	private static File initDir(String path){
		File f= new File(path);
		if(!f.exists()){
			LOG.info("Initializing directory {}", f);
			f.mkdirs();
		}
		return f;
	}
	
	private static File initFile(String path){
		File f = new File(path);
		if(!f.exists()){
			System.err.println("Initializing file "+f);
			f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.err.println("Error initializing file "+f + ": "+e.getMessage());
			}
		}
		return f;
	}
	
}


