package creditCalculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import projectData.Constants;
import projectData.ProjectVariables;


public class CreditCardData 
{
	public static void main(String[] args) throws IOException, ParseException
	
	{		
		
	}
		             
	public static Boolean CheckAndUpdateRowForLastMonthOutstanding(List<Double> Amount) throws IOException, ParseException
	{
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis= new FileInputStream(F1);
		HSSFWorkbook Ex1= new HSSFWorkbook(Fis);
		HSSFSheet Sh1= Ex1.getSheetAt(0);
				
		short iRemarks=4;	
		int Row= Sh1.getPhysicalNumberOfRows();
		
		try
		 {
			if(Sh1.getRow(Row-1).getCell(iRemarks).getStringCellValue().contains("Outstanding Details updated for Last Bill Cycle Dated:" +spConstants.sSt))		
			  System.out.println("*** Outstanding Details alerady updated for Bill Cycle Dated:" +spConstants.sSt);
			else
			  UpdateFirstSheet(Amount);
		 }
		catch(Exception e)
		 {				
			UpdateFirstSheet(Amount);
		 }	
		return true;
	 }

	public static Date GetLastBillStatementDate(Date sPaymentDate1)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sPaymentDate1);
	    calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
		
	}
		
	public static Boolean StoreCreditCardDataInExcel(List<Double> Amount) throws IOException, ParseException

		{
			Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
			File F1= new File(spConstants.sExcelLocation);
			FileInputStream Fis2= new FileInputStream(F1);
			HSSFWorkbook Ex2= new HSSFWorkbook(Fis2);
			HSSFSheet Sh2= Ex2.getSheetAt(1);
			short iRemarks=3;	
			int Row= Sh2.getPhysicalNumberOfRows();
			
			try
			{
				if(Sh2.getRow(Row-1).getCell(iRemarks).getStringCellValue().contains("Outstanding Details updated for Last Bill Cycle Dated:" +spConstants.sSt))
				{	
					System.out.println("***Outstanding Details alerady updated for Bill Cycle Dated:" +spConstants.sSt);
					return true;
				}
				
				else
				{
					UpdateSecondSheet(Amount);
				}
			}
			
			catch(Exception e)
			{
				UpdateSecondSheet(Amount);	
			}
				
			return true;
		}	
	
    public static void UpdateFirstSheet(List<Double> Amount) throws IOException, ParseException
    {
		Double dOutstanding=Amount.get(0);
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sStatementDate = sdf.parse(spConstants.sSt);
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis= new FileInputStream(F1);
		HSSFWorkbook Ex1= new HSSFWorkbook(Fis);
		HSSFSheet Sh1= Ex1.getSheetAt(0);
				
		int cellCount=Sh1.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];
		int Row= Sh1.getPhysicalNumberOfRows();
		
		for(short iCol=0; iCol<cellCount; iCol++)				
		{	
			Header[iCol]=Sh1.getRow(0).getCell(iCol).getStringCellValue();							
			{
				if (Header[iCol].contains("Payment Type"))
				{				
					Sh1.createRow(Row).createCell(iCol).setCellValue("LastBillCycleOustanding");
				}					
				else if (Header[iCol].contains("Transaction Amount"))
				{
					Sh1.createRow(Row).createCell(iCol).setCellValue(dOutstanding);
				}									
				else if (Header[iCol].contains("Payment Date"))
				{						
					HSSFCellStyle cellStyle= Ex1.createCellStyle();
					cellStyle.setDataFormat((short)14);
					//Sh1.createRow(Row).createCell(iCol).setCellStyle(cellStyle);		
					//createHelper.createDataFormat().getFormat("dd-mmm-yy")); 

					//2. Apply the Date cell style to a cell

					//This example sets the first cell in the row using the date cell style
					//cell = row.createCell(0);
					//cell.setCellValue(new Date());
					//cell.setCellStyle(cellStyle);
												
					Sh1.createRow(Row).createCell(iCol).setCellValue(sStatementDate);
					Sh1.getRow(Row).getCell(iCol).setCellStyle(cellStyle);	
				}						
				 else if (Header[iCol].contains("EMI"))
				{				
					Sh1.createRow(Row).createCell(iCol).setCellValue(0.00);
				}					
				 else if (Header[iCol].contains("Remarks"))
					{				
					 	Sh1.createRow(Row).createCell(iCol).setCellValue("Outstanding Details updated for Bill Cycle Dated:" +spConstants.sSt);
						System.out.println(" Outstanding Details updated for Bill Cycle Dated:" +spConstants.sPmnt);
						System.out.println("");
					}
				 else if (Header[iCol].contains("LastCycleOutstandingCleared"))
				 {
					 if(Outstanding.LastBillOutstandingCleared())
					 {
						 Sh1.createRow(Row).createCell(iCol).setCellValue("Yes");
					 }
					 else
					 {
						 Sh1.createRow(Row).createCell(iCol).setCellValue("No");
					 }
						 
				 }
			}			
		}
		FileOutputStream fos=new FileOutputStream(new File(spConstants.sExcelLocation));
		Ex1.write(fos);
		fos.close();
    }

    public static void UpdateSecondSheet(List<Double> Amount) throws IOException, ParseException
    {
    	Double MAD= Amount.get(1);
		Double dOutstanding=Amount.get(0);
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sStatementDate = sdf.parse(spConstants.sSt);
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis2= new FileInputStream(F1);
		HSSFWorkbook Ex2= new HSSFWorkbook(Fis2);
		HSSFSheet Sh2= Ex2.getSheetAt(1);
		int cellCount=Sh2.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];
			
		int Row= Sh2.getPhysicalNumberOfRows();
		
    	for(short iCol=0; iCol<cellCount; iCol++)
			
		{	
			Header[iCol]=Sh2.getRow(0).getCell(iCol).getStringCellValue();
			
			if (Header[iCol].contains("Statement Date"))
			{
				HSSFCellStyle cellStyle= Ex2.createCellStyle();
				cellStyle.setDataFormat((short)14);							
				Sh2.createRow(Row).createCell(iCol).setCellValue(sStatementDate);
				Sh2.getRow(Row).getCell(iCol).setCellStyle(cellStyle);	
			}
								
			else if (Header[iCol].contains("Outstanding Amount"))
			{
				Sh2.createRow(Row).createCell(iCol).setCellValue(dOutstanding);
			}
			
			else if (Header[iCol].contains("Current MAD Amount"))
			{
				Sh2.createRow(Row).createCell(iCol).setCellValue(MAD);
				System.out.println("$$$ Minimum Amount Due Rs. " +MAD+ " for Bill Statement Dated: " +spConstants.sSt+ " Cascaded in Next Bill Cycle as OutStanding Amount NOT Paid as of "+spConstants.sPmnt);
				
			}
			
			else if (Header[iCol].contains("Remarks"))
			{				
				Sh2.createRow(Row).createCell(iCol).setCellValue("Outstanding Details updated Last Bill Cycle Dated:" +spConstants.sSt);
				System.out.println("$$$ Outstanding Details updated for Bill Cycle Dated:" +spConstants.sPmnt);
				System.out.println("");
			}
			
			  
			FileOutputStream fos=new FileOutputStream(new File(spConstants.sExcelLocation));
			Ex2.write(fos);
			fos.close();	
			 
			}
		;
    	
    }  
    
    public static List<Double> GetCompleteDetails() throws IOException, ParseException
	{
		Double InterestAndServiceTax=0.00,MAD=0.00;
		List<Double> CreditCardDetails = new ArrayList<Double>();
		System.out.println("*                    CREDIT CARD STATEMENT                    *");
		System.out.println("**************************************************************");
		
		Double PrincipleOutstanding=Outstanding.GetTotalOutStanding();
		
		System.out.println("*** Total Outstanding before interest & tax: "+PrincipleOutstanding);	
		System.out.println(" ");
		Double dTotalInterestAndServiceTax = Interest.CalculateInterestAndServiceTax(PrincipleOutstanding);
		Double LateFee=LatePaymentFee.GetLateFee(PrincipleOutstanding);
		PrincipleOutstanding=PrincipleOutstanding+dTotalInterestAndServiceTax+LateFee;
		if (LateFee>0)
			MAD= MinimumAmountDue.GetMinimumAmountDue(PrincipleOutstanding,false);
		else
			MAD= MinimumAmountDue.GetMinimumAmountDue(PrincipleOutstanding,true);
		
		
		System.err.println("*** Total InterestAndServiceTax: "+dTotalInterestAndServiceTax);
		System.err.println("*** Total Outstanding After interest & tax: "+PrincipleOutstanding);
		System.err.println("*** Minimum Amount Due " +MAD);
		System.err.println("*** Late Fine Amount Due " +LateFee);
		System.out.println();
		System.out.println("*************END OF STATEMENT*********************************");
		System.out.println(" ");
		
		CreditCardDetails.add(0, PrincipleOutstanding);
		CreditCardDetails.add(1, MAD);
		CreditCardDetails.add(2, InterestAndServiceTax);
		CreditCardDetails.add(3, LateFee);
		
		
		return CreditCardDetails;
	}
}
	


    
