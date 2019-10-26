#!/bin/bash
if [ -z $HY_HOME ]; then
  echo "HY_HOME not set"
  HY_HOME=.
fi

JAVA_HOME=$HY_HOME/jdk8
JAVA_OPTS="-Duser.timezone=GMT+8 -server -Xms124m -Xmx2048m -Xloggc:./gc.log"
APP_HOME=$HY_HOME/
APP_MAIN=com.rbcloudtech.hycloud.client.InteractiveClient

for i in $HY_HOME/lib/*.*; do
CLASSPATH="$CLASSPATH":"$i"
done

#userName:$1 password:$2
#userID_1a 1
$JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAIN $1 $2
