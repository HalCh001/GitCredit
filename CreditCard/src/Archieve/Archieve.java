package Archieve;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Archieve {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
	
	/*public static Double GetLastBillCycleMinimumAmountDueFromExcel() throws IOException, ParseException
	{
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sPaymentDate = sdf.parse(spConstants.sPmnt);
        Date sStatementDate = sdf.parse(spConstants.sSt);
        Double LastBillCycleMinimumAmountDue=0.00;
        
		File F1= new File("C:\\Users\\GamezzOn\\Desktop\\Credit card Statement.xls");
		FileInputStream Fis= new FileInputStream(F1);
		HSSFWorkbook Ex1= new HSSFWorkbook(Fis);
		HSSFSheet Sh1= Ex1.getSheetAt(0);
				
		int cellCount=Sh1.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];
		int Row= Sh1.getPhysicalNumberOfRows();
		short MADColoumIndex=4;
		
		for(int iTrans=0;iTrans<Row;iTrans++)
		{
			for(short iCol=0; iCol<cellCount;iCol++)
			{
				Header[iCol]=Sh1.getRow(0).getCell(iCol).getStringCellValue();
				if (Header[iCol].contains("Statement Date"))
					{
						System.out.println("^^^ "+ProjectVariables.GetLastCycleDate(sStatementDate));
						if(Sh1.getRow(iTrans).getCell(iCol).getDateCellValue()==ProjectVariables.GetLastCycleDate(sStatementDate))
							{
								LastBillCycleMinimumAmountDue = Sh1.getRow(iTrans).getCell(MADColoumIndex).getNumericCellValue();
								System.out.println("Last BillCycle MinimumAmount Due FromExcel is:"+LastBillCycleMinimumAmountDue);
							}
				
					}
			}
		
		

	}
		return LastBillCycleMinimumAmountDue;
		
	}*/
	
	
}
	


