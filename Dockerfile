# Usamos una imagen base con Java 17 (ideal para versiones modernas de JavaFX)
FROM eclipse-temurin:21-jdk-jammy

# Evitamos que Debian nos haga preguntas durante la instalación
ENV DEBIAN_FRONTEND=noninteractive

# Instalamos las herramientas de compilación y el "motor gráfico" (Xvfb, VNC, noVNC)
RUN apt-get update && apt-get install -y \
    maven \
    xvfb \
    x11vnc \
    matchbox-window-manager \
    novnc \
    websockify \
    python3-numpy \
    libgtk-3-0 \
    libgl1-mesa-glx \
    libegl1 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

# Creamos la carpeta de trabajo
WORKDIR /app

# Copiamos todo el código al contenedor
COPY . /app

# Compilamos el proyecto (se usa Maven)
# (Si pasamos a Gradle, cambia esta línea a: RUN ./gradlew build)
RUN mvn clean package -DskipTests

# Exponemos el puerto 8080 para que la web pueda ver el streaming de video
EXPOSE 8080

# Copiamos y damos permisos al script de arranque
COPY start.sh /start.sh
RUN chmod +x /start.sh

# Al arrancar, ejecutamos el script que enciende la pantalla fantasma y la app
CMD ["/start.sh"]