package creditCalculation;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import projectData.Constants;
import projectData.ProjectVariables;
import org.testng.annotations.Test;

public class MonthlyStatement {

	public static void main(String args[]) throws IOException, ParseException
	{
		Constants spConstants= ProjectVariables.GetStatementAndPaymentDate();
		
		System.out.println("****************************Project Constants*****************");
		System.out.println("@ Payment Due Date: "+spConstants.sPmnt);
		System.out.println("@ Bill Statement Date: "+spConstants.sSt);
		System.out.println("@ Monthly Interest Rate: "+spConstants.dIntrstRate);
		System.out.println("@ Service Tax Rate: "+spConstants.dSrvcTaxRate);
		System.out.println("@ Card Type: "+spConstants.sCardType);
		System.out.println("**************************************************************");
		
		try
		{
	        List<Double> StatementDetails = CreditCardData.GetCompleteDetails();
			CreditCardData.CheckAndUpdateRowForLastMonthOutstanding(StatementDetails);
			CreditCardData.StoreCreditCardDataInExcel(StatementDetails);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	
	
}
