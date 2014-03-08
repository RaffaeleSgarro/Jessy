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

To start the application in Netbeans, right click on the project and then "run".
To start the application from the command line, run the task jfxsa-run:

    $ ant jfxsa-run
    
The launcher class is app.Launcher (currently doesn't work)

Tips
====

The following Ant tasks are used to manage the test database (db/jessydb-test

    $ ant migrate-db
    $ ant clean-db
