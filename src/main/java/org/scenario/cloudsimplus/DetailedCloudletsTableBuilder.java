package org.scenario.cloudsimplus;

import java.util.List;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Identifiable;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;

public class DetailedCloudletsTableBuilder extends CloudletsTableBuilder{

    private static final String TIME_FORMAT = "%d";
    private static final String SECONDS = "(sec)";
    private static final String CPU_CORES = "CPU cores";
    private static final String MEGABYTE = "MB";

	public DetailedCloudletsTableBuilder(List<? extends Cloudlet> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	 @Override
	    protected void createTableColumns() {
	        final String ID = "ID";
	        addColumnDataFunction(getTable().addColumn("Cloudlet", ID), Identifiable::getId);
	        addColumnDataFunction(getTable().addColumn("Status "), c -> c.getStatus().name());
	        addColumnDataFunction(getTable().addColumn("DC", ID), c -> c.getVm().getHost().getDatacenter().getId());
	        addColumnDataFunction(getTable().addColumn("Host", ID), c -> c.getVm().getHost().getId());
	        addColumnDataFunction(getTable().addColumn("Host PEs ", CPU_CORES), c -> c.getVm().getHost().getNumberOfWorkingPes());
	        addColumnDataFunction(getTable().addColumn("VM", ID), c -> c.getVm().getId());
	        addColumnDataFunction(getTable().addColumn("VM PEs   ", CPU_CORES), c -> c.getVm().getNumberOfPes());
	        addColumnDataFunction(getTable().addColumn("CloudletLen", "MI"), Cloudlet::getLength);
	        addColumnDataFunction(getTable().addColumn("CloudletPEs", CPU_CORES), Cloudlet::getNumberOfPes);
	        addColumnDataFunction(getTable().addColumn("RequestedFileSize", MEGABYTE), c -> ((AdaptedCloudlet)c).getRequestedFile().getSize());
	        addColumnDataFunction(getTable().addColumn("SendTime", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getSendTime()));
	        addColumnDataFunction(getTable().addColumn("ReceivedByDC", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getDcReceiveTime()));
	        addColumnDataFunction(getTable().addColumn("ReceivedByVM", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getVmReceiveTime()));
	        addColumnDataFunction(getTable().addColumn("StartTime", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getExecStartTime()));
	        addColumnDataFunction(getTable().addColumn("RetreivedFile", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getFileRetrievalTime()));
	        addColumnDataFunction(getTable().addColumn("FinishTime", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getFinishTime()));
	        addColumnDataFunction(getTable().addColumn("LeftVm", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getLeftVmToBrokerTime()));
	        addColumnDataFunction(getTable().addColumn("LeftDc", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getLeftDcToBrokerTime()));
	        addColumnDataFunction(getTable().addColumn("RequestReturn", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getGotToBrokerTime()));
	        addColumnDataFunction(getTable().addColumn("ActualCpuTime", SECONDS), c -> Double.toString(((AdaptedCloudlet)c).getActualCpuTime()));
	        addColumnDataFunction(getTable().addColumn("OverallTime", SECONDS), c -> Double.toString( ((AdaptedCloudlet)c).getGotToBrokerTime()-((AdaptedCloudlet)c).getSendTime() ));
	    }
	 
}
