if [[ $# < 3 ]]; then
	echo "create-archetype.sh <archetypeName> <groupId> <artifactId>"
	exit 1
fi

mvn org.apache.maven.plugins:maven-archetype-plugin:1.0-alpha-7:create \
  -DremoteRepositories=http://faxintelligence.com:8082/archiva/repository/snapshots \
  -DarchetypeGroupId=javasupport.maven \
  -DarchetypeArtifactId=maven-archetype-$1 \
  -DarchetypeVersion=0.0.3-SNAPSHOT \
  -DgroupId=$2 \
  -DartifactId=$3