FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean bootJar -x test

CMD ["sh", "-c", "java -jar build/libs/*.jar"]