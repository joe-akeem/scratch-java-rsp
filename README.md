# scratch-java-rsp
A Java Remote Sensor Protocol implementation for Scratch 1.4. It provides features for sending and receiving broadcast messages and sensor updates between your application and a running [Scratch](https://scratch.mit.edu/) instance. Visit [this article](http://wiki.scratch.mit.edu/wiki/Remote_Sensors_Protocol) for details about the Scratch 1.4 Remote Sensor Protocol.

## Prerequisites
You need the Java 7 JDK, Maven and of course a running Scratch instance.

## Getting and Building

Clone this repo to your system...
``` shell
git clone https://github.com/joe-akeem/scratch-java-rsp.git
```

...and build it:
``` shell
cd scratch-java-rsp
mvn install
```

This will install the scratch-java-rsp library in your local Maven repository to be used by other projects such as the [Scratch Robot](https://github.com/joe-akeem/scratch-robot)

## Run Demo

### Setting up Scratch

Start Scratch on any computer in your network. In this example we will run Scratch and the demo on the same system (using localhost to connect to Scratch).

Scratch must be set up to accept remote connections. See instructions in the 'Enabling' section of the [this article](http://wiki.scratch.mit.edu/wiki/Remote_Sensor_Connections) on how to do that.

Create a script in scratch that sends a broadcast message and that sets a variable. See instructions [here](http://wiki.scratch.mit.edu/wiki/Broadcast) on how to send broadcast messages.

Start your script in Scratch.

### Run Example class

The example will simply log all received broadcast messages and sensor updates received from Scratch to stdout. Run it on the the same host as Scratch from the command line as follows:

``` shell
mvn exec:java
```

Without any further configuration it will try to connect to Scratch on host 'localhost' and port '42001'. if Scratch is running on another host and/or port you can pass the a host and port to the RemoteSensor and Scratch14Instance classes.

Also have a look at the example: src/main/java/de/joeakeem/scratch/rsp/example/Example.java