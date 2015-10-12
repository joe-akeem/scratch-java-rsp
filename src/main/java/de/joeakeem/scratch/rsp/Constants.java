package de.joeakeem.scratch.rsp;

/**
 * Contains constants used in this package only.
 * 
 * @author joe
 *
 */
class Constants {

	/**
	 * The message type for broadcast messages as specified
	 * here: <a href="http://wiki.scratch.mit.edu/wiki/Remote_Sensors_Protocol">Scratch Remote Sensor Protocol</a>
	 */
	static final String BROADCAST_MESSAGE_TYPE = "broadcast";
	
	/**
	 * The meassage type for sensor update messages as specified
	 * here: <a href="http://wiki.scratch.mit.edu/wiki/Remote_Sensors_Protocol">Scratch Remote Sensor Protocol</a>
	 */
	static final String SENSOR_UPDATE_MESSAGE_TYPE = "sensor-update";
	
	/**
	 * The default host the Scratch instance is listening on.
	 */
	static final String SCRATCH_DEFAULT_HOST = "localhost";
	
	/**
	 * The default port the Scratch instance is listening on.
	 */
	static final int SCRATCH_DEFAULT_PORT = 42001;
}
