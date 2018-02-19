package creditCalculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import projectData.Constants;
import projectData.ProjectVariables;

public class Interest 
{
	
	public static int getDifferenceDays(Date d1, Date d2) 
	{
	    int daysdiff = 0;
	    long diff = d2.getTime() - d1.getTime();
	    long diffDays = diff / (24 * 60 * 60 * 1000);
	    daysdiff = (int) diffDays;
	    
	    if(daysdiff<0)
	    {
	    	daysdiff=0;
	    }	    	    
	    return daysdiff+1;
	    
	}

    public static Boolean LastBillCycleOutstandingClearedBeforePaymentDueDate() throws IOException, ParseException
    {
    	
    	Double PaymentTowardsLastBillCycleOutstanding=0.00;
    	ArrayList<CreditData> CD= new ArrayList<CreditData>();
    	CD= creditCalculation.TransactionAmount.GetTransactionAmount();
		Collections.reverse(CD);
		
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sPaymentDate = sdf.parse(spConstants.sPmnt);
        Date sStatementDate = sdf.parse(spConstants.sSt);
        Date dtLastBillStatementDate=ProjectVariables.GetLastCycleDate(sStatementDate);
        
    	List<Double> LastBillCycleDue= new ArrayList<Double>();
    	LastBillCycleDue= LastBillCycle.GetDues(sStatementDate);
    	
    	Double LastCycleOutstanding=LastBillCycleDue.get(0);
    	
    	for(int iTrans=0; iTrans<CD.size();iTrans++)
		{  
		   if(!CD.get(iTrans).PaymentDt.after(sPaymentDate) && CD.get(iTrans).PaymentDt.after(dtLastBillStatementDate))
			{
			 
			   if(CD.get(iTrans).sPayType.contains("CustomerPaymentToBank"))
			   {
				   PaymentTowardsLastBillCycleOutstanding= PaymentTowardsLastBillCycleOutstanding+ CD.get(iTrans).TrnsAmount;
			   }
			}
		}
    	
    	if((PaymentTowardsLastBillCycleOutstanding-LastCycleOutstanding) >= 0.00)
    	{
    		System.out.println("### Thank you for Clearing COMPLETE Outstanding as shared in Last Bill Cycle dated: "+dtLastBillStatementDate);
    		System.out.println();
    		return false;
    	}
    	
    	else
    	{
    		System.err.println("Sorry..You missed to Pay Complete Outstanding as shared in Last Bill Cycle. Hence wille charged for all the Purchases with no Interest Free Period");  
    		System.out.println();
    		return true;
    	}    	
    }
		
	public static Double CalculateInterestAndServiceTax(Double Amount) throws IOException, ParseException
	{
		
		Double sTotalOutstanding=Amount;
		Boolean bConsidered=true;
		int IntterestFreePeriodConsidered=0;
		Double dInterestAndServiceTax=0.00, dTransAmount=0.00,dInterest=0.00,dServiceTax=0.00;
		ArrayList<CreditData> CD= new ArrayList<CreditData>();
		ArrayList<CreditData> CCD= new ArrayList<CreditData>();
		CD= creditCalculation.TransactionAmount.GetTransactionAmount();
		Collections.reverse(CD);
		
		 
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sStatementDate = sdf.parse(spConstants.sSt);
        Date dtNextDate;
        Calendar calendar = Calendar.getInstance();
        
        DecimalFormat df = new DecimalFormat("#.##");
        
        Date dtLastBillStatementDate=ProjectVariables.GetLastCycleDate(sStatementDate);
        List<Double> dLastBillCycleDues=LastBillCycle.GetDues(sStatementDate);
        
        System.out.println("*** Last Bill Cycle Statement Date: "+dtLastBillStatementDate);
        System.out.println("*** Last Cycle O/S is: "+dLastBillCycleDues.get(0));
        System.out.println("*** Last Cycle Minimum Amount Due is: "+dLastBillCycleDues.get(1));
        System.out.println("*** Principle Outstanding as of: "+sStatementDate+ "is:"+ +sTotalOutstanding);
        System.out.println();
        
        File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis3= new FileInputStream(F1);
		HSSFWorkbook Ex3= new HSSFWorkbook(Fis3);
		HSSFSheet Sh3= Ex3.getSheetAt(2);
		int cellCount=Sh3.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];
			
        
       
        if (LastBillCycleOutstandingClearedBeforePaymentDueDate())
        	{ 
        	  System.out.println("+++++---Welcome to Interest TRAP---++++++");
        	  System.out.println("-----------------------------------------");
        	  
        	  if (spConstants.sCardType=="AMEX")
        	  {
        		  dtLastBillStatementDate=Outstanding.DateOfLastBillCycleOutstandingCleared();
        	  }
			  for(int iTrans=0; iTrans<CD.size();iTrans++) //changed iTrans=1 from 0
				{  
				  
				   if(!CD.get(iTrans).PaymentDt.after(sStatementDate) && CD.get(iTrans).PaymentDt.after(dtLastBillStatementDate))
					{
					   
					   
					   if (bConsidered && CD.get(iTrans).sPayType.contains("CustomerPaymentToBank") && IntterestFreePeriodConsidered==0)
					    {						   
						   //List<Double> remainingOutstanding = LastBillCycle.GetDues(sStatementDate);
						   //CD.get(iTrans).TrnsAmount = CD.get(iTrans).TrnsAmount -remainingOutstanding.get(0);
						   CCD.add(CD.get(iTrans));
						   
						   IntterestFreePeriodConsidered++;
					    }
					   
					   else
					   {					   
						   CCD.add(CD.get(iTrans));
					   }					  
					}
				   				   
				}
			  
			  for(int iTrans=0; iTrans<CCD.size();iTrans++)
			  {
				  if(iTrans<CCD.size()-1)
				   {
					   if (getDifferenceDays(CCD.get(iTrans).PaymentDt,CCD.get(iTrans+1).PaymentDt)==1 )
					   {
						   bConsidered=false;
					   }
					   else
					   {

						   bConsidered=true;
					   }					   
				   }
				  
				  else
				  {
					  bConsidered=true;
				  }
				  
				  if (CCD.get(iTrans).sPayType.contains("BankCreditToCustomer"))
				  {
					  if(CCD.get(iTrans).EMI==null)
					  {
						  CCD.get(iTrans).EMI=0.00;
					  }
					  
					  if(CCD.get(iTrans).EMI==0.00)
					  {
						  dTransAmount= dTransAmount+ CCD.get(iTrans).TrnsAmount;
						  System.out.println("EMI not added for "+CCD.get(iTrans).TrnsAmount );
					  }
					  
					  else
					  {
						  dTransAmount= dTransAmount + CCD.get(iTrans).EMI;
						  System.out.println("EMI added for "+CCD.get(iTrans).TrnsAmount );
					  }
				  }
				  
				  else if (CCD.get(iTrans).sPayType.contains("LastBillCycleOustanding") && spConstants.sCardType!="AMEX")
				  {
					  dTransAmount= dTransAmount+ CCD.get(iTrans).TrnsAmount;
					  IntterestFreePeriodConsidered++;
				  }
				  
				  else if (CCD.get(iTrans).sPayType.contains("CustomerPaymentToBank"))
				  {
					  dTransAmount= dTransAmount- CCD.get(iTrans).TrnsAmount;
				  }
			  
				  if(bConsidered)
					{ 
					  int Row= Sh3.getPhysicalNumberOfRows();
					  
						  if ((iTrans==CCD.size()-1) || (getDifferenceDays(CCD.get(iTrans).PaymentDt,sStatementDate)==1) )
						  {
							  calendar.setTime(sStatementDate);
							  calendar.add(Calendar.DATE, 1);
							  dtNextDate=calendar.getTime();
						  }
						  
						  else
						  {
							  dtNextDate= CCD.get(iTrans+1).PaymentDt;
						  }					  				  
						  				  
						  calendar.setTime(dtNextDate);
						  calendar.add(Calendar.DATE, -1);
						  dtNextDate=calendar.getTime();
						  
						  
						  int iDays= getDifferenceDays(CCD.get(iTrans).PaymentDt,dtNextDate);
						  
						  /*if (dtNextDate!=sStatementDate )
						  {
							  calendar.setTime(dtNextDate);
							  calendar.add(Calendar.DATE, 1);
							  dtNextDate=calendar.getTime();
						  }*/
					  
		                  if(dTransAmount>0.00)	
		                  {
		                	  dInterest=(dTransAmount*(spConstants.dIntrstRate)*(iDays))/365;
						  	  dServiceTax=dInterest*(spConstants.dSrvcTaxRate);	
							  System.out.println("*** Interest on Credit Amount: Rs" +dTransAmount+ " from " +CCD.get(iTrans).PaymentDt +" to "+ dtNextDate + " is Rs "+dInterest);
							  System.out.println("** Service Tax on Service tax on interest Rs " +dServiceTax);
							  System.out.println("Interest charged for: "+iDays+"Days");	                  
							  System.out.println("---------------------------------------");
		                  
							  Header[0]=Sh3.getRow(0).getCell((short) 0).getStringCellValue();
							  Header[1]=Sh3.getRow(0).getCell((short) 1).getStringCellValue();
							  Header[2]=Sh3.getRow(0).getCell((short) 2).getStringCellValue();
							  Header[3]=Sh3.getRow(0).getCell((short) 3).getStringCellValue();
							  Header[4]=Sh3.getRow(0).getCell((short) 4).getStringCellValue();
							  
							  	if (Header[0].contains("Cumulative Transaction Acoount"))
								{					
									Sh3.createRow(Row).createCell((short) 0).setCellValue(dTransAmount);							
								}
													
								if (Header[1].contains("Period"))
								{
									HSSFCellStyle cellStyle= Ex3.createCellStyle();
									cellStyle.setDataFormat((short)14);
									Sh3.createRow(Row).createCell((short) 1).setCellValue(ProjectVariables.DateConversion(CCD.get(iTrans).PaymentDt) +"   -->   "+ ProjectVariables.DateConversion(dtNextDate));
									Sh3.getRow(Row).getCell((short) 1).setCellStyle(cellStyle);
								}
								
								if (Header[2].contains("No of Days"))
								{
									Sh3.createRow(Row).createCell((short) 2).setCellValue(df.format(iDays));					
								}
								
								if (Header[3].contains("Interest Amount"))
								{	
									Sh3.createRow(Row).createCell((short) 3).setCellValue(dInterest);
								}
							  
								if (Header[4].contains("Service Tax"))
								{				
									Sh3.createRow(Row).createCell((short) 4).setCellValue(dServiceTax);
								}
							  
							  	FileOutputStream fos=new FileOutputStream(new File(spConstants.sExcelLocation));
								Ex3.write(fos);
								fos.close();	
							  
		                  
		                  
								dInterestAndServiceTax=dInterestAndServiceTax+dInterest+dServiceTax; 
						  
		                  }
		                  
						}
				   
				}
        	}
        return dInterestAndServiceTax;
		
		
	}
	
}