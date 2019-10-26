#!/bin/bash
if [ -z $HY_HOME ]; then
  echo "HY_HOME not set"
  HY_HOME=.
fi

JAVA_OPTS="-Duser.timezone=GMT+8 -server -Xms124m -Xmx2048m -Xloggc:./gc.log"
APP_LOG=$HY_HOME/var/logs/cloudLogs
APP_HOME=$HY_HOME/
APP_MAIN=cn.sensordb2.stcloud.server.Startup

for i in $HY_HOME/lib/*.*; do
CLASSPATH="$CLASSPATH":"$i"
done
PD=0

getPID() {
    javaps=`jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        PID=`echo $javaps | awk '{print $1}'`
    else
        PID=0
    fi
}

startup() {
    getPID
    echo "================================================================================================================"
    if [ $PID -ne 0 ]; then
        echo "cloud already started(PID=$PID)"
        echo "================================================================================================================"
    else
        echo -n "Starting cloud"
        nohup java $JAVA_OPTS -classpath $CLASSPATH $APP_MAIN &
        getPID
        if [ $PID -ne 0 ]; then
            echo "(PID=$PID)...[Success]"
            echo "================================================================================================================"
        else
            echo "[Failed]"
            echo "================================================================================================================"
        fi
    fi
}

startup
