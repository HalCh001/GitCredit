package creditCalculation;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import projectData.Constants;
import projectData.ProjectVariables;


public class LastBillCycle {

	public static void main(String[] args) throws IOException, ParseException 
	{
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sStatementDate = sdf.parse(spConstants.sSt);
		GetDues(sStatementDate);
	}
	
	public static List<Double> GetDues(Date StatementDate) throws IOException, ParseException
	{	
		Date sStatementDate;
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
        sStatementDate=StatementDate;
        Date dtLastBillStatementDate=ProjectVariables.GetLastCycleDate(sStatementDate);
		//System.err.println("# Expecting Last Bill cycle dues Data as on: "+dtLastBillStatementDate);
		
		Double dLastBillCycleOutstanding=0.00,dLastBillCycleMAD=0.00;
		List<Double> dLastBillCycleDues = new ArrayList<Double>();
		
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream FLastCycleDues= new FileInputStream(F1);
		HSSFWorkbook Ex2= new HSSFWorkbook(FLastCycleDues);
		HSSFSheet Sh2= Ex2.getSheetAt(1);
		int cellCount=Sh2.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];
		int Row= Sh2.getPhysicalNumberOfRows();

		for(short iCol=0; iCol<cellCount; iCol++)			
		{	
			Header[iCol]=Sh2.getRow(0).getCell(iCol).getStringCellValue();
			
			if(Header[iCol].contains("Statement Date"))
			{
				for(int iRow=1;iRow<Row;iRow++)					
				  {	
						if(Sh2.getRow(iRow).getCell(iCol).getDateCellValue().equals(dtLastBillStatementDate))
						{
							short sOutStandingColumn=1,sMAD=2;
							dLastBillCycleOutstanding=Sh2.getRow(iRow).getCell(sOutStandingColumn).getNumericCellValue();
							dLastBillCycleMAD=Sh2.getRow(iRow).getCell(sMAD).getNumericCellValue();
							dLastBillCycleDues.add(dLastBillCycleOutstanding);
							dLastBillCycleDues.add(dLastBillCycleMAD);
						}
							
					}
				}
			}
		return dLastBillCycleDues;
	}
}
