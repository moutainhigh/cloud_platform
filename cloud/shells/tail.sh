#!/bin/bash
if [ -z $HY_HOME ]; then
  echo "HY_HOME not set"
  HY_HOME=.
fi

tail $HY_HOME/var/logs/cloudLogs/nohup.log
