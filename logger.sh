#!/bin/sh
# David Lynch
# A shell script to start and stop a daemon.

# Path to the JSVC facility
EXEC=/usr/bin/jsvc
# This should be a Java 7 JDK
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
# Assuming this script is executes from the same directory as the jar file
CLASS_PATH="/usr/share/java/commons-daemon.jar":"/opt/logdaemon/dit-logdaemon-1.0-jar-with-dependencies.jar"
# The main class to execute
CLASS=ie.dit.logdaemon.LogDaemon
USER=dave
PID=/opt/logdaemon/logger.pid
LOG_OUT=/opt/logdaemon/logger.out
LOG_ERR=/opt/logdaemon/logger.err

do_exec()
{
    $EXEC -home "$JAVA_HOME" -cp $CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS
}

case "$1" in
    start)
        echo "Starting Logger daemon..."
        echo "jsvc path         $EXEC"
        echo "JAVA_HOME         $JAVA_HOME"
        echo "Main class        $CLASS"
        echo "Running as user   $USER"
        echo "PID file          $PID"
        echo "Error log         $LOG_ERR"
        echo "STDOUT log        $LOG_OUT"
        do_exec
        echo "...Logger daemon started"
            ;;
    stop)
        do_exec "-stop"
            ;;
    restart)
        if [ -f "$PID" ]; then
            do_exec "-stop"
            do_exec
        else
            echo "service not running, will do nothing"
            exit 1
        fi
            ;;
    *)
            echo "usage: daemon {start|stop|restart}" >&2
            exit 3
            ;;
esac
