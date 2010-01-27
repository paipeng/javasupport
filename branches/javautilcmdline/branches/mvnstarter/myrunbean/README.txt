Project Setup:
mvn archetype:create -DgroupId=deng.myrunbean -DartifactId=myrunbean

Running:
mvn -q exec:java -DappHome=. -Dexec.mainClass=deng.myrunbean.RunBean -Dexec.args="conf/beans.xml main"
