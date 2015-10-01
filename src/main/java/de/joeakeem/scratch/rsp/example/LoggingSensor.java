package de.joeakeem.scratch.rsp.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.joeakeem.scratch.rsp.RemoteSensor;

public class LoggingSensor implements RemoteSensor {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoggingSensor.class);

	@Override
	public void broadcast(String message) {
		LOG.info("BROADCAST: '{}'", message);
	}

	@Override
	public void sensorUpdate(String name, String value) {
		LOG.info("SENSOR UPDATE: '{}'='{}'", name, value);
	}

	@Override
	public void sensorUpdate(String name, double value) {
		LOG.info("SENSOR UPDATE: '{}'={}", name, value);
	}

	@Override
	public void sensorUpdate(String name, boolean value) {
		LOG.info("SENSOR UPDATE: '{}'={}", name, value);
	}

	@Override
	public void otherMessage(String message) {
		LOG.info("OTHER MESSAGE: '{}'", message);
	}

}
