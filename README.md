Jessy
=====

Jessy manages payrolls and payments

Requirements
============

 * Oracle JDK 1.7.0_40 (contains the JavaFX runtime and devkit)
 * Netbeans 7.4
 * Ant 1.9
 
Develop
=======

There are interactive TestNG tests in the test source root, beside unit tests
when appliable.

Tips
====

The following Ant tasks are used to manage the test database (db/jessydb-test

    $ ant migrate-db
    $ ant clean-db
