# Bisher noch relativ leer :D

all: target/game-server.jar

target/game-server.jar: pom.xml
	mvn clean install

run: target/game-server.jar
	java -jar target/game-server.jar

# make phony?