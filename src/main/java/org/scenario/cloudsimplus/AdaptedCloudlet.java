package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.network.NetworkPacket;

public class AdaptedCloudlet extends NetworkCloudlet{
	
	/**
	 * @see #getSendTime()
	 */
	private double sendTime;
	
	/**
	 * @see #getDcReceiveTime()
	 */
	private double dcReceiveTime;
	
	/**
	 * @see #getVmReceiveTime()
	 */
	private double vmReceiveTime;
	
	/**
	 * @see #getReturnTime()
	 */
	private double returnTime;
	
	
//	private requestedFile
	
	/**
	 * @param id
	 * @param cloudletLength
	 * @param pesNumber
	 */
	public AdaptedCloudlet(int id, long cloudletLength, int pesNumber) {
		super(id, cloudletLength, pesNumber);
		// TODO Auto-generated constructor stub
	}


	public double getSendTime() {
		return sendTime;
	}


	public void setSendTime(double sendTime) {
		this.sendTime = sendTime;
	}

	
	public double getDcReceiveTime() {
		return dcReceiveTime;
	}


	public void setDcReceiveTime(double dcReceiveTime) {
		this.dcReceiveTime = dcReceiveTime;
	}


	public double getVmReceiveTime() {
		return vmReceiveTime;
	}


	public void setVmReceiveTime(double VmReceiveTime) {
		this.vmReceiveTime = VmReceiveTime;
	}


	public double getReturnTime() {
		return returnTime;
	}


	public void setReturnTime(double returnTime) {
		this.returnTime = returnTime;
	}

	
	

	

}
