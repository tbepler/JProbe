package jprobe.system.osgi;

import java.io.File;

import jprobe.system.launcher.Launcher;

public class OsgiConstants {
	
	public static final String URL_PATH_SEPARATOR = "/";
	
	public static final String JAR_URL = Launcher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	public static final String JAR_DIR = JAR_URL.substring(0, JAR_URL.lastIndexOf(URL_PATH_SEPARATOR));
	
	public static final String PLUGIN_AUTODEPLOY = JAR_DIR + File.separator + "plugins";
	
	public static final String USER_PLUGINS_DIR_NAME = "plugins";
	public static final String FELIX_CACHE_DIR_NAME = ".felix-bundle-cache";
	
	public static final String FELIX_EXPORT_PACKAGES = "jprobe.services;version=1.0.0," +
			"jprobe.services.data;version=1.0.0," +
			"jprobe.services.function;version=1.0.0,"
			+ "jprobe.services.function.components;version=1.0.0,"
			//export the crossplatform library
			+ "bepler.crossplatform;version=1.0.0,"
			//export the slf4j library
			+ "org.slf4j;version=1.7.7,"
			+ "util.progress;version=1.0.0,"
			+ "util.gui;version=1.0.0,"
			+ "util;version=1.0.0,"
			+ "util.logging;version=1.0.0,"
			+ "util.genome;version=1.0.0,"
			+ "util.genome.reader;version=1.0.0,"
			+ "util.genome.reader.query;version=1.0.0,"
			+ "util.genome.reader.threaded;version=1.0.0,"
			+ "util.genome.peak;version=1.0.0,"
			+ "util.genome.kmer;version=1.0.0,"
			+ "util.genome.probe;version=1.0.0,"
			+ "util.genome.pwm;version=1.0.0,"
			+ "util.xmlserializer;version=1.0.0";
	
	public static final String FELIX_BOOTDELEGATION_PACKAGES = "javax.swing,javax.swing.*";
	
	public static final String FELIX_AUTODEPLOY_ACTION = "install,start";
	
	public static final String FELIX_FILE_INSTALL_DIR_PROP = "felix.fileinstall.dir";
	public static final String FELIX_FILE_INSTALL_INITIALDELAY_PROP = "felix.fileinstall.noInitialDelay";
	public static final String FELIX_INITIALDELAY = "true";
	
}
