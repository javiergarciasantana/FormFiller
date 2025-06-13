#!/bin/bash

jlink \
  --module-path "/Users/javiersantana/Java/javafx-sdk-23.0.1/lib:$JAVA_HOME/jmods" \
  --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,jdk.unsupported,java.net.http \
  --output custom-runtime \
  --bind-services \
  --strip-debug \
  --compress=2 \
  --no-header-files \
  --no-man-pages
