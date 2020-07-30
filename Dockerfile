# FROM openjdk:11
FROM openjdk:12-alpine
MAINTAINER team020
LABEL version="1.0" author="Florian Sihler" git="https://gitlab.informatik.uni-ulm.de/sopra/ws19-no-time-to-spy/teams/team0020"
# RUN useradd -ms /bin/bash server-user
RUN addgroup -S server-group && adduser -S server-user -G server-group
USER server-user
EXPOSE 7007
VOLUME /home/server-user/data
WORKDIR /home/server-user/data
ADD ./target/game-server.jar /home/server-user/game-server.jar 
ENTRYPOINT [ "java", "-jar", "/home/server-user/game-server.jar"]

