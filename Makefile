all:
	mvn clean package
	java -jar target/MiniSpark-1.0-SNAPSHOT-fat.jar
