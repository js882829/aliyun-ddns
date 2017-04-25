#!/bin/bash
USER=liuxh
DIR="/home/liuxh/ddns"
PID_FILE="$DIR/ddns.pid"
COUNT="0"
if [ -f $PID_FILE ]; then
  PID=`cat $PID_FILE`
  COUNT=`ps -f -p $PID|grep $USER|grep java|grep ddns|wc -l`
fi
if [ $COUNT == "0" ]; then
  cd $DIR
  /home/liuxh/ddns/bin/startup.sh
fi