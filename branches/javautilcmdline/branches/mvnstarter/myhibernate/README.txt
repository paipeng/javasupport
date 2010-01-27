Project Setup:
mvn archetype:create -DgroupId=deng.myhibernate -DartifactId=myhibernate
echo 'create database myhibernate_dev;' | mysql -u root

Running:
mvn compile hibernate3:hbm2ddl
