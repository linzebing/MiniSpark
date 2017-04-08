all:
	mvn clean package
	java -Xms16384m -Xmx22384m -jar target/MiniSpark-1.0-SNAPSHOT-fat.jar
