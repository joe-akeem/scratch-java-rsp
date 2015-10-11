package de.joeakeem.scratch.rsp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Represents a Scratch 1.4 instance to which broadcast messages and
 * sensor updates can be sent.
 * 
 * @author joe
 *
 */
public class Scratch14Instance {
	
	/**
	 * the meassage type for broadcast messages as specified
	 * here: http://wiki.scratch.mit.edu/wiki/Remote_Sensors_Protocol
	 */
	private static final String BROADCAST_MESSAGE_TYPE = "broadcast"; 
	
	/**
	 * The host Scratch is listening on for remote sensor connections. Defaults to "localhost".
	 */
	private String scratchHost = "localhost";
	
	/**
	 * The port Scratch is listening on for remote sensor connections. Defaults to 42001.
	 */
	private int scratchPort = 42001;
	
	/**
	 * The socket that is used to connect to Scratch. This will be
	 * initialized by the connect() method.
	 */
	private Socket socket;
	
	/**
	 * The output stream used to send messages to Scratch. This will be
	 * initialized by the connect() method.
	 */
	private OutputStream outputStream;
	
	/**
	 * Connects to the remote Scratch instance specified by this instances Scratch host and port.
	 * Once this instance is connected to Scratch it can start sending messages to Scratch.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void connect() throws UnknownHostException, IOException {
		socket = new Socket(scratchHost, scratchPort);
		outputStream = socket.getOutputStream();
	}

	/**
	 * Disconnects from the remote Scratch instance.
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (outputStream != null) {
			outputStream.close();
		}
		if (socket != null) {
			socket.close();
		}
	}
	
	/**
	 * Sends a broadcast message to the remote Scratch instance.
	 * 
	 * @param message - the message to be sent to Scratch
	 * @throws IOException 
	 */
	public void broadcast(String message) throws IOException {
		String broadcastMessage = BROADCAST_MESSAGE_TYPE + " " + "\"" + message + "\"";
		byte[] messageSize = ByteBuffer.allocate(4).putInt(broadcastMessage.length()).array();
		outputStream.write(messageSize);
		outputStream.write(broadcastMessage.getBytes(Charset.forName("UTF-8")));
	}

	/**
	 * Sends a sensor update message to the remote Scratch instance.
	 * 
	 * @param name - the remote variable to update
	 * @param value - the value to set the variable to
	 * @throws IOException
	 */
	public void sensorUpdate(String name, String value) throws IOException {
		// TODO:
	}
	
	/**
	 * Sends a sensor update message to the remote Scratch instance.
	 * 
	 * @param name - the remote variable to update
	 * @param value - the value to set the variable to
	 * @throws IOException
	 */
	public void sensorUpdate(String name, double value) throws IOException {
		// TODO:
	}
	
	/**
	 * Sends a sensor update message to the remote Scratch instance.
	 * 
	 * @param name - the remote variable to update
	 * @param value - the value to set the variable to
	 * @throws IOException
	 */
	public void sensorUpdate(String name, boolean value) throws IOException {
		// TODO:
	}

	public String getScratchHost() {
		return scratchHost;
	}

	public void setScratchHost(String scratchHost) {
		this.scratchHost = scratchHost;
	}

	public int getScratchPort() {
		return scratchPort;
	}

	public void setScratchPort(int scratchPort) {
		this.scratchPort = scratchPort;
	}
}
