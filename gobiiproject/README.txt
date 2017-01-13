Project dependencies:

gobii-web -------> |
                   | gobii-domain -----> gobii-dao
gobii-process ---> |        |               |
-------------------         |               |
        |                   |               |
        |                   |               |
        |                   |               |
        |                   |               |
       \|/                 \|/             \|/
       ---------------------------------------
                      gobii-model

The most important thing to know about the the module structure is that
all business logic and data crunching should live in the domain layer. The
lingua franca for data representation are the dto objects that are defined
in the model project. In this application, pojos that represent actual database
entities are never exposed outside of the dao layer: when the data representation
changes, the change should not cascade throughout the system. Therefore, it is the
job of the dao layer to map the data representation from the data store into the
the dto classes defined in the model. So model project is exposed to all layers
of the system. However, the dao layer should be consumed only by the domain layer,
whose job it is to massage the data provided by the dao layer.

The next most important thing to know about this project is that it uses the Spring
frameowrk. Accordingly, the application-config.xml contains bean definitions that
make it easy to swap arbitrary implementations of interfaces. Thus, for example,
the DAO layer functionality exposed to the domain layer through interfaces.
Implementations of the interfaces for a particular data source (e.g., postgres)
can be swapped for implementations for another data source (e.g., mongo) just by
modifying the bean definitions application-config.xml.

There are two separate application-config.xml files, one for gobii-process and one
for gobii-web. In principle, they should be identical. However, since the
extractor and loader development may follow a different development trajectory,
it seems useful to keep them separate for now.

The mechanism for using the Spring injection container is:
 * Define a <bean/> in application-config.xml;
 * Use the bean with the @Autowire annotation.
 * In the Extractor and Loader processes, you will need to use the
   Spring application context to retrieve instances of classes from
   the injection container, as follows:

   MarkerService markerService = springContextLoader.getApplicationContext().getBean(MarkerService.class);

   If you don't retrieve the class in this way, any instance references with the @AutoWire
   annotation will be null. The same is true of the web application: whenever you instance a
   class with the new keyword, none of the injected dependencies will be set.


Setup instructions:
1) Import the project at the parent pom level into your ide;
2) Run Maven install and import the maven types into your IDE;
3) The Maven compile configuration is set to Java 1.8, so you should have the JDK 1.8
   installed on your system, and configure your IDE to use JDK 1.8 for gobiiproject.
3) Set up a tomcat 7.0 maven run configuration in your IDE:
    * Use conference of the conf directory in the gobiiproject-conf.zip in this
      project for the conf directory of the tomcat instance;
    * Deploy the war produced by the gobii-web project;
    * When deployed and running, the following REST call should produce a
      meaningful result:
      curl -i -H "Accept: application/json" -H "Content-Type: application/json"  -H "Cache-Control: no-cache, no-store, must-revalidate" -d "{\"name\":\"article\",\"scope\":\"dumb scope\"}" http://localhost:8181/resource/search/bycontenttype

4) Set up a run configuration for org/gobiiproject/gobiiprocess/ExtractorProcess.java;
   when you run it, you should see that the service call it uses logs a meaningful result.

5) Set up a maven run configuration in the dao project as follows:

   Commandline: clean hibernate3:hbm2hbmxml hibernate3:hbm2java compile
   Working directory: <physical-path-to-module> (e.g., C:/phil-source/IntelliJ/gobiiproject/gobii-dao)

   In order for this to work, you also have to modify resources/hibernate.properties in order
   connect to the database. Upon completion, this run task will create jpa/hibernate entity
   classes from the database tables.

   After you run the task, you will also have to do Generate Sources/Update Folders in order
   for the classes to be seen by the IDE compiler.



