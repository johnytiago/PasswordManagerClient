all:
	javac */*.java
run:
	-mkdir keys
	java terminal.ClientTerminal
clean:
	rm -f javac */*.class		
test:
	-mkdir keys
	-rm -r keys/*
	mvn test
