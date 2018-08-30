package org.scenario;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.scenario.Utils.DetailedCloudletsTableBuilder;
import org.scenario.Utils.Utils;
import org.scenario.autoadaptive.ReplicaCatalog;
import org.scenario.cloudsimplus.AdaptedCloudlet;
import org.scenario.config.InitializeReplicationScenarioBasicTreeTopology;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;





public class RunReplicationScenario {
	
    
    public void run() throws RowsExceededException, WriteException, IOException, BiffException{
        
        List<DatacenterBroker> brokers = new InitializeReplicationScenarioBasicTreeTopology().init(); 
        Utils.writeInAGivenFile("Log",  "" , false);
        brokers.get(0).getSimulation().start();
//        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out))); // make out go back to default
//        try {
//			System.setOut(new PrintStream(new FileOutputStream("1000req_1MB_Fair")));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        List<Cloudlet> finished = brokers.get(0).getCloudletFinishedList();
//        finished.sort((c1,c2) -> (c1.getVm().getId() < c2.getVm().getId()) ? 1 : -1);
//        
        DetailedCloudletsTableBuilder results = new DetailedCloudletsTableBuilder(finished);
        		results.build();
        		
        		Workbook workbook = Workbook.getWorkbook(new File("CloudletTime.xls"));

        	    //MODIFY XLS

        	    WritableWorkbook copy = Workbook.createWorkbook(new File("CloudletTime (copy).xls"), workbook);
        	    WritableSheet sheet = copy.getSheet(0); 
        	    int currentRow = 9;
        	    WritableCellFormat format = new WritableCellFormat();
        	    format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        	    format.setBackground(Colour.WHITE);
        	    WritableCellFormat format1 = new WritableCellFormat();
        	    format1.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        	    format1.setBackground(Colour.LIGHT_ORANGE);
        	    WritableCellFormat format2 = new WritableCellFormat();
        	    format2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        	    format2.setBackground(Colour.GRAY_25);
        	    WritableCellFormat format3 = new WritableCellFormat();
        	    format3.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        	    format3.setBackground(Colour.LIGHT_GREEN);
        		for(Cloudlet cl : finished) {
        			WritableCell cell0 = new Number(0, currentRow, cl.getId()); cell0.setCellFormat(format1); sheet.addCell(cell0);
        			WritableCell cell00 = new Number(1, currentRow, ((AdaptedCloudlet) cl).getSendTime()); cell00.setCellFormat(format1); sheet.addCell(cell00);
        			
        			WritableCell cell01 = new Number(3, currentRow, ((AdaptedCloudlet) cl).getDcReceiveTime()); cell01.setCellFormat(format); sheet.addCell(cell01);
        			WritableCell cell02 = new Number(4, currentRow, 0); cell02.setCellFormat(format); sheet.addCell(cell02);
        			WritableCell cell03 = new Number(6, currentRow, ((AdaptedCloudlet) cl).getVmReceiveTime()); cell03.setCellFormat(format); sheet.addCell(cell03);
        			WritableCell cell04 = new Number(7, currentRow, ((AdaptedCloudlet) cl).getExecStartTime()); cell04.setCellFormat(format); sheet.addCell(cell04);
        			WritableCell cell05 = new Number(9, currentRow, ((AdaptedCloudlet) cl).getFileRetrievalTime()); cell05.setCellFormat(format); sheet.addCell(cell05);
        			WritableCell cell06 = new Number(10, currentRow, ((AdaptedCloudlet) cl).getFinishTime()); cell06.setCellFormat(format); sheet.addCell(cell06);
        			WritableCell cell07 = new Number(11, currentRow, ((AdaptedCloudlet) cl).getLeftVmToBrokerTime()); cell07.setCellFormat(format); sheet.addCell(cell07);
        			
        			WritableCell cell08 = new Number(12, currentRow, ((AdaptedCloudlet) cl).getUplinkTime()); cell08.setCellFormat(format3); sheet.addCell(cell08);
        			
        			WritableCell cell09 = new Number(13, currentRow, ((AdaptedCloudlet) cl).getLeftDcToBrokerTime()); cell09.setCellFormat(format); sheet.addCell(cell09);
        			
        			WritableCell cell10 = new Number(15, currentRow, ((AdaptedCloudlet) cl).getGotToBrokerTime()); cell10.setCellFormat(format1); sheet.addCell(cell10);
        			
        			WritableCell cell11 = new Number(16, currentRow, ((AdaptedCloudlet) cl).getActualCpuTime()); cell11.setCellFormat(format2); sheet.addCell(cell11);
        			WritableCell cell12 = new Number(17, currentRow, ((AdaptedCloudlet) cl).getOverallTime()); cell12.setCellFormat(format2); sheet.addCell(cell12);
        			currentRow++;
        			
        		}

        	    copy.write();
        	    copy.close();
        	    
        double workloadTime = ((AdaptedCloudlet)finished.get(finished.size()-1)).getLeftDcToBrokerTime() - ((AdaptedCloudlet)finished.stream().findFirst().get()).getDcReceiveTime() ;   
        long totalData = 0 ;
        double overallAvg = 0;
        double variance = 0;
        double avgRemontee = 0;
        
        for(Cloudlet cl : finished)
        	totalData += ReplicaCatalog.getCatalogInstance().getFileMetadataWithId(((AdaptedCloudlet) cl).getRequestedFileId()).getFileSize() ;
        
        for(Cloudlet cl : finished)
        	overallAvg += ((AdaptedCloudlet) cl).getOverallTime();
        overallAvg /= finished.size();
        
        for(Cloudlet cl : finished)
        	variance += Math.pow((((AdaptedCloudlet) cl).getOverallTime() - overallAvg), 2);
        variance /= finished.size();
        
        for(Cloudlet cl : finished)
        	avgRemontee += ((AdaptedCloudlet)cl).getLeftDcToBrokerTime(1)-((AdaptedCloudlet)cl).getLeftVmToBrokerTime(1);
        avgRemontee /= finished.size();
        System.out.println( (float)workloadTime);
        System.out.println((float)(overallAvg));
        System.out.println((float)(finished.size() / workloadTime)); //debit
        System.out.println( (float)(totalData /workloadTime)); // BW
        System.out.println((float)(avgRemontee));
        String theString = 	"" + (float)workloadTime + "\n" +
    						"" + (float)overallAvg + "\n" +
    						"" + (float)(finished.size() / workloadTime) + "\n" +
    						"" + (float)(totalData /workloadTime) + "\n" +
    						"" + (float)(avgRemontee) + "\n" ;
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        System.out.println( (float)(variance));
    }
    
}
