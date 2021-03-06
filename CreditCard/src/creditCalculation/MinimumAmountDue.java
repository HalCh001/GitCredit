package creditCalculation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projectData.Constants;
import projectData.ProjectVariables;

public class MinimumAmountDue {

	public static void main(String[] args) throws IOException, ParseException 
	{
		
	}
	
	public static Double GetMinimumAmountDue(Double Amount,Double TotalInterest,Boolean LastCycleMADPaid) throws IOException, ParseException
		{
			Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date sStatementDate = sdf.parse(spConstants.sSt);
			Date sLastBillCycleDate=ProjectVariables.GetLastCycleDate(sStatementDate);
			Double sTotalOutstanding=Amount;
			List<Double> dLastBillCycleDues= LastBillCycle.GetDues(sStatementDate);
			Double LastCycleMAD=dLastBillCycleDues.get(1);
			Double TotalAmountPaidThisBillCycle=Outstanding.GetTotalAmountPaidByCustomer();
			
			Double MAD1=0.00,MAD2=0.00,dTotalEMI=0.00, MAD0=100.00,MAD=0.00;
			ArrayList<CreditData> CD= new ArrayList<CreditData>();	
			CD= creditCalculation.TransactionAmount.GetTransactionAmount();
			
			

	        for(int iTrans=0; iTrans<CD.size();iTrans++)
	        {
	        	if(!CD.get(iTrans).PaymentDt.after(sStatementDate) && CD.get(iTrans).PaymentDt.after(sLastBillCycleDate))
	        	{
		        	if(CD.get(iTrans).EMI >0.00)
		        	{
		        		dTotalEMI=dTotalEMI+CD.get(iTrans).EMI;
		        	}
	        	}
	        }
	        
	        
	        if (sTotalOutstanding>0.00)
	        {
	        	if(LastCycleMADPaid)
	        	{
	        		LastCycleMAD=0.00;
	        	}
		        MAD1= LastCycleMAD+ 0.05*(sTotalOutstanding-LastCycleMAD);  // 5% of remaining Outstanding
		        MAD2= LastCycleMAD+ dTotalEMI +TotalInterest+ (0.01*(dLastBillCycleDues.get(0)-TotalAmountPaidThisBillCycle-LastCycleMAD)); // 1% of remaining Outstanding
		        MAD=Math.max(Math.max(MAD1,MAD2),MAD0);
	        } 
	        
	        else
	        {
	        	MAD=0.00;
	        }

		return MAD;
}

}

