/**
 * Copyright (c) 2001-2016 Primeton Technologies, Ltd.
 * All rights reserved. 
 */
package com.primeton.euler.db.util;

/**
 * @author ZhongWen Li (mailto:lizw@primeton.com)
 *
 */
public class MainUsage {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (null == args || 0 == args.length) {
			help();
			return;
		}
		
	}
	
	/**
	 * Print Usage Help. <br>
	 */
	private static void help() {
		final String CMD_PREFIX = "$ java -jar db-util-" + VERSION.LATEST + ".jar";
		System.out.println("Usage:");
		System.out.println("# Print usage help information:");
		System.out.println(CMD_PREFIX + " -h\n");
		
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
