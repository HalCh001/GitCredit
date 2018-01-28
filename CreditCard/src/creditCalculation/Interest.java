package creditCalculation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
		   if(!CD.get(iTrans).PaymentDt.after(sPaymentDate) && !CD.get(iTrans).PaymentDt.before(dtLastBillStatementDate))
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
        
        Date dtLastBillStatementDate=ProjectVariables.GetLastCycleDate(sStatementDate);
        List<Double> dLastBillCycleDues=LastBillCycle.GetDues(sStatementDate);
        
        System.out.println("*** Last Bill Cycle Statement Date: "+dtLastBillStatementDate);
        System.out.println("*** Last Cycle O/S is: "+dLastBillCycleDues.get(0));
        System.out.println("*** Last Cycle Minimum Amount Due is: "+dLastBillCycleDues.get(1));
        System.out.println("*** Principle Outstanding as of: "+sStatementDate+ "is:"+ +sTotalOutstanding);
        System.out.println();
        
       
        if (LastBillCycleOutstandingClearedBeforePaymentDueDate())
        	{ 
        	  System.out.println("+++++---Welcome to Interest TRAP---++++++");
        	  System.out.println("-----------------------------------------");
        	  
        	  if (spConstants.sCardType=="AMEX")
        	  {
        		  dtLastBillStatementDate=Outstanding.DateOfLastBillCycleOutstandingCleared();
        	  }
        	   
			  for(int iTrans=0; iTrans<CD.size();iTrans++)
				{  
				   if(CD.get(iTrans).PaymentDt.before(sStatementDate) && !CD.get(iTrans).PaymentDt.before(dtLastBillStatementDate))
					{
					   if (CD.get(iTrans).sPayType.contains("CustomerPaymentToBank") && IntterestFreePeriodConsidered==0)
					    {
						   List<Double> remainingOutstanding = LastBillCycle.GetDues(ProjectVariables.GetNextCycleDate(dtLastBillStatementDate));
						   //CCD.get(0).TrnsAmount= CCD.get(0).TrnsAmount -remainingOutstanding.get(0);
						   CD.get(iTrans).TrnsAmount= CD.get(iTrans).TrnsAmount -remainingOutstanding.get(0);
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
					  if (iTrans==CCD.size()-1)
					  {
						  dtNextDate=sStatementDate; 
						  //calendar.setTime(dtNextDate);
						  //calendar.add(Calendar.DATE, 1);
						  //dtNextDate=calendar.getTime();
					  }
					  else
					  {
						  dtNextDate= CCD.get(iTrans+1).PaymentDt;
					  }
					  				  
				  
					  if (CCD.get(iTrans).sPayType.contains("BankCreditToCustomer"))
					  {
						  dTransAmount= dTransAmount+ CCD.get(iTrans).TrnsAmount;
					  }
					  
					  else if (CCD.get(iTrans).sPayType.contains("LastBillCycleOustanding") && spConstants.sCardType!="AMEX")
					  {
						  dTransAmount= dTransAmount+ CCD.get(iTrans).TrnsAmount;
					  }
					  
					  else if (CCD.get(iTrans).sPayType.contains("CustomerPaymentToBank"))
					  {
						  dTransAmount= dTransAmount- CCD.get(iTrans).TrnsAmount;
					  }
					  				  
					  calendar.setTime(dtNextDate);
					  calendar.add(Calendar.DATE, -1);
					  dtNextDate=calendar.getTime();
					  
					  int iDays= getDifferenceDays(CCD.get(iTrans).PaymentDt,dtNextDate);
	                  if(dTransAmount>0.00)	
	                  {
	                	  dInterest=(dTransAmount*(spConstants.dIntrstRate)*(iDays))/365;
					  	  dServiceTax=dInterest*(spConstants.dSrvcTaxRate);	
						  System.out.println("*** Interest on Credit Amount: Rs" +dTransAmount+ " from " +CCD.get(iTrans).PaymentDt +" to "+ dtNextDate + " is Rs "+dInterest);
						  System.out.println("** Service Tax on Service tax on interest Rs " +dServiceTax);
						  System.out.println("Interest charged for: "+iDays+"Days");	                  
						  System.out.println("---------------------------------------");
						  
	                  }
	                  
					  dInterestAndServiceTax=dInterestAndServiceTax+dInterest+dServiceTax; 
					}
				   
				}
        	
		
		return dInterestAndServiceTax;
		
	}
	
}