#!/bin/bash
if [ -z $HY_HOME ]; then
  echo "HY_HOME not set"
  HY_HOME=.
fi

if [ ! -d "$HY_HOME/var" ]; then
  mkdir $HY_HOME/var
fi

if [ ! -d "$HY_HOME/var/logs" ]; then
  mkdir $HY_HOME/var/logs
fi

if [ ! -d "$HY_HOME/var/logs/cloudLogs" ]; then
  mkdir $HY_HOME/var/logs/cloudLogs
fi

if [ ! -d "$HY_HOME/var/logs/cloudLogsEach" ]; then
  mkdir $HY_HOME/var/logs/cloudLogsEach
fi


if [ ! -d "$HY_HOME/var/logs/websocketLogs" ]; then
  mkdir $HY_HOME/var/logs/websocketLogs
fi


if [ ! -d "$HY_HOME/var/logs/httpLogsEach" ]; then
  mkdir $HY_HOME/var/logs/httpLogsEach
fi

