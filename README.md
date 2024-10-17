# play-java-ebean-example

This is an example Play application that uses Java, and communicates with an in memory database using EBean.

The original GitHub location for this project is inside:

<https://github.com/playframework/play-samples>

## Server backend

By default, the project uses the Pekko HTTP Server backend. To switch to the Netty Server backend, enable the `PlayNettyServer` sbt plugin in the `build.sbt` file.
In the `build.sbt` of this project, you'll find a commented line for this setting; simply uncomment it to make the switch.
For more detailed information, refer to the Play Framework [documentation](https://www.playframework.com/documentation/3.0.x/Server).

## Play

Play documentation is here:

[https://playframework.com/documentation/latest/Home](https://playframework.com/documentation/latest/Home)

## EBean

EBean is a Java ORM library that uses SQL:

[https://www.playframework.com/documentation/latest/JavaEbean](https://www.playframework.com/documentation/latest/JavaEbean)

and the documentation can be found here:

[https://ebean-orm.github.io/](https://ebean-orm.github.io/)


## Exercise

Goal is to have a very simple web shop, where created computers can be added to a shopping basket and can be ordered in a simple checkout process.
 
### Mandatory

 - It has to be possible, to add computers to a shopping basket with the click of a button
 - The quantity of an item (computer) within a basket can be 1 or more 
 - The basket can be displayed, showing all added computers. 
   - items can be removed
   - the quantity can be changed
 - a basket can be submitted to complete the order

### Optional

 - The order has to be persisted in the database, after the shopping basket was submitted
 - Customer information, like address, has to be entered before an order can be submitted 