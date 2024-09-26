FROM amd64/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/Routine-Ade-Server-0.0.1-SNAPSHOT.jar /app/routineAde.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "routineAde.jar"]
