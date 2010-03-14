SCRIPT_DIR=`dirname $0`
CP="$(wpath $SCRIPT_DIR);$(wpath $SCRIPT_DIR/*.jar)" # 2+ classpath elements is needed!
DATA_DIR=`wpath $SCRIPT_DIR/../data`
java -cp $CP org.h2.tools.Server -baseDir $DATA_DIR "$@"

