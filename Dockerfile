FROM gradle:8.8-jdk21 AS build
WORKDIR /home/gradle/project
COPY . .
RUN gradle :app:installDist --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /opt/dogv-agent
COPY --from=build /home/gradle/project/app/build/install/app ./
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENTRYPOINT ["/opt/dogv-agent/bin/app"]
