package jprobe;

import java.io.File;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

public class Launcher {
	
	private static org.slf4j.Logger LOG;
	
	public static void main(String[] args){
		
		//init the user's jprobe directory
		initUserDirectory();
		//init the log
		initLog();
		LOG = LoggerFactory.getLogger(Launcher.class);
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


	private static void initLog() {
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		
		LoggerContext context = rootLogger.getLoggerContext();
		context.reset();
		
		RollingFileAppender<ILoggingEvent> rfAppender = new RollingFileAppender<ILoggingEvent>();
		rfAppender.setContext(context);
		rfAppender.setFile(Constants.LOG_DIR + Constants.LOG_NAME);
		
		//might want to change this policy in the future.
		FixedWindowRollingPolicy fwrPolicy = new FixedWindowRollingPolicy();
		fwrPolicy.setContext(context);
		fwrPolicy.setFileNamePattern(Constants.LOG_DIR + Constants.LOG_NAME_PATTERN);
		fwrPolicy.setMinIndex(1);
		fwrPolicy.setMaxIndex(Constants.MAX_LOGS);
		fwrPolicy.setParent(rfAppender);
		fwrPolicy.start();
		
		SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();
		triggeringPolicy.setMaxFileSize(Constants.MAX_LOG_FILE_SIZE);
		triggeringPolicy.start();
		
		rfAppender.setRollingPolicy(fwrPolicy);
		rfAppender.setTriggeringPolicy(triggeringPolicy);
		
		rootLogger.addAppender(rfAppender);
		
		rootLogger.debug("Root log initialized.");
		
	}



	private static void initUserDirectory() {
		File jprobeDir = initDir(Constants.USER_JPROBE_DIR);
		if(!jprobeDir.exists()){
			System.err.println("Error initializing directory "+jprobeDir);
			System.err.println("Trying in working directory...");
			Constants.USER_JPROBE_DIR = "jprobe";
			jprobeDir = initDir(Constants.USER_JPROBE_DIR);
			if(jprobeDir.exists()){
				System.err.println("Success.");
			}else{
				System.err.println("Cannot initialize directory "+jprobeDir);
				System.err.println("Exiting");
				System.exit(-1);
			}
			//update all files and dirs to use new user dir
			Constants.LOG_DIR = Constants.USER_JPROBE_DIR + File.separator + "logs" + File.separator;
			Constants.USER_PLUGINS_DIR = Constants.USER_JPROBE_DIR + File.separator + "plugins";
			Constants.FELIX_CACHE_DIR = Constants.USER_JPROBE_DIR + File.separator + "cache";
			Constants.PREFERENCES_DIR = Constants.USER_JPROBE_DIR + File.separator + "preferences";
			Constants.CONFIG_FILE = Constants.PREFERENCES_DIR + File.separator + "jprobe.pref";
		}
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


