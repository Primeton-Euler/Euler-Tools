/**
 * Copyright (c) 2001-2016 Primeton Technologies, Ltd.
 * All rights reserved. 
 */
package com.primeton.euler.db.tool;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * @author ZhongWen Li (mailto:lizw@primeton.com)
 *
 */
public class DbUtils {

	private DbUtils() {
		super();
	}

	/**
	 * 
	 * @param dbcfg
	 * @param sql
	 * @throws SQLException
	 */
	public static void executeSQL(Properties dbcfg, String sql) throws SQLException {
		if (null == dbcfg || dbcfg.isEmpty() || null == sql || sql.isEmpty()) {
			return;
		}
		Connection connection = getConnection(dbcfg);
		if (null == connection) {
			System.err.println("$ Connection is null."); //$NON-NLS-1$
			return;
		}
		ScriptRunner runner = new ScriptRunner(connection);
		runner.setAutoCommit(true);
		Reader reader = null;
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(sql.getBytes("utf-8")); //$NON-NLS-1$
			reader = new InputStreamReader(in, "utf-8"); //$NON-NLS-1$
			runner.runScript(reader);
		} catch (Throwable e) {
			throw new SQLException("$ An error occured while try to execute SQL script.", e); //$NON-NLS-1$
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(in);
			runner.closeConnection();
		}
	}
	
	/**
	 * 
	 * @param dbcfg
	 * @param path SQL file or contain SQL file directory
	 * @throws SQLException
	 */
	public static void executeSQL(Properties dbcfg, File path) throws SQLException {
		if (null == dbcfg || dbcfg.isEmpty() || null == path || !path.exists()) {
			return;
		}
		if (path.isFile()) {
			Connection connection = getConnection(dbcfg);
			if (null == connection) {
				System.err.println("$ Connection is null."); //$NON-NLS-1$
				return;
			}
			ScriptRunner runner = new ScriptRunner(connection);
			runner.setAutoCommit(true);
			Reader reader = null;
			InputStream in = null;
			try {
				in = new FileInputStream(path); //$NON-NLS-1$
				reader = new InputStreamReader(in, "utf-8"); //$NON-NLS-1$
				runner.runScript(reader);
			} catch (Throwable e) {
				throw new SQLException("$ An error occured while try to execute SQL script.", e); //$NON-NLS-1$
			} finally {
				IOUtils.closeQuietly(reader);
				IOUtils.closeQuietly(in);
				runner.closeConnection();
			}
		}
		
		if (path.isDirectory()) {
			File[] children = path.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".sql") || new File(dir, name).isDirectory(); //$NON-NLS-1$
				}
			});
			if (null != children && children.length > 0) {
				Arrays.sort(children);
				for (File file : children) {
					executeSQL(dbcfg, file);
				}
			}
			
		}
	}
	
	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static Connection getConnection(Properties properties) {
		if (null == properties) {
			return null;
		}
		String user = properties.getProperty("jdbc.user"); //$NON-NLS-1$
		String pass = properties.getProperty("jdbc.password"); //$NON-NLS-1$
		String url = properties.getProperty("jdbc.url"); //$NON-NLS-1$
		String driver = properties.getProperty("jdbc.driver"); //$NON-NLS-1$
		
		try {
			Class.forName(driver);
			return null == user && null == pass ? DriverManager.getConnection(url)
					: DriverManager.getConnection(url, user, pass);
		} catch (Throwable t) {
			throw new RuntimeSqlException(t);
		}
	}
	
}
