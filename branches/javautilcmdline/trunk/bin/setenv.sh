#
#	Environment settings to be sourced for custom install.
#	$Id: setenv.sh 4 2006-03-16 15:27:19Z zemian $
# :tabSize=2:indentSize=2:
#

# add classpath if exists
if [[ $CLASSPATH ]]; then
	if [[ $LOCALCLASSPATH ]]; then LOCALCLASSPATH=$CLASSPATH:$LOCALCLASSPATH
	else LOCALCLASSPATH=$CLASSPATH; fi
fi

# add development class path if exists
if [[ -e $PROGRAM_HOME/build/classes ]]; then
	if [[ $LOCALCLASSPATH ]]; then LOCALCLASSPATH=$LOCALCLASSPATH:$PROGRAM_HOME/build/classes
	else LOCALCLASSPATH=$PROGRAM_HOME/build/classes; fi
fi
