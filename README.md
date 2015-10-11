# scratch-java-rsp
A Java Remote Sensor Protocol Implementation for Scratch 1.4.

## Prerequisites
You need Java 7 JDK and Maven.

## Getting and Building

Clone this repo to your Raspberry Pi...
``` shell
git clone https://github.com/joe-akeem/scratch-java-rsp.git
```

...and build it:
``` shell
cd scratch-java-rsp
sudo /opt/apache-maven-3.3.3/bin/mvn install

This will install the scratch-java-rsp library in your maven Repository to be used by other projects such as the [Scratch Robot](https://github.com/joe-akeem/scratch-robot)

## Run Demo

### Setting up Scratch

Start Scratch on any computer in your network. In this example we will run Scratch and the demo on the same system (using localhost to connect to Scratch).

Scratch must be set up to accept remote connections. See instructions in the 'Enabling' section of the [this article](http://wiki.scratch.mit.edu/wiki/Remote_Sensor_Connections) on how to do that.

Create a script in scratch that sends a broadcast message and that sets a variable. See instructions [here](http://wiki.scratch.mit.edu/wiki/Broadcast) on how to send broadcast messages.

Start your script in Scratch.

### Run Example class

The example will simply log all received broadcast messages and sensor updates received from Scratch to stdout.
