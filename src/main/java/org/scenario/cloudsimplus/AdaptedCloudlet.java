package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;

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
	 * @see #getFileRetrievalTime()
	 */
	private double fileRetrievalTime;

	/**
	 * @see #getLeftVmToBrokerTime()
	 */
	private double leftVmToBrokerTime;
	
	/**
	 * @see #getLeftDcToBrokerTime()
	 */
	private double leftDcToBrokerTime;
	
	/**
	 * @see #getGotToBrokerTime()
	 */
	private double gotToBrokerTime;
	
	
	/**
	 * @see #getRequestedFileId()
	 */
	private int requestedFileId;
	

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
	
	
	public double getFileRetrievalTime() {
		return fileRetrievalTime;
	}
	
	
	public void setFileRetrievalTime(double fileRetrievalTime) {
		this.fileRetrievalTime = fileRetrievalTime;
	}

	public double getLeftVmToBrokerTime() {
		return leftVmToBrokerTime;
	}


	public void setLeftVmToBrokerTime(double leftVmToBrockerTime) {
		this.leftVmToBrokerTime = leftVmToBrockerTime;
	}


	public double getLeftDcToBrokerTime() {
		return leftDcToBrokerTime;
	}


	public void setLeftDcToBrokerTime(double leftDcToBrokerTime) {
		this.leftDcToBrokerTime = leftDcToBrokerTime;
	}


	public double getGotToBrokerTime() {
		return gotToBrokerTime;
	}


	public void setGotToBrokerTime(double gotToBrokerTime) {
		this.gotToBrokerTime = gotToBrokerTime;
	}	
	

	public int getRequestedFileId() {
		return requestedFileId;
	}
	
	
	public void setRequestedFileId(int requestedFileId) {
		this.requestedFileId = requestedFileId;
	}
}
