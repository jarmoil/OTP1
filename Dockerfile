FROM maven:latest
LABEL authors="Jarmo"

WORKDIR /app
COPY pom.xml /app
COPY . /app

# Download and set up JavaFX
RUN apt-get update && \
    apt-get install -y wget unzip libx11-6 libxext6 libxrender1 libxtst6 libxi6 libfreetype6 libgtk-3-0 && \
    wget https://download2.gluonhq.com/openjfx/20.0.1/openjfx-20.0.1_linux-x64_bin-sdk.zip && \
    unzip openjfx-20.0.1_linux-x64_bin-sdk.zip && \
    mv javafx-sdk-20.0.1 /opt/javafx && \
    rm openjfx-20.0.1_linux-x64_bin-sdk.zip

RUN apt-get update && \
    apt-get install -y xvfb


# Set environment variable for X11 display
ENV DISPLAY=:99

RUN mvn package
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & java -Djava.library.path=/opt/javafx/lib --module-path /opt/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/main.jar"]