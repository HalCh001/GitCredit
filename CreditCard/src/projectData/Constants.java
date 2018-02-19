package projectData;

public class Constants{
	
	public String sSt,sPmnt,sCardType,sExcelLocation;
	public Double dIntrstRate,dSrvcTaxRate;
	public int iBillCycleDuration;
	
	Constants(String sStatementDate,String sPaymentDate,int BillCycleDuration, Double dAnnualInterestRate, Double dServiceTaxRate,String CardType, String ExcelLocation )
	{
	 sSt=sStatementDate;
	 sPmnt=sPaymentDate;
	 dIntrstRate=dAnnualInterestRate;
	 dSrvcTaxRate=dServiceTaxRate;
	 sCardType=CardType;
	 iBillCycleDuration=BillCycleDuration;
	 
	 sExcelLocation=ExcelLocation;
	}
}
