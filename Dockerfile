FROM maven:latest AS build
LABEL authors="Jarmo"

WORKDIR /app
COPY pom.xml /app
COPY . /app

# Package the application with dependencies using the maven-shade-plugin
RUN mvn clean package -DskipTests

# Runtime stage using Eclipse Temurin (JDK 21)
FROM eclipse-temurin:21-jdk


# Install necessary dependencies for JavaFX, X11, and Xvfb
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libgl1-mesa-dri \
    mesa-utils \
    libgl1 \
    libgtk-3-0 \
    libx11-6 \
    libxcb1 \
    libxtst6 \
    libxrender1 \
    xauth \
    x11-apps \
    unzip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Download and unpack JavaFX SDK
RUN wget https://download2.gluonhq.com/openjfx/20.0.1/openjfx-20.0.1_linux-x64_bin-sdk.zip \
    && unzip openjfx-20.0.1_linux-x64_bin-sdk.zip -d /opt \
    && mv /opt/javafx-sdk-20.0.1 /opt/javafx \
    && rm openjfx-20.0.1_linux-x64_bin-sdk.zip

# Fonts for japanese
RUN apt-get update && \
    apt-get install -y fonts-noto-cjk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*



# Set DISPLAY for Xming (will be overridden by docker-compose)
ENV DISPLAY=host.docker.internal:0.0

# Copy the Maven built shaded application (JAR file) from the build stage
COPY --from=build /app/target/main.jar app.jar

CMD ["java", "-Dprism.order=sw", "-Djava.library.path=/opt/javafx/lib", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]

