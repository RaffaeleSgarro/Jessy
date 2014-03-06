Jessy
=====

Annotate and report facts about your workers. "Jessy" is just the codename,
because a decent one is difficult, yet naming the project is a task that cannot
be deferred. We'll find a better name later (I hope)

Requirements
============

 * Oracle JDK 1.7.0_40 (contains the JavaFX runtime and devkit)
 * Netbeans 7.4
 * Ant 1.9
 
Develop
=======

The JDBC URL/driver are temporarily hardcoded, while the database name is in
resources/conf/database.properties. The application assumes there's a Derby
network server listening on localhost:1537. To develop, start the Derby server,
then prepare the database (note flyway is configured in its flyway/conf):

    $ flyway\flyway.cmd migrate

To start the application in Netbeans, right click on the project and then "run".
To start the application from the command line, run the task jfxsa-run:

    $ ant jfxsa-run
    
The launcher class is app.Launcher

Tips
====

To clean the database you can use

    flyway clean
