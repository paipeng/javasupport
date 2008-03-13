if [[ $# < 2 ]]; then
	echo "create-webapp.sh <groupId> <artifactId>"
	exit 1
fi

mvn org.apache.maven.plugins:maven-archetype-plugin:1.0-alpha-7:create \
  -DremoteRepositories=http://faxintelligence.com:8082/archiva/repository/internal \
  -DarchetypeGroupId=javasupport.maven \
  -DarchetypeArtifactId=maven-archetype-webapp \
  -DarchetypeVersion=0.0.2-SNAPSHOT \
  -DgroupId=$1 \
  -DartifactId=$2 \