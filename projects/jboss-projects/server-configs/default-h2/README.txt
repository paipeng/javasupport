1. Startup H2 DB in Server Mode:
dengz1@ORLIW7MNDC91:/js/h2-2009-12-06
$ bin/run-server-web.sh

2. Setup JBoss

dengz1@ORLIW7MNDC91:/jb/server
$ cp -rf default default-h2
$ cp /js/h2-2009-12-06/bin/h2-1.2.125.jar default-h2/lib
$ bak default-h2/hsqldb-ds.xml
$ e default-h2/deploy/h2-ds.xml
    <?xml version="1.0" encoding="UTF-8"?>
    <datasources>
        <local-tx-datasource>
        <jndi-name>DefaultDS</jndi-name>
        <connection-url>jdbc:h2:tcp://localhost/jboss</connection-url>
        <driver-class>org.h2.Driver</driver-class>
        <user-name>sa</user-name>
        <password></password>
        <!-- Can't find one online for H2DB type-mapping, let's use HSQLDB for now and it seems to work! -->
        <metadata>
          <type-mapping>Hypersonic SQL</type-mapping>
        </metadata>
        </local-tx-datasource>
    </datasources>
$ /jb/bin/run.sh -c default-h2

3. Verify the database tables created by JBoss server (Use the browser opened during h2db startup.)
   JDBC URL: jdbc:h2:jboss
   * The database file should be in $H2_HOME/data/jboss.db
