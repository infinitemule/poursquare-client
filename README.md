Storm/Finagle Hackathon Submission - Poursquare 
===============================================

This was my submission to the Storm/Finagle Hackathon which took place at hack/reduce 
in July of 2013.  

This repo contains the code for the client portion of the application.  This includes the 
web application that connects to a MongoDB database and a Spring MVC application that displays some 
statistics and real time checkin map.

Getting Started
---------------

### Prerequisites

Install the [Java SDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 
and [Maven](http://maven.apache.org/download.cgi).

Install an application server.  I used [Tomcat](http://tomcat.apache.org/) but Jetty or Glassfish or
any servlet container will work just fine.

Although I am using Scala, the libraries get bundled into the WAR file, you can deploy it 
just as you would any other WAR file.


### Installing

First, clone the repo onto your local machine.

Next, if you are using Eclipse, you can set up a Dynamic Web Project and have Eclipse automatically publish 
to the server.  If you are using the command line, or just prefer to build a WAR and then deploy it yourself, 
you can call `mvn package` and copy the WAR out of the `target` directory


### Running

Start your app server.  You'll also want to run the Storm topology so that it can populate Mongo.  
Then you can point your browser here to see the most recent checkins:

    http://localhost:8080/poursquare/latest
    
Also check out checkins by countries:

    http://localhost:8080/poursquare/countries
    
And of course, the real time map:

    http://localhost:8080/poursquare/map           

Notes
-----

* Implementing Spring MVC in Scala ain't pretty, but it works.  It was the fastest way for me to write 
a small web app.  [Scalatra](http://www.scalatra.org) might be have been a better fit here.
* I used the Java MongoDB driver and JSON queries.  Again, not pretty but it's simple and works 
just fine.  There is a Scala driver called [Casbah](http://api.mongodb.org/scala/casbah/2.0) that may have been a better fit.


