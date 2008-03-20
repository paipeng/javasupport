if [[ $# < 2 ]]; then
	echo "create-springwebapp-hibernate.sh <groupId> <artifactId>"
	exit 1
fi

mvn org.apache.maven.plugins:maven-archetype-plugin:1.0-alpha-7:create \
  -DremoteRepositories=http://faxintelligence.com:8082/archiva/repository/snapshots \
  -DremoteRepositories=http://bb:8082/archiva/repository/connextions \
  -DarchetypeGroupId=javasupport.maven \
  -DarchetypeArtifactId=maven-archetype-springwebapp-hibernate \
  -DarchetypeVersion=0.0.3-SNAPSHOT\
  -DgroupId=$1 \
  -DartifactId=$2