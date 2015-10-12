package de.joeakeem.scratch.rsp.example;

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
