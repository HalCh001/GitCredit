package sendMail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import projectData.Constants;
import projectData.ProjectVariables;

public class Mail 
{
	public static void main(String[] args) throws IOException 
	{    
		Constants spConstants= ProjectVariables.GetStatementAndPaymentDate();
		
		System.out.println("****************************Project Constants*****************");
		System.out.println("@ Payment Due Date: "+spConstants.sPmnt);
		System.out.println("@ Bill Statement Date: "+spConstants.sSt);
		System.out.println("@ Monthly Interest Rate: "+spConstants.dIntrstRate);
		System.out.println("@ Service Tax Rate: "+spConstants.dSrvcTaxRate);
		System.out.println("@ Card Type: "+spConstants.sCardType);
		System.out.println("**************************************************************");
		
		StringBuilder  email = new StringBuilder();
		
		email.append("Payment Due Date: "+spConstants.sPmnt);
		email.append("Bill Statement Date: "+spConstants.sSt);
		email.append("Monthly Interest Rate: "+spConstants.dIntrstRate);
		email.append("Service Tax Rate: "+spConstants.dSrvcTaxRate);
		email.append("Card Type: "+spConstants.sCardType);
		
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis= new FileInputStream(F1);
		HSSFWorkbook Ex1= new HSSFWorkbook(Fis);
		HSSFSheet Sh1= Ex1.getSheetAt(0);
				
		int cellCount=Sh1.getRow(0).getLastCellNum();
		String[] Header= new String[cellCount];
		int Row= Sh1.getPhysicalNumberOfRows();
				
		for (int iTrans=Row-1;iTrans>0;iTrans--)
		{
			for(short iCol=0;iCol<cellCount;iCol++ )
			{
				
			}
		
		
		
		
		
		
		
		
		
		
		email.append("<html><body>"
                + "<table border = '1'>");
        email.append("<tr bgcolor=\"#33CC99\">");
        
        email.append("<tr>");
        email.append("<th>");
        email.append("Transaction Amount");
        email.append("</th>");
        email.append("<th>");
        email.append("Payment Type");
        email.append("</th>");
        email.append("<th>");
        email.append("Transaction Date");
        email.append("</th>");
        email.append("<th>");
        email.append("Interest");   
        email.append("</th>");
        email.append("</tr>");
        
        for(int iCnt=1;iCnt<=4;iCnt++)
        {
        	email.append("<tr>");
        	for(int jCnt=1;jCnt<=4;jCnt++)
        	{
		        email.append("<td >");
		        email.append(iCnt+jCnt);
		        email.append("</td>");	
        	}	    	
        	email.append("</tr>");
        }
       
        email.append("</table></body></html>");
        
        
        SendEmailJar.send("AutoCreditMail@gmail.com","Test@12345#","chiranjit.halder@gmail.com","FirsMail",email.toString());    
    }

		
	    
	 }  
}
	
