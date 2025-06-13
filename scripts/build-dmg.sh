#!/bin/bash

jpackage \
  --type dmg \
  --name BananaFormFiller \
  --input target \
  --main-jar FormFiller-1.0-SNAPSHOT-jar-with-dependencies.jar \
  --main-class com.example.formfiller.FormFillerApplication \
  --icon icon.icns \
  --dest dist \
  --app-version 1.0 \
  --vendor "jgsantana" \
  --runtime-image custom-runtime \
  --mac-package-identifier com.example.formfiller \
  --mac-package-name BananaFormFiller \
  --resource-dir Resources \
  --java-options "-Xmx512m -Duser.language=es -Duser.country=ES -Djna.library.path=\$APPDIR/tesseract -Djava.library.path=\$APPDIR/javafx"
