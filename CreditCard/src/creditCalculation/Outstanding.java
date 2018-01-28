package creditCalculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import projectData.Constants;
import projectData.ProjectVariables;

public class Outstanding 
{
	
	public static void main(String args[]) throws IOException
	{
		DateOfLastBillCycleOutstandingCleared();
	}
			
	public static Double GetTotalOutStanding() throws IOException, ParseException
	{
		Double dTotalOutstanding=0.00;
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		ArrayList<CreditData> CD= new ArrayList<CreditData>();	
		CD= TransactionAmount.GetTransactionAmount();
		Collections.reverse(CD);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sStatementDate = sdf.parse(spConstants.sSt);
        Date dtLastBillStatementDate=ProjectVariables.GetLastCycleDate(sStatementDate);

    
		for(int iTrans=0; iTrans<CD.size();iTrans++)
		{   			
			if(CD.get(iTrans).PaymentDt.before(sStatementDate) && !CD.get(iTrans).PaymentDt.before(dtLastBillStatementDate) )
				{
					if(CD.get(iTrans).EMI==null)
					{
						CD.get(iTrans).EMI=0.00;
					}
					
					if(!(CD.get(iTrans).EMI>0.00))
						{
							if(CD.get(iTrans).sPayType.contains("BankCreditToCustomer") ||CD.get(iTrans).sPayType.contains("LastBillCycleOustanding"))
								{
								dTotalOutstanding=dTotalOutstanding+CD.get(iTrans).TrnsAmount;
								}
							else if (CD.get(iTrans).sPayType.contains("CustomerPaymentToBank"))
								{
								dTotalOutstanding=dTotalOutstanding-CD.get(iTrans).TrnsAmount;
								}
						}
					else
						{
							dTotalOutstanding=dTotalOutstanding+CD.get(iTrans).EMI;
						}
				}
	}
		
			//dLastBillCycleDues= LastBillCycle.GetDues();
			//dTotalOutstanding=dTotalOutstanding+ dLastBillCycleDues.get(0);
			System.out.println(" ");

	return dTotalOutstanding;
	}
	
	public static Date DateOfLastBillCycleOutstandingCleared() throws IOException
	{
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		Date ClearDate= null; 
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis= new FileInputStream(F1);
		HSSFWorkbook Ex1= new HSSFWorkbook(Fis);
		HSSFSheet Sh1= Ex1.getSheetAt(0);
				
		int cellCount=Sh1.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];
		int Row= Sh1.getPhysicalNumberOfRows();
				
		for (int iTrans=Row-1;iTrans>0;iTrans--)
		{
			for(short iCol=0;iCol<cellCount;iCol++ )
			{
				Header[iCol]=Sh1.getRow(0).getCell(iCol).getStringCellValue();
				if(Header[iCol].contains("LastCycleOutstandingCleared") && Sh1.getRow(iTrans).getCell((short)0).getStringCellValue().contains("LastBillCycleOustanding"))
				{
					try
					{	
						if(Sh1.getRow(iTrans).getCell(iCol).getStringCellValue().contains("Yes"))
						{
							ClearDate= Sh1.getRow(iTrans).getCell((short) 2).getDateCellValue();
							System.out.println("*** Customer XXX had Zero Outstanding as in Bill cycle dated: "+ClearDate);							
							System.out.println("");
							List<Double> remainingOutstanding = LastBillCycle.GetDues(ClearDate);  //Changed since GetDues() fetches dues w.r.t last cycle date of the date given as input.
							
							Date LastBillCycleDate= ProjectVariables.GetLastCycleDate(ClearDate);
							System.out.println("~~~Outstanding of BillCycle Prior to the Zero Outstanding BillCycle: "+remainingOutstanding.get(0));
							System.out.println("~~~MAD of BillCycle Prior to the Zero Outstanding BillCycle: "+remainingOutstanding.get(1));
							System.out.println("");
							return LastBillCycleDate;
						}										
					}
										
					catch(Exception e)
					 {
					    continue;
					 }					
			   }
			}
			
		}
		return (Sh1.getRow(1).getCell((short) 2).getDateCellValue());
		
	}
	
	public static Boolean LastBillOutstandingCleared() throws IOException, ParseException
	{
		Double dAmountPaidByCustomer=0.00;
		ArrayList<CreditData> CD= new ArrayList<CreditData>();	
		CD= creditCalculation.TransactionAmount.GetTransactionAmount();
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sPaymentDate = sdf.parse(spConstants.sPmnt);
        Date sStatementDate = sdf.parse(spConstants.sSt);
        Date dtLastBillStatementDate=ProjectVariables.GetLastCycleDate(sStatementDate);
        List<Double> LastCycleDue=LastBillCycle.GetDues(sStatementDate);
        

		for(int iTrans=0; iTrans<CD.size();iTrans++)
		{     
			if(!CD.get(iTrans).PaymentDt.after(sPaymentDate) && !CD.get(iTrans).PaymentDt.before(dtLastBillStatementDate))  //slight Doubt
				{
					if (CD.get(iTrans).sPayType.contains("CustomerPaymentToBank"))
						{
							dAmountPaidByCustomer=dAmountPaidByCustomer+CD.get(iTrans).TrnsAmount;
						}
				}
		}
		System.out.println("%%% Amount Paid By customer from: "+dtLastBillStatementDate+ " To: "+sPaymentDate+ " is: Rs. "+dAmountPaidByCustomer);
       
       if (dAmountPaidByCustomer>=LastCycleDue.get(0))
       {
    	   return true;
       }
       else
       {    	   
    	   return false;
       }       		
	}
		
	//public static int getExactDate()
	{
		/*for (int iSelect=lastZeroOutDateRow+1;iSelect<iTrans;iSelect++)
		{
			System.out.println("1st Checkpoint"+lastZeroOutDateRow);
		
			for(short iColumn=0;iColumn<cellCount;iColumn++ )
			{
				System.out.println("Check this " +Sh1.getRow(iSelect).getCell((short)0).getStringCellValue());
				//remainingOutstanding= LastBillCycle.GetDues(FinalDate); //here I need to pass FinalDate & and filter out the exact row, where customer paid last cycle dues in total. return the row index/date. recursive interest will be charged from thereon.
				remainingOutstanding=10000.00;
				if (Sh1.getRow(iSelect).getCell((short)0).getStringCellValue().contains("CustomerPaymentToBank"))
				{
					System.out.println("Need to Stop here1");
					remainingOutstanding=remainingOutstanding-Sh1.getRow(iSelect).getCell((short)1).getNumericCellValue();
					System.out.println("^^^" +Sh1.getRow(iColumn).getCell((short)1).getNumericCellValue());
					System.out.println("---- "+remainingOutstanding);
				}
										
				if(remainingOutstanding<=0.00)
				{
					ClearDate=Sh1.getRow(iSelect).getCell((short)2).getDateCellValue();
					System.out.println("Got it: "+ClearDate);
					break; */
		
		
	}
			
	
}

	
	
	

