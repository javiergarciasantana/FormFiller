jlink \
  --module-path $JAVA_HOME_x86/jmods \
  --add-modules java.base,java.desktop,java.logging,javafx.controls \
  --output jre-x86 \
  --strip-debug --no-header-files --no-man-pages --compress=2
