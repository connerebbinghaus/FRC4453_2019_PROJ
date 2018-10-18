package org.usfirst.frc.team4453.robot.library;

import java.util.concurrent.atomic.AtomicLong;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    static final String	HOSTNAME = "raspberrypi.local";

    AtomicLong		angle	 = new AtomicLong(Double.doubleToRawLongBits(Double.NaN));
    AtomicLong		distance = new AtomicLong(Double.doubleToRawLongBits(Double.NaN));

    public Vision() {
	SmartDashboard.putString("MQTT Status:", "DISCONNECTED");

	MQTT mqtt = new MQTT();
	CallbackConnection connection = mqtt.callbackConnection();
	connection.listener(new Listener() {

	    // @Override
	    public void onConnected() {
		SmartDashboard.putString("MQTT Status:", "CONNECTED");
	    }

	    // @Override
	    public void onDisconnected() {
		SmartDashboard.putString("MQTT Status:", "DISCONNECTED");
	    }

	    // @Override
	    public void onFailure(Throwable e) {
		DriverStation.reportError("MQTT: " + e.getMessage(), e.getStackTrace());
	    }

	    // @Override
	    public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
		if (topic.toString().equals("angle")) {
		    angle.set(Double.doubleToRawLongBits(Double.parseDouble(payload.toString())));
		}
		else if (topic.toString().equals("distance")) {
		    distance.set(Double.doubleToRawLongBits(Double.parseDouble(payload.toString())));
		}
	    }
	});

	connection.connect(new Callback<Void>() {

	    // @Override
	    public void onFailure(Throwable e) {
		SmartDashboard.putString("MQTT Status:", "DISCONNECTED");
		DriverStation.reportError("MQTT: " + e.getMessage(), e.getStackTrace());
	    }

	    // @Override
	    public void onSuccess(Void arg0) {
		Topic[] topics = { new Topic("angle", QoS.AT_LEAST_ONCE), new Topic("distance", QoS.AT_LEAST_ONCE) };
		connection.subscribe(topics, new Callback<byte[]>() {

		    // @Override
		    public void onFailure(Throwable e) {
			DriverStation.reportError("MQTT: " + e.getMessage(), e.getStackTrace());
		    }

		    // @Override
		    public void onSuccess(byte[] arg0) {
		    }

		});
	    }

	});
    }

    public double getDistance() {
	return Double.longBitsToDouble(distance.get());
    }

    public double getAngle() {
	return Double.longBitsToDouble(angle.get());
    }
}
