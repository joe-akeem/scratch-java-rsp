package de.joeakeem.scratch.rsp;

/**
 * Represents a remote sensor that will be notified by Scratch.
 * 
 * @author joe
 *
 */
public interface RemoteSensor {

	void broadcast(String message);
	
	void sensorUpdate(String name, String value);
	
	void sensorUpdate(String name, double value);
	
	void sensorUpdate(String name, boolean value);
	
	void otherMessage(String message);
}
