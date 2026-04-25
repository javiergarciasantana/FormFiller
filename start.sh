#!/bin/bash

echo "Iniciando monitor fantasma (Xvfb)..."
# Crea una pantalla virtual (:0) con resolución 1024x768
Xvfb :0 -screen 0 1024x768x24 &
export DISPLAY=:0

echo "Iniciando gestor de ventanas (Fluxbox)..."
# Fluxbox permite que tu app de JavaFX tenga barra de título para poder moverla
fluxbox &

echo "Iniciando servidor VNC..."
x11vnc -display :0 -nopw -listen localhost -xkb -ncache 10 -ncache_cr -forever &

echo "Iniciando noVNC (Puente Web)..."
# Convierte el VNC a un WebSocket en el puerto 8080
/usr/share/novnc/utils/launch.sh --vnc localhost:5900 --listen 8080 &

# Dale un par de segundos a la red para establecerse
sleep 2

echo "Arrancando la aplicación JavaFX FormFiller..."
# Ajusta este comando según cómo ejecutes tu proyecto.
# Si usas el plugin de JavaFX en Maven:
mvn javafx:run

# (Si ya generaste un .jar ejecutable con las dependencias, sería algo así:)
# java -jar target/tu-aplicacion.jar