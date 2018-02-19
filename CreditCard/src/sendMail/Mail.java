package sendMail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import creditCalculation.CreditCardData;
import creditCalculation.LastBillCycle;
import creditCalculation.Outstanding;
import projectData.Constants;
import projectData.ProjectVariables;

public class Mail 
{
	public static void main(String[] args) throws IOException, MessagingException, ParseException 
	{    
		Constants spConstants= ProjectVariables.GetStatementAndPaymentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sStatementDate = sdf.parse(spConstants.sSt);
        Date sPaymentDate = sdf.parse(spConstants.sPmnt);
		DecimalFormat df = new DecimalFormat("#.##");
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(ProjectVariables.GetLastCycleDate(sStatementDate));
		calendar.add(Calendar.DATE, 1);
		Date dLastStatementDate=calendar.getTime();
		List<Double> StatementDetails = CreditCardData.GetCompleteDetails();
		
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis3= new FileInputStream(F1);
		HSSFWorkbook Ex3= new HSSFWorkbook(Fis3);
		HSSFSheet Sh3= Ex3.getSheetAt(2);
		int cellCount=Sh3.getRow(0).getLastCellNum();
		int Row= Sh3.getPhysicalNumberOfRows();
		
		
		
		CreditCardData.CheckAndUpdateRowForLastMonthOutstanding(StatementDetails);
		CreditCardData.StoreCreditCardDataInExcel(StatementDetails);
		
		
		
		Multipart mp = new MimeMultipart();
		BodyPart Greetings = new MimeBodyPart();
		BodyPart textPart = new MimeBodyPart();
		BodyPart htmlPart = new MimeBodyPart();
		BodyPart htmlPart2 = new MimeBodyPart();
		BodyPart htmlPart1 = new MimeBodyPart();
		BodyPart Regards = new MimeBodyPart();
		
		Greetings.setText("Dear Customer," +"\n"+
					      "Please find your Credit Card Bill as below-" +"\n");
		
		
		
		
		
		
					      				
		textPart.setText("<b> Payment Due Date: </b>" +spConstants.sPmnt +"\n"+
						 "Bill Statement Duration: "+ProjectVariables.DateConversion(dLastStatementDate)+ " to "+ProjectVariables.DateConversion(sStatementDate) +"\n"+
						 "Yearly Interest Rate: "+spConstants.dIntrstRate+"\n"+
						 "Service Tax Rate: "+spConstants.dSrvcTaxRate +"\n"+
						 "Card Type: "+spConstants.sCardType +"\n");
		
		Regards.setText("With regards," +"\n"+
			      "Chiranjit" +"\n");
				
		
		StringBuilder  email = new StringBuilder();
		email.append("<html><body text-align: center>");
		email.append("<div");
		
		email.append("<p><em> Dear Customer," +"<br/>" +
					      "Please find your Credit Card Bill as below-" + "<em></p>");

		email.append("<p><b> Payment Due Date: </b>" +ProjectVariables.DateConversion(ProjectVariables.GetNextCycleDate1(sPaymentDate)) +"<br/>"+
				 "<b>Bill Statement Duration: </b>"+ProjectVariables.DateConversion(dLastStatementDate)+ " to "+ProjectVariables.DateConversion(sStatementDate) +"<br/>"+
				 "<b>Yearly Interest Rate: </b>"+spConstants.dIntrstRate+"<br/>"+
				 "<b>Service Tax Rate: </b>"+spConstants.dSrvcTaxRate +"<br/>"+
				 "<b>Card Type: </b>"+spConstants.sCardType + "<br/>"+ "</p>");

		email.append("<p style=\"color:Tomato;\"><em> #Total Due as in Last Statement:: Rs.</em>" +LastBillCycle.GetDues(sStatementDate).get(0) +"<p/>");
		email.append("<p style=\"color:MediumSeaGreen;\"><em> #Total Amount Paid by Customer in this Bill Cycle:: Rs.</em>" +Outstanding.GetTotalAmountPaidByCustomer()+"<p/>");
		
		email.append("<p style=\"color:Gray;\"><em><br/><b><i> Hello!..Important Points to remember.. </i></b><br/><em></p>");
        email.append("<table border = '1' border-collapse: collapse >");
        email.append("<tr>");
        
        email.append("<tr bgcolor=\"#33CC99\">");
        email.append("<th>");
        email.append("Principle Outstanding");
        email.append("</th>");
        email.append("<th>");
        email.append("Minimum Amount Due");
        email.append(" </th>");
        email.append("<th>");
        email.append("Interest & ServiceTax/Finance Charges");
        email.append("</th>");
        email.append("<th>");
        email.append("Late Fine");   
        email.append("</th>");
        email.append("</tr>");
                
        for(int iCnt=1;iCnt<2;iCnt++)
        {
        	email.append("<tr align=\"center\">");
        	for(int jCnt=0;jCnt<4;jCnt++)
        	{
		        email.append("<td >");
		        email.append("Rs." +df.format(StatementDetails.get(jCnt).doubleValue()));
		        email.append("</td>");	
        	}	    	
        	email.append("</tr>");
        }
       
        email.append("</table>");
        
        email.append("<p style=\"color:Orange;\"><em><br/><i>Hey, Anuradha!..Happy to Help you with Interest Breakup: </i><br/><em></p>");
        
        
        //2nd Table
        
        //email.append("<b><caption> Interest Breakup </caption></b>");
        
        email.append("<table style=\"width:100%\" border = '1' text-align: center >");
        
        email.append("<tr bgcolor=\"#33CC99\">");
        email.append("<th>");
        email.append("Cumulative Transaction Acoount");
        email.append("</th>");
        email.append("<th>");
        email.append("Period");
        email.append("</th>");
        email.append("<th>");
        email.append("No of Days");
        email.append("</th>");
        email.append("<th>");
        email.append("Interest Amount");   
        email.append("</th>");
        email.append("<th>");
        email.append("Service Tax");   
        email.append("</th>");
        email.append("</tr>");
        
        for(int iCnt=1;iCnt<Row;iCnt++)
        {
        	email.append("<tr align=\"center\">");
        	for(short jCnt=0;jCnt<cellCount;jCnt++)
        	{
        		email.append("<td>");
        		try
        		{
        			email.append(Sh3.getRow(iCnt).getCell(jCnt).getStringCellValue());
        		}
        		
        		catch(Exception e)
        		{
        			email.append("Rs."+df.format(Sh3.getRow(iCnt).getCell(jCnt).getNumericCellValue()));
        		}
        		email.append("</td>");	
        	}	    	
        }
       
        email.append("</table></body></html>");
        
        email.append("<p><em> With regards," +"<br/>"+
			      "Chiranjit" +"<br/>" +"<em></p>");
        

        //htmlPart2.setContent(email.toString(), "text/html; charset=utf-8");
        
        htmlPart1.setContent(email.toString(), "text/html; charset=utf-8");
              
        //mp.addBodyPart(Greetings);
        //mp.addBodyPart(textPart);
        //mp.addBodyPart(htmlPart1);
        mp.addBodyPart(htmlPart1);
        //mp.addBodyPart(htmlPart2);
        //mp.addBodyPart(Regards);
        

        SendEmailJar.send("AutoCreditMail@gmail.com","Test@12345#","chiranjit.halder@gmail.com","Citi Bank Credit Bill- Customised",mp);
        deleteRow();
    }

	public static void deleteRow() throws IOException 
	{
		
		Constants spConstants= ProjectVariables.GetStatementAndPaymentDate();
		File F1= new File(spConstants.sExcelLocation);
		FileInputStream Fis4= new FileInputStream(F1);
		HSSFWorkbook Ex4= new HSSFWorkbook(Fis4);
		HSSFSheet Sh4= Ex4.getSheetAt(2);
		int Row= Sh4.getPhysicalNumberOfRows();
        
        for (int iRow=1;iRow<Row;iRow++)
        {
        	HSSFRow RowToDelete= Sh4.getRow(iRow);
        	Sh4.removeRow(RowToDelete);
        }
        
        FileOutputStream fos1=new FileOutputStream(new File(spConstants.sExcelLocation));
		Ex4.write(fos1);
		fos1.close();	
	}
	    
	 }  