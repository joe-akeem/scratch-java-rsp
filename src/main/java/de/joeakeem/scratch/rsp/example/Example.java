package de.joeakeem.scratch.rsp.example;

import de.joeakeem.scratch.rsp.RemoteSensor;

/**
 * Hello world!
 *
 */
public class Example 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	LoggingSensor sensor = new LoggingSensor();
        Thread t = sensor.connect();
        t.join();
    }
}
