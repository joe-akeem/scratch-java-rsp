package de.joeakeem.scratch.rsp.example;

import de.joeakeem.scratch.rsp.Scratch14Instance;

/**
 * Hello world!
 *
 */
public class Example 
{
    public static void main( String[] args ) throws InterruptedException
    {
        Scratch14Instance scratch = new Scratch14Instance();
        scratch.registerRemoteSensor(new LoggingSensor());
        Thread t = scratch.connect();
        t.join();
    }
}
