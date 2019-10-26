#!/bin/bash
tcpNormal=`netstat -tln | grep -E 19090`
if [ -n "$tcpNormal" ] 
then
	echo "tcpNormal"
else
	echo "-tcpNormal"
fi

tcpNormalSsl=`netstat -tln | grep -E 19092`
if [ -n "$tcpNormalSsl" ]
then
        echo "tcpNormalSsl"
else
	echo "-tcpNormalSsl"
fi

tcpTemp=`netstat -tln | grep -E 19091`
if [ -n "$tcpTemp" ] 
then
	echo "tcpTemp"
else
	echo "-TcpTemp"
fi

tcpTempSsl=`netstat -tln | grep -E 19093`
if [ -n "$tcpTempSsl" ]
then
        echo "tcpTempSsl"
else
	echo "-TcpTempSsl"
fi

socketio=`netstat -tln | grep -E 39090`
if [ -n "$socketio" ]
then
        echo "socketio"
else
	echo "-socketio"
fi
socketioSsl=`netstat -tln | grep -E 39091`
if [ -n "$socketioSsl" ]
then
        echo "socketioSsl"
else
	echo "-socketioSsl"
fi

httpApi=`netstat -tln | grep -E 49000`
if [ -n "$httpApi" ]
then
        echo "httpApi"
else
	echo "-httpApi"
fi
httpApiSsl=`netstat -tln | grep -E 49001`
if [ -n "$httpApiSsl" ]
then
        echo "httpApiSsl"
else
	echo "-httpApiSsl"
fi
