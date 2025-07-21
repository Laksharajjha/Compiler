FROM openjdk:17

# Install Python and C++
RUN apt-get update && \
    apt-get install -y python3  && \
apt install build-essential && \
    apt-get clean

RUN useradd -m judgeuser
USER judgeuser

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/judgecompiler-0.0.1-SNAPSHOT.jar"]
