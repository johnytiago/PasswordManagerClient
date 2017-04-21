all:
	javac */*.java
run:
	java terminal.ClientTerminal
clean:
	rm -f javac */*.class		
test:
	-rm -r keys/*
	mvn test
