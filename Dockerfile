FROM openjdk:21-jdk-slim

WORKDIR /intershop

EXPOSE 8080

ENV HOME_DIR /intershop
ENV JAR_FILE app.jar

COPY build/libs/$JAR_FILE $HOME_DIR/

COPY script-start.sh $HOME_DIR/
COPY .env $HOME_DIR/

RUN chmod -R +x $HOME_DIR/script-start.sh

WORKDIR $HOME_DIR

ENTRYPOINT ["./script-start.sh"]