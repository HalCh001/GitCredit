package creditCalculation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projectData.Constants;
import projectData.ProjectVariables;

public class LatePaymentFee 

{
	
	public static Boolean EligibleForLateFine(Double Amount) throws IOException, ParseException
	{
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sPaymentDate = sdf.parse(spConstants.sPmnt);
		Date sStatementDate = sdf.parse(spConstants.sSt);
		Date dtLastBillStatementDate=ProjectVariables.GetLastCycleDate(sStatementDate);
		Double dAmountPaidByCustomer=0.00;
		ArrayList<CreditData> CD= new ArrayList<CreditData>();	
		CD= creditCalculation.TransactionAmount.GetTransactionAmount();
		List<Double> LastCycleDue=LastBillCycle.GetDues(sStatementDate);
		System.out.println("### Last Cycle Minimum Amount Due is: "+LastCycleDue.get(1));
		
        

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
		System.out.println("### Amount Paid By customer from: "+dtLastBillStatementDate+ " To: "+sPaymentDate+ " is: Rs. "+dAmountPaidByCustomer);
       
       if (dAmountPaidByCustomer>=LastCycleDue.get(1))
       {
    	   return false;
       }
       else
       {    	   
    	   return true;
       }       		
	}
				
	public static double GetLateFee(Double Amount) throws IOException, ParseException
	{
		Double dLatePaymentFee=0.00;
		Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sPaymentDate = sdf.parse(spConstants.sPmnt);	        
		Double sTotalOutstanding=Amount;
		
		if(EligibleForLateFine(Amount))
		{			
			if (sTotalOutstanding>0.00 && sTotalOutstanding<= 10001.00)
			{
				dLatePaymentFee= 300*(1+(spConstants.dSrvcTaxRate));
			}
			else if (sTotalOutstanding >= 10001 && sTotalOutstanding <=26000)
			{
				dLatePaymentFee= 600*(1+(spConstants.dSrvcTaxRate));
			}
			
			else if (sTotalOutstanding>26000)
			{
				dLatePaymentFee= 950*(1+(spConstants.dSrvcTaxRate));
			}

		}
		else
		{
			System.out.println("### Thank you for clearing your Minimum Amount Due before " +sPaymentDate);
			System.out.println(" ");
		}
  
	return dLatePaymentFee;
  }
}
