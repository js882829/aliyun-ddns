#!/bin/bash

script="$0"
while [ -h "$script" ] ; do
  ls=`ls -ld "$script"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    script="$link"
  else
    script=`dirname "$script"`/"$link"
  fi
done

bin=`dirname "$script"`
bin=$(cd "$bin"; pwd)
DDNS_HOME=$(cd "$bin/.."; pwd)

if [ -x "$bin/java_env.sh" ]; then
  . ${bin}/java_env.sh
  echo "use JAVA_HOME : $JAVA_HOME "
  echo "use base CLASSPATH : $CLASSPATH"
  export JAVA_HOME="$JAVA_HOME"
  export CLASSPATH="$CLASSPATH"
fi
if test -n "${JAVA_HOME}"; then
  if test -z "${JAVA_EXE}"; then
    JAVA_EXE="$JAVA_HOME/bin/java"
  fi
fi
if test -z "${JAVA_EXE}"; then
  JAVA_EXE=java
fi
for f in `ls ${DDNS_HOME}/lib`
do
  CLASSPATH=${CLASSPATH}:${DDNS_HOME}/lib/${f}
done

cd "${DDNS_HOME}"
nohup ${JAVA_EXE}\
 -Dlogback.configurationFile=${DDNS_HOME}/conf/logback.xml\
 -Dddns.home=${DDNS_HOME}\
 -Dddns.provider.name="ipEcho"\
 -cp ${CLASSPATH}\
 cn.howardliu.aliyun.ddns.runner.Runner ${DDNS_HOME}/conf/config.json > /dev/null 2>&1 &\
 echo $! > ${DDNS_HOME}/ddns.pid