#!/bin/sh
if [ -z $HY_HOME ]; then
  echo "HY_HOME not set"
  HY_HOME=.
fi

APP_MAIN=cn.sensordb2.stcloud.server.Startup
JAVA_HOME=$HY_HOME/jdk8
PID=0

getPID(){
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        PID=`echo $javaps | awk '{print $1}'`
    else
        PID=0
    fi
}

shutdown(){
    getPID
    echo "================================================================================================================"
    if [ $PID -ne 0 ]; then
        echo -n "Stopping cloud(PID=$PID)..."
        kill -9 $PID
        if [ $? -eq 0 ]; then
            echo "[Success]"
            echo "================================================================================================================"
        else
            echo "[Failed]"
            echo "================================================================================================================"
        fi
        getPID
        if [ $PID -ne 0 ]; then
            shutdown
        fi
    else
        echo "cloud is not running"
        echo "================================================================================================================"
    fi
}

shutdown
