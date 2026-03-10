FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy DUY NHẤT file .jar từ giai đoạn 'builder' ở trên xuống đây
# Thay 'guest-service-0.0.1-SNAPSHOT.jar' bằng tên file jar thực tế của bạn
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["top", "-b"]