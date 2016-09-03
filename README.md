# Euler Tools Project  
  
`------------------------------------------------------------------------`  
`Copyright (c) 2001-2016 Primeton Technologies, Ltd. All rights reserved.`  
  
`Author:	ZhongWen (mailto:lizw@primeton.com)`  
`Last update:	2016-08-31`  
`------------------------------------------------------------------------`  
  
  
## Usage  
  
### db-tool  
  
`$ java -jar db-tool-1.0.0.jar help`  
`$ `  
`$ java -jar db-tool-1.0.0.jar exec -d /root/db.properties -f /root/scripts`  
`$ `  
`$ java -jar db-tool-1.0.0.jar exec -d /root/db.properties -f /root/scripts/test.sql`  
`$ `  
`$ java -jar db-tool-1.0.0.jar run -d /root/db.properties -s CREATE TABLE TEST (...)`  
`$ `  
`$ `  
`$ java -jar db-tool-1.0.0.jar set -d /root/db.properties`  
`$ java -jar db-tool-1.0.0.jar exec -f /root/scripts`  
`$ java -jar db-tool-1.0.0.jar exec -f /root/scripts/test.sql`  
`$ java -jar db-tool-1.0.0.jar run -s CREATE TABLE TEST (...)`  
`$ `  
`$ `  
`# `  
`# if not mysql, you can run with JVM arguments '-cp ojdbc14-10.2.0.4.0.jar'`  
`# `  
`$ java -cp ~/.m2/repository/.../${driver}.jar -jar db-tool-1.0.0.jar ...`  
  
  
	`# db.properties`  
  
	jdbc.url=jdbc:mysql://localhost:3306/test?autoReconnect=true&characterEncoding=UTF-8
	jdbc.user=root
	jdbc.password=root
	jdbc.driver=com.mysql.jdbc.Driver
  
  
### ...  