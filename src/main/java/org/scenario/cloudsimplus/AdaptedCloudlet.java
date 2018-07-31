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
	 * @see #getExecutionStartTime()
	 */
	private double executionStartTime;
	
	/**
	 * @see #getFileRetrievalTime()
	 */
	private double fileRetrievalTime;

	/**
	 * @see #getReturnTime()
	 */
	private double returnTime;
	
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

	public double getReturnTime() {
		return returnTime;
	}


	public void setReturnTime(double returnTime) {
		this.returnTime = returnTime;
	}

	
	public int getRequestedFileId() {
		return requestedFileId;
	}
	
	
	public void setRequestedFileId(int requestedFileId) {
		this.requestedFileId = requestedFileId;
	}


	public double getExecutionStartTime() {
		return executionStartTime;
	}


	public void setExecutionStartTime(double executionStartTime) {
		this.executionStartTime = executionStartTime;
	}
	
	
	

}
