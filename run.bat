mvn clean install -Dmaven.test.skip=true & java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -Xms1024M -Xmx30072M -jar target\cimt-ke-eval-0.0.1-SNAPSHOT.jar
