package projectData;

public class Constants{
	
	public String sSt,sPmnt,sCardType,sExcelLocation;
	public Double dIntrstRate,dSrvcTaxRate;
	
	Constants(String sStatementDate,String sPaymentDate,Double dAnnualInterestRate, Double dServiceTaxRate,String CardType, String ExcelLocation )
	{
	 sSt=sStatementDate;
	 sPmnt=sPaymentDate;
	 dIntrstRate=dAnnualInterestRate;
	 dSrvcTaxRate=dServiceTaxRate;
	 sCardType=CardType;
	 sExcelLocation=ExcelLocation;
	}
}
