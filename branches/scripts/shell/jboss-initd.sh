#! /bin/sh

# alternative: http://pby.wikispaces.com/HOWTO+Install+and+Configure+Apache+HTTPD+and+JBoss+AS+on+Red+Hat+Enterprise+Linux?f=print
# cp /apps/jboss-4.2.0.GA/bin/jboss_init_redhat /etc/init.d/jboss
# useradd jboss
# passwd -l jboss
# vi /etc/init.d/jboss, #Set JBOSS_HOME, JBOSS_USER, JAVAPTH, JBOSS_BIND_ADDR to appropriate values
# chkconfig --add jboss
# chkconfig --level 345 jboss on

# http://www.jboss.org/community/docs/DOC-12305
# /etc/rc3.d/S84jboss
# /etc/rc3.d/K15jboss

APP_USER=lmcapp
start(){
        echo "Starting jboss ..."

        # If using an SELinux system such as RHEL 4, use the command below
        # instead of the "su":
        # eval "runuser - jboss -c '/opt/jboss/current/bin/run.sh > /dev/null 2> /dev/null &'
        # if the 'su -l ...' command fails (the -l flag is not recognized by my su cmd) try:
        #   sudo -u jboss /opt/jboss/bin/run.sh > /dev/null 2> /dev/null &
        su -l $APP_USER -c '/opt/jboss/current/bin/run.sh > /dev/null 2> /dev/null &'
}

stop(){
        echo "Stopping jboss ..."

        # If using an SELinux system such as RHEL 4, use the command below
        # instead of the "su":
        # eval "runuser - jboss -c '/opt/jboss/current/bin/shutdown.sh -S &'
        # if the 'su -l ...' command fails try:
        #   sudo -u jboss /opt/jboss/bin/shutdown.sh -S &
        su -l $APP_USER -c '/opt/jboss/current/bin/shutdown.sh -S &'
}

restart(){
        stop
# give stuff some time to stop before we restart
        sleep 60
# protect against any services that can't stop before we restart (warning this kills all Java instances running as 'jboss' user)
        su -l $APP_USER -c 'killall java'
# if the 'su -l ...' command fails try:
        #   sudo -u jboss killall java
        start
}




case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  restart)
        restart
        ;;
  *)
        echo "Usage: jboss {start|stop|restart}"
        exit 1
esac

exit 0

