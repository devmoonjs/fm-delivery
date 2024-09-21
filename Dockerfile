FROM openjdk:17-jdk-alpine

# JAR 파일을 이미지에 복사
COPY fm-delivery-0.0.1-SNAPSHOT.jar /app/fm-delivery-0.0.1-SNAPSHOT.jar

# 작업 디렉터리 설정
WORKDIR /app

# 애플리케이션 실행 시 prod 프로필 활성화
ENTRYPOINT ["java", "-jar", "fm-delivery-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
