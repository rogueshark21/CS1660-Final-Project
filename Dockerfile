FROM openjdk:7
COPY --from=python:3.8 / /
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp/src
CMD ["java", "GUIProgram"]