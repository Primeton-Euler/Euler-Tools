/**
 * Copyright (c) 2001-2016 Primeton Technologies, Ltd.
 * All rights reserved. 
 */
package com.primeton.euler.db.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author ZhongWen Li (mailto:lizw@primeton.com)
 *
 */
public class JarMainClass {
	
	public static final String ACTION_EXEC = "exec";
	public static final String ACTION_RUN = "run";
	public static final String ACTION_SET = "set";
	public static final String ACTION_HELP = "help";
	
	private static String USER_DIRECTORY = System.getProperty("user.home");
	
	private static final String CMD_PREFIX = "$ java -jar db-tool-" + VERSION.LATEST + ".jar";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (null == args || 0 == args.length) {
			help();
			return;
		}
		final String action = null == args[0] ? null : args[0].trim();
		if (null == action) {
			help();
			return;
		} else if (ACTION_EXEC.equalsIgnoreCase(action)) {
			doExec(args);
		} else if (ACTION_RUN.equalsIgnoreCase(action)) {
			doRun(args);
		} else if (ACTION_SET.equalsIgnoreCase(action)) {
			doSet(args);
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	private static void doSet(String[] args) throws IOException {
		if (null == args || args.length < 3) {
			System.out.println("$ Usage:"); //$NON-NLS-1$
			System.out.println(CMD_PREFIX + " set -d /root/db.properties"); //$NON-NLS-1$
			return;
		}
		String path = getArgValue(args, "-d", false); //$NON-NLS-1$
		if (null == path || path.isEmpty()) {
			help();
			return;
		}
		File f = new File(path);
		if (f.exists() && f.isFile()) {
			File target = new File(getDefaultDbFile());
			if (target.exists()) {
				FileUtils.forceDelete(target);
				target = new File(getDefaultDbFile());
			}
			FileUtils.copyFile(f, target); //$NON-NLS-1$
			System.out.println("$ Set default database jdbc connection configuration file success.");
		} else {
			System.err.println("$ " + path + " not exist or is not a file."); //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 * @param args
	 * @param key
	 * @return
	 */
	protected static String getArgValue(String[] args, String key) {
		return getArgValue(args, key, true);
	}
	
	/**
	 * 
	 * @param args
	 * @param key
	 * @return
	 */
	protected static String getArgValue(String[] args, String key, boolean ifNoneValueReturnKey) {
		if (null == args || 0 == args.length || null == key || key.isEmpty()) {
			return null;
		}
		for (int i = 0; i < args.length - 1; i++) {
			if (key.equals(args[i])) {
				if (i == args.length - 1) {
					return key;
				} else {
					return args[i + 1];
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	private static Properties getDbProperties(File f) throws IOException {
		if (null == f || !f.exists() || !f.exists()) {
			return null;
		}
		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(f);
			properties.load(in);
			return properties;
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private static void doRun(String[] args) throws SQLException, IOException {
		if (null == args || args.length < 3) { //$NON-NLS-1$
			System.out.println("$ Usage:"); //$NON-NLS-1$
			System.out.println(CMD_PREFIX + " run -d /root/db.properties -s \"SELECT * FROM TEST\""); //$NON-NLS-1$
			System.out.println(CMD_PREFIX + " run -s \"SELECT * FROM TEST\""); //$NON-NLS-1$
			return;
		}
		String sql = getArgValue(args, "-s", false); //$NON-NLS-1$
		String dbPath = getArgValue(args, "-d", false); //$NON-NLS-1$
		System.out.println("$ " + sql);
		System.err.println("$ " + dbPath);
		if (null == sql || sql.isEmpty()) {
			help();
			return;
		}
		if (null == dbPath || dbPath.isEmpty()) {
			dbPath = getDefaultDbFile();
		}
		File f = new File(dbPath);
		if (f.exists() && f.isFile()) {
			DbUtils.executeSQL(getDbProperties(f), sql);
		} else {
			help();
			return;
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private static void doExec(String[] args) throws SQLException, IOException {
		if (null == args || args.length < 3) {
			System.out.println("$ Usage:"); //$NON-NLS-1$
			System.out.println(CMD_PREFIX + " exec -f /root/scripts"); //$NON-NLS-1$
			System.out.println(CMD_PREFIX + " exec -f /root/scripts/test.sql"); //$NON-NLS-1$
			System.out.println(CMD_PREFIX + " exec -d /root/db.properties -f /root/scripts"); //$NON-NLS-1$
			System.out.println(CMD_PREFIX + " exec -d /root/db.properties -f /root/scripts/test.sql"); //$NON-NLS-1$
			return;
		}
		String sqlPath = getArgValue(args, "-f", false); //$NON-NLS-1$
		String dbPath = getArgValue(args, "-d", false); //$NON-NLS-1$
		System.out.println(sqlPath);
		System.out.println(dbPath);
		if (null == sqlPath || sqlPath.isEmpty()) {
			help();
			return;
		}
		if (null == dbPath || dbPath.isEmpty()) {
			dbPath = getDefaultDbFile();
		}
		File f = new File(dbPath);
		if (f.exists() && f.isFile()) {
			DbUtils.executeSQL(getDbProperties(f), new File(sqlPath));
		} else {
			help();
			return;
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	private static String getUserDirectory() {
		return null == USER_DIRECTORY ? "/tmp/euler" : USER_DIRECTORY; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @return
	 */
	private static String getDefaultDbFile() {
		return getUserDirectory() + File.separator + "db.properties"; //$NON-NLS-1$
	}

	/**
	 * Print Usage Help. <br>
	 */
	private static void help() {
		System.out.println("Usage:");
		System.out.println("# Print usage help information:");
		System.out.println(CMD_PREFIX + " help\n");
		
		System.out.println("# execute sql files:");
		System.out.println(CMD_PREFIX + " exec -d /root/db.properties -f /root/scripts");
		System.out.println(CMD_PREFIX + " exec -d /root/db.properties -f /root/scripts/test.sql");
		
		System.out.println("\n# execute sql statement:");
		System.out.println(CMD_PREFIX + " run -d /root/db.properties -s \"CREATE TABLE TEST (...)\"");
		
		System.out.println("\n# or use default database connection configuration:\n");
		
		System.out.println(CMD_PREFIX + " set -d /root/db.properties");
		System.out.println(CMD_PREFIX + " exec -f /root/scripts");
		System.out.println(CMD_PREFIX + " exec -f /root/scripts/test.sql");
		System.out.println(CMD_PREFIX + " run -s \"SELECT * FROM TEST\"");
		
		System.out.println("\n\n# Example: db.properties");
		System.out.println("----------------------------Begin---------------------------------------------------");
		System.out.println("jdbc.url=jdbc:mysql://localhost:3306/test?autoReconnect=true&characterEncoding=UTF-8");
		System.out.println("jdbc.user=root");
		System.out.println("jdbc.password=root");
		System.out.println("jdbc.driver=com.mysql.jdbc.Driver");
		System.out.println("------------------------------End---------------------------------------------------");
		
		System.out.println("\n\nTo see more https://github.com/Primeton-Euler/Euler-Tools");
	}

}
