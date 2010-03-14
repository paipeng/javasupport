=====
ABOUT
=====
${artifactId} is about ...

	
===========
DEVELOPMENT
===========
To Setup database
1) cd into project dir
2) mvn hibernate3:ddl

To setup Tomcat server
1) Download tomcat core(zip only) and unzip it to /opt/tomcat

To use NetBeans with maven plugin:
1) Open projects by browse to the directory. NetBeans supports maven project natively.
2) Switch to Services Windows > Servers > Right click Tomcat 6.0 > Properties
   a) In Platform tab > VM Option input, enter this line:
      -D${artifactId}.conf.dir=file:///path_to_your_project/conf/dev
3) Switch to Projects Windows > Right click project > Properties > Run. Select the Tocmat6 Server.
4) Right click project, Run.

To use EclipseIDE with m2eclipse plugin:
1) mvn eclipse:m2eclipse -Dwtpversion=1.5
2) In Eclipse, import existing project and browse to this project root.

3) In Eclipse, Add Server Runtime > Apache>Tomcat6.0 and point to installation dir.
4) In Eclipse Turn off ALL Validation in project preference.

5) Open project properties, select J2EE Module Depenedencies, and check Maven Dependencies. Hit Apply.
6) Open project properties, select Java BuildPath Library Tab, Add Library and Select ServerRuntime Tomcat6.
7) If you still see red X errors marked in your project, tries these:
   a) Referesh your project
   b) Clean your project.
   c) Goto the Problem view, and right click then select "QuickFix".
   d) Close and open project, and reopen Eclipse.
8) In Server view, double click server, click "Open launch configuration" link. In the Environment tab add
   ${artifactId}.conf.dir=file:///path_to_your_project/conf/dev
9) Add your project to the Server view and start the tomcat server inside eclipse.
10) Browse to http://localhost:8080/${artifactId}
