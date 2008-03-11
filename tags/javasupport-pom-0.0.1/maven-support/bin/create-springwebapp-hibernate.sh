if [[ $# < 2 ]]; then
	echo "create-springwebapp-hibernate.sh <groupId> <artifactId>"
	exit 1
fi

mvn archetype:create \
  -DremoteRepositories=http://faxintelligence.com:8082/archiva/repository/internal \
  -DremoteRepositories=http://bb:8082/archiva/repository/connextions \
  -DarchetypeGroupId=javasupport.maven \
  -DarchetypeArtifactId=maven-archetype-springwebapp-hibernate \
  -DarchetypeVersion=0.0.1-SNAPSHOT\
  -DgroupId=$1 \
  -DartifactId=$2