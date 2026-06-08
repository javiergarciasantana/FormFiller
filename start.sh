#!/bin/bash

echo "Iniciando monitor virtual a 512x600..."
Xvfb :0 -screen 0 512x600x24 &
export DISPLAY=:0

echo "Iniciando Matchbox (Gestor de app única)..."
# -use_titlebar no: Elimina la barra superior para que parezca web nativa
matchbox-window-manager -use_titlebar no &

echo "Iniciando servidor VNC (Sin recortes)..."
x11vnc -display :0 -nopw -listen localhost -xkb -forever &

echo "Iniciando noVNC..."
/usr/share/novnc/utils/launch.sh --vnc localhost:5900 --listen 8080 &

sleep 2

echo "Arrancando form-filler..."
mvn javafx:run

# (Si ya generaste un .jar ejecutable con las dependencias, sería algo así:)
# java -jar target/tu-aplicacion.jar