if [[ $# < 3 ]]; then
	echo "create-archetype.sh <archetypeName> <groupId> <artifactId>"
	exit 1
fi

mvn archetype:generate \
  -DremoteRepositories=http://faxintelligence.com:8082/archiva/repository/snapshots \
  -DarchetypeGroupId=javasupport.maven \
  -DarchetypeArtifactId=maven-archetype-$1 \
  -DarchetypeVersion=0.0.4-SNAPSHOT \
  -DgroupId=$2 \
  -DartifactId=$3