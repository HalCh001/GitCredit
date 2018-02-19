package projectData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


public class ProjectVariables 
{
	
public static void main(String args[]) throws IOException, ParseException
	{
	
	}	




public static Date GetLastofLastCycleDate(Date sPaymentDate1)
{
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(sPaymentDate1);
    calendar.add(Calendar.MONTH, -2);
    calendar.setTime(calendar.getTime()); 
	return calendar.getTime();
	
}

public static Date GetLastCycleDate(Date sPaymentDate1) throws IOException
{
	Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
	if (spConstants.sCardType=="CiTi")
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sPaymentDate1);
	    calendar.add(Calendar.DATE, -(spConstants.iBillCycleDuration));
	    calendar.setTime(calendar.getTime());
		return calendar.getTime();
	}
	
	else
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sPaymentDate1);
	    calendar.add(Calendar.MONTH, -1);
	    calendar.setTime(calendar.getTime());
		return calendar.getTime();
	}	
}

public static Date GetNextCycleDate1(Date sPaymentDate1) throws IOException
{
	Constants spConstants = ProjectVariables.GetStatementAndPaymentDate();
	if (spConstants.sCardType=="CiTi")
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sPaymentDate1);
	    calendar.add(Calendar.DATE, (spConstants.iBillCycleDuration));
	    calendar.add(Calendar.DATE, -1);
	    calendar.setTime(calendar.getTime());
		return calendar.getTime();
	}
	
	else
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sPaymentDate1);
	    calendar.add(Calendar.MONTH, 1);
	    calendar.setTime(calendar.getTime());
		return calendar.getTime();
	}	
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
		prop.setProperty("ExcelLocation","S:\\Selenium\\CreditCardSetUp\\DataTables\\Credit card Statement.xls");
				
		prop.setProperty("PaymentDueDate","2018-02-01");		//Anu Details
		prop.setProperty("StatementDate","2018-02-12");
		prop.setProperty("AnnualInterestRate", "0.408");
		prop.setProperty("ServiceTaxRate", "0.18");
		prop.setProperty("CardType","CiTi");
		prop.setProperty("Bill Cycle Duration","29");
		
		/*prop.setProperty("PaymentDueDate","2017-11-05");		//Riyasree Details
		prop.setProperty("StatementDate","2017-11-16");
		prop.setProperty("AnnualInterestRate", "0.408");
		prop.setProperty("ServiceTaxRate", "0.18");
		prop.setProperty("CardType","CiTi");
		prop.setProperty("Bill Cycle Duration","31");*/
		
				
		prop.store(fos, "Updated"); 
		
		String sPaymentDate= prop.getProperty("PaymentDueDate");
		String sStatementDate= prop.getProperty("StatementDate");
		String sCardType=prop.getProperty("CardType");
		
		String sExcelLocation=prop.getProperty("ExcelLocation");
		
		
		Double dAnnualInterestRate= Double.parseDouble(prop.getProperty("AnnualInterestRate"));
		Double dServiceTaxRate= Double.parseDouble(prop.getProperty("ServiceTaxRate"));
		int iBillCycleDuration= Integer.parseInt(prop.getProperty("Bill Cycle Duration"));
		Constants rConstants= new Constants(sStatementDate,sPaymentDate,iBillCycleDuration,dAnnualInterestRate,dServiceTaxRate,sCardType,sExcelLocation);
	
		return rConstants;
	}

public static String DateConversion(Date dt) throws ParseException 
{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		return dateFormat.format(dt);
	}
}




 
	









