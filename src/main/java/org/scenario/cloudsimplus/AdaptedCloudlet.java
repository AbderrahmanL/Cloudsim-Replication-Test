package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.network.NetworkPacket;

public class AdaptedCloudlet extends NetworkCloudlet implements NetworkPacket<SimEntity>{

	/**
	 * @see #getSource()
	 */
	private SimEntity src;
	
	/**
	 * @see #getDestination()
	 */
	private SimEntity dest;
	
	/**
	 * @see #getSendTime()
	 */
	private double sendTime;
	
	/**
	 * @see #getReceiveTime()
	 */
	private double receiveTime;
	
	/**
	 * @param id
	 * @param cloudletLength
	 * @param pesNumber
	 */
	public AdaptedCloudlet(int id, long cloudletLength, int pesNumber) {
		super(id, cloudletLength, pesNumber);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return input size of the cloudlet plus the tcp minimum packet size
	 */
	@Override
	public long getSize() {
		return this.getFileSize() + 1500;
	}

	@Override
	public SimEntity getSource() {
		return src;
	}

	@Override
	public void setSource(SimEntity source) {
		this.src = source;
		
	}

	@Override
	public SimEntity getDestination() {
		return dest;
	}

	@Override
	public void setDestination(SimEntity destination) {
		this.dest = destination;
	}

	@Override
	public double getSendTime() {
		return sendTime;
	}

	@Override
	public void setSendTime(double time) {
		this.sendTime = time;
	}

	@Override
	public double getReceiveTime() {
		return receiveTime;
	}

	@Override
	public void setReceiveTime(double time) {
		this.receiveTime = time;
	}

	

}
