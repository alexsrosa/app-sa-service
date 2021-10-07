##############################
## Build a release artifact. ##
FROM alexsrosa/gradle-7.2-app-sa-service-cache:latest as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

#############################
## Create image with build ##
FROM openjdk:11.0.12-slim

ARG PROFILES_DEFAULT="default"
ENV PROFILES=$PROFILES_DEFAULT

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

# Copy the jar to the production image from the builder stage.
COPY --from=builder /home/gradle/src/build/libs/* ./app.jar
EXPOSE 8080

# Run the web service on container startup.
CMD [ "java", "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar" ]