#!/bin/bash
grep -B 3 -A 10 -r "Exception" ./var/logs/cloudLogs/mongodbLog.log > exception.log
