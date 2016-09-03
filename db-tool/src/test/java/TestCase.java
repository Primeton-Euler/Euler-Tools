import com.primeton.euler.db.tool.JarMainClass;

/**
 * Copyright (c) 2001-2016 Primeton Technologies, Ltd.
 * All rights reserved. 
 */

/**
 * @author ZhongWen Li (mailto:lizw@primeton.com)
 *
 */
public class TestCase {
	
	public static void main(String[] args) throws Exception {
		JarMainClass.main(new String[] {});
		JarMainClass.main(new String[] {"help"});
		
		JarMainClass.main(new String[] {"run", "-d", "/root/db.properties", "-s", "SELECT * FROM TEST"});
	}

}
