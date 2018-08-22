package org.scenario.cloudsimplus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.resources.File;

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
		return BigDecimal.valueOf(sendTime).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}


	public void setSendTime(double sendTime) {
		this.sendTime = sendTime;
	}

	
	public double getDcReceiveTime() {
		return BigDecimal.valueOf(dcReceiveTime).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}


	public void setDcReceiveTime(double dcReceiveTime) {
		this.dcReceiveTime = dcReceiveTime;
	}


	public double getVmReceiveTime() {
		return BigDecimal.valueOf(vmReceiveTime).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}


	public void setVmReceiveTime(double VmReceiveTime) {
		this.vmReceiveTime = VmReceiveTime;
	}
	
	
	public double getFileRetrievalTime() {
		return BigDecimal.valueOf(fileRetrievalTime).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	
	public void setFileRetrievalTime(double fileRetrievalTime) {
		this.fileRetrievalTime = fileRetrievalTime;
	}

	public double getLeftVmToBrokerTime() {
		return BigDecimal.valueOf(leftVmToBrokerTime).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	public double getLeftVmToBrokerTime(int flag) {
		return BigDecimal.valueOf(leftVmToBrokerTime).setScale(9, RoundingMode.HALF_UP).doubleValue();
	}


	public void setLeftVmToBrokerTime(double leftVmToBrockerTime) {
		this.leftVmToBrokerTime = leftVmToBrockerTime;
	}

	public double getUplinkTime() {
		return BigDecimal.valueOf(this.getLeftDcToBrokerTime(1)-this.getLeftVmToBrokerTime(1)).setScale(18, RoundingMode.HALF_UP).doubleValue();
	}

	public double getLeftDcToBrokerTime() {
		return BigDecimal.valueOf(leftDcToBrokerTime).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	public double getLeftDcToBrokerTime(int flag) {
		return BigDecimal.valueOf(leftDcToBrokerTime).setScale(9, RoundingMode.HALF_UP).doubleValue();
	}


	public void setLeftDcToBrokerTime(double leftDcToBrokerTime) {
		this.leftDcToBrokerTime = leftDcToBrokerTime;
	}


	public double getGotToBrokerTime() {
		return BigDecimal.valueOf(gotToBrokerTime).setScale(3, RoundingMode.HALF_UP).doubleValue();
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
	
	@Override
	public double getExecStartTime(){
		return BigDecimal.valueOf(super.getExecStartTime()).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	@Override
	public double getFinishTime(){
		return BigDecimal.valueOf(super.getFinishTime()).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	@Override
	public double getActualCpuTime(){
		return BigDecimal.valueOf(super.getActualCpuTime()).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	public double getOverallTime() {
		return BigDecimal.valueOf(this.getGotToBrokerTime()).subtract(BigDecimal.valueOf(this.getSendTime())).setScale(3, RoundingMode.HALF_UP).doubleValue(); 
	}
	public File getRequestedFile(){
		String fileName = this.getRequiredFiles().get(0);
		return ((AdaptedDatacenterStorage)this.getLastDatacenter().getDatacenterStorage()).getFile(fileName);
	}
}
