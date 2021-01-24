scan:
	mvn clean verify sonar:sonar
build:
	mvn clean package
run:
	mvn clean package -DskipTests && cd application/target && java -jar application-1.0.1.jar
