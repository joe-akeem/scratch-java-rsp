package de.joeakeem.scratch.rsp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a remote instance of the Scratch 1.4 programming environment
 * that listens for remote sensor connections on a specified host and port.
 * When no host and port are specified instances of this class will try to
 * connect to localhost:42001.
 * 
 * @see also http://wiki.scratch.mit.edu/wiki/Remote_Sensors_Protocol
 * 
 * @author joe
 *
 */
public class Scratch14Instance implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(Scratch14Instance.class);
	
	private static final String BROADCAST_MESSAGE_TYPE = "broadcast";
	
	private static final String SENSOR_UPDATE_MESSAGE_TYPE = "sensor-update";
	
	enum ParserState {IDENTIFY_MESSAGE_TYPE, BROADCAST_MESSAGE, READ_VARIABLE_NAME, READ_VARIABLE_VALUE, DONE};
	
	/**
	 * The host Scratch is listening on for remote sensor connections. Defaults to "localhost".
	 */
	private String host = "localhost";
	
	/**
	 * The port Scratch is listening on for remote sensor connections. Defaults to 42001.
	 */
	private int port = 42001;
	
	/**
	 * A list of sensors that registered with this scratch instance to
	 * be notified.
	 */
	private List<RemoteSensor> remoteSensors = new ArrayList<RemoteSensor>();
	
	/**
	 * Creates a Scratch14Instance that uses the Scratch default host and port.
	 */
	public Scratch14Instance() {
	}
	
	/**
	 * Creates a Scratch14Instance that that connects to Scratch
	 * using the specified host and port.
	 * 
	 * @param host - the host Scratch is listening for connections on
	 * @param port - the port Scratch is listening for connections on
	 */
	public Scratch14Instance(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Connects to the remote Scratch instance specified by this instances host and port
	 * and starts receiving messages in a new thread. This method will return immediately
	 * and all registered RemoteSensor instance will be notified asynchronously.
	 */
	public Thread connect() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}

	/**
	 * Connects to the remote Scratch instance specified by this instances host and port
	 * and starts receiving messages in loop. When a message is received the registered
	 * RemoteSensor instances are notified.
	 * 
	 * The scratch messages are parsed according to the protocol specified
	 * here: http://wiki.scratch.mit.edu/wiki/Remote_Sensors_Protocol
	 */
	@Override
	public void run() {
		try (Socket socket = new Socket(host, port);
			 InputStream inputStream = socket.getInputStream())
		{
			LOG.info("Connected to Scratch. Waiting for incoming messages...");
			byte[] sizeBuf = new byte[4];
			while (true) {
				int readCount = inputStream.read(sizeBuf, 0, 4);
				if (readCount != 4) {
					throw new IOException("Expected 4 bytes for message size but got " + readCount +" instead.");
				}
				ByteBuffer bb = ByteBuffer.wrap(sizeBuf);
				int messageSize = bb.getInt();
				byte[] messageBuf = new byte[messageSize];
				readCount = inputStream.read(messageBuf, 0, messageSize);
				if (readCount != messageSize) {
					throw new IOException("Expectes message of size " + messageSize + " bytes but got " + readCount + " instead.");
				}
				String message = new String(messageBuf, "UTF-8");
				notifyRemoteSensors(message);
			}
		} catch (IOException e) {
			LOG.error("Error while communicating to Scratch", e);
			return;
		}
	}
	
	/**
	 * Parses the transmitted message that follows the 4 byte message length indicator
	 * and calls the corresponding methods on the registered sensor instances.
	 * 
	 * @param message
	 */
	private void notifyRemoteSensors(String message) {
		ParserState state = ParserState.IDENTIFY_MESSAGE_TYPE;
		String remainder = message;
		String variableName = null;
		String variableValue = null;
		while (true) {
			switch (state) {
				case IDENTIFY_MESSAGE_TYPE:
					String messageType = remainder.substring(0, message.indexOf(" "));
					remainder = remainder.substring(messageType.length()).trim();
					if (BROADCAST_MESSAGE_TYPE.equals(messageType)) {
						state = ParserState.BROADCAST_MESSAGE;
					} else if (SENSOR_UPDATE_MESSAGE_TYPE.equals(messageType)) {
						state = ParserState.READ_VARIABLE_NAME;
					} else {
						notifyOtherMessage(message);
						state = ParserState.DONE;
					}
					break;
				case BROADCAST_MESSAGE:
					String broadcastMsg;
					if (remainder.startsWith("\"")) {
						broadcastMsg = remainder.substring(1, remainder.indexOf("\"", 1));
					} else {
						// according to the protocol this
						// should not contain any spaces any more...
						broadcastMsg = remainder;
					}
					notifyBroadcastMessage(broadcastMsg);
					state = ParserState.DONE;
					break;
				case READ_VARIABLE_NAME:
					variableName = remainder.substring(1, remainder.indexOf("\"", 1));
					remainder = remainder.substring(remainder.indexOf("\"", 1) + 1).trim();
					state = ParserState.READ_VARIABLE_VALUE;
					break;
				case READ_VARIABLE_VALUE:
					if (remainder.startsWith("\"")) {
						variableValue = remainder.substring(1, remainder.indexOf("\"", 1));
						remainder = remainder.substring(remainder.indexOf("\"", 1) + 1).trim();
					} else {
						variableValue = remainder.substring(0, remainder.indexOf(" "));
						remainder = remainder.substring(remainder.indexOf(" ")).trim();
					}
					notifySensorUpdateMessage(variableName, variableValue);
					if (remainder.length() > 0) {
						state = ParserState.READ_VARIABLE_NAME;
					} else {
						state = ParserState.DONE;
					}
					break;
				case DONE:
					return;
				default:
					throw new IllegalStateException("Unhandled state " + state);
			}
		}
	}
	
	/**
	 * Notifies the registered sensores of unknown messages.
	 * @param message
	 */
	private synchronized void notifyOtherMessage(String message) {
		for (RemoteSensor remoteSensor : remoteSensors) {
			remoteSensor.otherMessage(message);
		}
	}
	
	/**
	 * Notifies the registered sensors of broadcast messages.
	 * @param message
	 */
	private synchronized void notifyBroadcastMessage(String message) {
		for (RemoteSensor remoteSensor : remoteSensors) {
			remoteSensor.broadcast(message);
		}
	}
	
	/**
	 * Updates the registered sensor variable.
	 * 
	 * @param name
	 * @param value
	 */
	private synchronized void notifySensorUpdateMessage(String name, String value) {
		Double doubleValue = null;
		Boolean booleanValue = null;
		String stringValue = null;
		try {
			doubleValue = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			if ("true".equals(value.toLowerCase()) || "false".equals(value.toLowerCase())) {
				booleanValue = Boolean.parseBoolean(value);
			} else {
				stringValue = value;
			}
		}
		for (RemoteSensor remoteSensor : remoteSensors) {
			if (doubleValue != null) {
				remoteSensor.sensorUpdate(name, doubleValue);
			} else if (booleanValue != null) {
				remoteSensor.sensorUpdate(name, booleanValue);
			} else {
				remoteSensor.sensorUpdate(name, stringValue);
			}
		}
	}
	
	/**
	 * Registers a remote sensor that will be notified of messages received from Scratch.
	 * 
	 * @param remoteSensor
	 */
	public synchronized void registerRemoteSensor(RemoteSensor remoteSensor) {
		remoteSensors.add(remoteSensor);
	}
	
	/**
	 * Removes a remote sensor. It will not be notified of
	 * messages received from Scratch any more.
	 * 
	 * @param remoteSensor
	 */
	public synchronized void removeRemoteSensor(RemoteSensor remoteSensor) {
		remoteSensors.remove(remoteSensor);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
