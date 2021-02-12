FROM adoptopenjdk/maven-openjdk11:latest AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn install -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/kalah-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "kalah-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080