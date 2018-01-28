package projectData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


public class ProjectVariables 
{
	
public static void main(String args[]) throws IOException, ParseException
	{
	
	}	
/*

public static String ConvertDateFormat(Date Dt)
{
	Calendar cal = Calendar.getInstance();
	cal.setTime(Dt);
	String formatedDate = cal.get(Calendar.YEAR)+ "-" + (cal.get(Calendar.MONTH + 1)+ "-"+ cal.get(Calendar.DATE));
	
	return formatedDate;
}

*/



public static Date GetLastofLastCycleDate(Date sPaymentDate1)
{
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(sPaymentDate1);
    calendar.add(Calendar.MONTH, -2);
    calendar.setTime(calendar.getTime()); 
	return calendar.getTime();
	
}

public static Date GetLastCycleDate(Date sPaymentDate1)
{
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(sPaymentDate1);
    calendar.add(Calendar.MONTH, -1);
    calendar.setTime(calendar.getTime());
	return calendar.getTime();
	
}

public static Date GetNextCycleDate(Date sPaymentDate1)
{
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(sPaymentDate1);
    calendar.add(Calendar.MONTH, 1);
    calendar.setTime(calendar.getTime());
	return calendar.getTime();
	
}
	
public static Constants GetStatementAndPaymentDate() throws IOException{
		String sProjectPath= System.getProperty("user.dir");		
		String sConstantFileLOcation=sProjectPath+ "\\DataTables\\constants.properties";
		Properties prop= new Properties();
		FileOutputStream fos = new FileOutputStream(sConstantFileLOcation);
		
		prop.setProperty("PaymentDueDate","2017-08-26");
		prop.setProperty("StatementDate","2017-09-05");
		prop.setProperty("AnnualInterestRate", "0.36");
		prop.setProperty("ServiceTaxRate", "0.14");
		prop.setProperty("CardType","AMEX");
		prop.store(fos, "Updated");
		String sPaymentDate= prop.getProperty("PaymentDueDate");
		String sStatementDate= prop.getProperty("StatementDate");
		String sCardType=prop.getProperty("CardType");
		
		
		Double dAnnualInterestRate= Double.parseDouble(prop.getProperty("AnnualInterestRate"));
		Double dServiceTaxRate= Double.parseDouble(prop.getProperty("ServiceTaxRate"));
		Constants rConstants= new Constants(sStatementDate,sPaymentDate,dAnnualInterestRate,dServiceTaxRate, sCardType);
	
		return rConstants;
	}

}



 
	









