#!/bin/bash

rm -f /tmp/.X0-lock /tmp/.X11-unix/X0

xpra start :0 \
  --bind-tcp=0.0.0.0:8080 \
  --html=on \
  --start-child="mvn javafx:run" \
  --exit-with-children=yes \
  --daemon=no \
  --encoding=x264 \
  --dpi=96 \
  --desktop-scaling=off
