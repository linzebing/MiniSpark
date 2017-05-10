all:
	mvn clean package
	java -Xms4096m -Xmx8192m -jar target/MiniSpark-1.0-SNAPSHOT-fat.jar
