#!/bin/bash
if [ -z $HY_HOME ]; then
  echo "HY_HOME not set"
  $HY_HOME=.
fi

$HY_HOME/cloud/shells/restartup.sh

echo "sleep 5"
sleep 5
$HY_HOME/viewPortNetstat.sh
