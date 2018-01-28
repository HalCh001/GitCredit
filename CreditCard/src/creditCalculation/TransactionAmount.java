package creditCalculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TransactionAmount 
{

	public static ArrayList<CreditData> GetTransactionAmount() throws IOException 
	{
		ArrayList<CreditData> CD= new ArrayList<CreditData>();
		Double dTransAmount=0.00,dEMI=0.00;
		Date dtPayDate = null;
		String sPayType = null;
		int iList=0;
		
		File F1= new File("E:\\Automation Tool\\Selenium\\SeleniumCoding\\CreditCard\\DataTables\\Credit card Statement.xls");
		FileInputStream Fis= new FileInputStream(F1);
		HSSFWorkbook Ex1= new HSSFWorkbook(Fis);
		HSSFSheet Sh1= Ex1.getSheetAt(0);
		int rowCnt= Sh1.getPhysicalNumberOfRows();
		int cellCount=Sh1.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];

		for(int iRow=1; iRow<rowCnt; iRow++)
		{
			
			//HSSFRow Row= Sh1.getRow(iRow);
			
			for(short iCol=0; iCol<cellCount; iCol++)
				
			{	
				Header[iCol]=Sh1.getRow(0).getCell(iCol).getStringCellValue();
				
				if (Header[iCol].contains("Payment Type"))
				{
					sPayType= Sh1.getRow(iRow).getCell(iCol).getStringCellValue();
					//System.out.println("Value of Row" +iRow+ " Coloumn:"+ +iCol+" " +Header[iCol]+ " " +sPayType);
				}
				
				if (Header[iCol].contains("Transaction Amount"))
				{
					dTransAmount= Sh1.getRow(iRow).getCell(iCol).getNumericCellValue();
					//System.out.println("Value of Row" +iRow+ " Coloumn:" +iCol+" " +Header[iCol]+ " " +dTransAmount);
				}
				
				if (Header[iCol].contains("Payment Date"))
				{					
					dtPayDate= Sh1.getRow(iRow).getCell(iCol).getDateCellValue();										
					//System.out.println("Value of Row" +iRow+ " Coloumn:"+ +iCol+" " +Header[iCol]+ " " +dtPayDate);
				}
				if (Header[iCol].contains("EMI"))
				{
					dEMI= Sh1.getRow(iRow).getCell(iCol).getNumericCellValue();
					//System.out.println("Value of Row" +iRow+ " Coloumn:"+ +iCol+" " +Header[iCol]+ " " +dEMI);
				}
												
			}
				CreditData TR= new CreditData(sPayType,dTransAmount,dtPayDate,dEMI);
				CD.add(iList, TR);
				//System.out.println(" ");			
		}
				iList++;
				return CD;
	}
}




class CreditData
{  
	Double TrnsAmount,EMI ;
    Date PaymentDt;
    String sPayType;
    
 
    CreditData(String sPaymentType,Double dTransactionAmount,Date dtPurchaseDate,Double dEMI)
    {  
        TrnsAmount=dTransactionAmount;  
        PaymentDt=dtPurchaseDate;  
        sPayType=sPaymentType;
        EMI=dEMI;
        
    }
    
}






