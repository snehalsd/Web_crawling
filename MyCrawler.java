import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
			 + "|png|mp3|mp3|zip|gz))$");
	private static final Pattern downloadPatterns = Pattern.compile(".*(\\.(pdf|doc|html|htm|docx?))$");
	private static final Pattern uscPatterns = Pattern.compile("^https?://[^/]*usc.edu.*");
	 private static final Pattern schoolPatterns = Pattern.compile("^https?://[^/]*dornsife.usc.edu.*"); 
	FileWriter fw = null;
	 FileWriter fw1 = null;
	 FileWriter fw2 = null;
	private static File storageFolder;
	private static String[] crawlDomains;
	private fileSync collectdata;
	@Override
	 public void onStart() {
		 collectdata = new fileSync();
	 }
	 
	 @Override
	 public Object getMyLocalData() {
		    return collectdata;
		  }

			 @Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
			
			 String href = url.getURL().toLowerCase();
			 List<url> UrlDataList = collectdata.getUrldata();
			 url urldata = new url();
			 urldata.setUrl(url.getURL());
			 Location location;
			 	
			 
				   if (url.getURL().contains("dornsife.usc.edu"))
				   {
				   location = Location.OK;
				   collectdata.setUniqueUrlinSchool(Integer.toString(url.getURL().hashCode()));
				   }
				   else if(url.getURL().contains("usc.edu")){
					   
				   location= Location.USC;
				   collectdata.setUniqueUrlOutsideSchool(Integer.toString(url.getURL().hashCode()));
				   }
				   else
				   {
					location = Location.OutUSC;
					collectdata.setUniqueUrlOutUsc(Integer.toString(url.getURL().hashCode()));
					
				   }
				     
				   urldata.setLocation(location);
				   UrlDataList.add(urldata);
				   collectdata.setUrldata(UrlDataList);
				   collectdata.setTotal_urls(collectdata.getTotal_urls()+1);
				   
				   
			 return !FILTERS.matcher(href).matches() && href.startsWith("http://dornsife.usc.edu/");
			 }
			 
	public static void configure(String[] domain, String storageFolderName) {
			  crawlDomains = domain;
			  storageFolder = new File(storageFolderName);
			  if (!storageFolder.exists()) {
				      storageFolder.mkdirs();
				    }
			   }
	private void setSizeCount(int length) {
		if(length < 1024){
			collectdata.setSize1(collectdata.getSize1() + 1);
		}else if(length < 10240){
			collectdata.setSize2(collectdata.getSize2() + 1);
		}else if(length < 102400){
			collectdata.setSize3(collectdata.getSize3() + 1);
		}else if(length < 1024000){
			collectdata.setSize4(collectdata.getSize4() + 1);
		}else{
			collectdata.setSize5(collectdata.getSize5() + 1);
		}
		
 }
	private void setContentType(String type) {
		if(type.compareTo("application/pdf")== 0){
			collectdata.setType_pdf(collectdata.getType_pdf() + 1);
		}else if(type.compareTo("image/gif") == 0){
			collectdata.setType_gif(collectdata.getType_gif() + 1);
		}else if(type.compareTo("application/msword")== 0){
			collectdata.setType_doc(collectdata.getType_doc() + 1);
		}else if(type.compareTo("text/html")== 0){
			collectdata.setType_html(collectdata.getType_html() + 1);
		}else if (type.compareTo("application/vnd.openxmlformats-officedocument.wordprocessingml.document")== 0){
			collectdata.setType_docx(collectdata.getType_docx() + 1);
		}else if (type.compareTo("image/jpeg")== 0){
			collectdata.setType_jpeg(collectdata.getType_jpeg() + 1);
		}else if (type.compareTo("image/png") == 0){
			collectdata.setType_png(collectdata.getType_png() + 1);
		}else{
			collectdata.setType_htm(collectdata.getType_htm() + 1);
		}		
}
	@Override
	 protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
	   //System.out.println("Status Code is: " + statusCode + " for " + webUrl.getURL());
	   //System.out.println(" in status code");
	   collectdata.setFetch_attempted(collectdata.getFetch_attempted() + 1);
	   if(statusCode == 200){
		   collectdata.setStatusok(collectdata.getStatusok() + 1);
		   collectdata.setFetch_succeeded(collectdata.getFetch_succeeded() + 1);
	   }
	   else if ( statusCode == HttpStatus.SC_MOVED_PERMANENTLY){
		   collectdata.setStatusmovedp(collectdata.getStatusmovedp() + 1);
		   collectdata.setFetch_aborted(collectdata.getFetch_aborted() + 1);
		   
	   }
	   else if ( statusCode == HttpStatus.SC_MOVED_TEMPORARILY){
		   collectdata.setStatusmovedt(collectdata.getStatusmovedt()+1);
		   collectdata.setFetch_aborted(collectdata.getFetch_aborted() + 1);
		  
	   }
	   else if ( statusCode == HttpStatus.SC_FORBIDDEN){
		   collectdata.setStatus_forbidden(collectdata.getStatus_forbidden()+ 1);
		   collectdata.setFetch_failed(collectdata.getFetch_failed()+ 1);
	   }
	   else if (statusCode == HttpStatus.SC_NOT_FOUND)
	   {
		   collectdata.setStatus_notfound(collectdata.getStatus_notfound() + 1);
		   collectdata.setFetch_failed(collectdata.getFetch_failed()+ 1);
	   }
	   else if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
	            statusCode == HttpStatus.SC_MULTIPLE_CHOICES || statusCode == HttpStatus.SC_SEE_OTHER ||
	            statusCode == HttpStatus.SC_TEMPORARY_REDIRECT ||
	            statusCode == 308){
		   collectdata.setFetch_aborted(collectdata.getFetch_aborted() + 1);
		   
	   }
	   else{
		   collectdata.setStatus_unauthorized(collectdata.getStatus_unauthorized() + 1);
		   collectdata.setFetch_failed(collectdata.getFetch_failed()+ 1);
	   }
	      
	   List<Fetch> FetchDataList = collectdata.getFetchdata();
	   Fetch fetchdata = new Fetch();
	   fetchdata.setUrl(webUrl.getURL());
	   fetchdata.setStatuscode(statusCode);
	   FetchDataList.add(fetchdata);
	   collectdata.setFetchdata(FetchDataList);
	   //System.out.println("leaving status");
	   
	 }
	
	@Override
	public void visit(Page page) {
		     //System.out.println("in visit");
			 String url = page.getWebURL().getURL();
			 
			 //setSizeCount(page.getContentData().length);
			 //setContentType(page.getContentType().split(";")[0]);
			 String filename = "";
			 String encodedName = "";
			 String extension = "";
			 System.out.println("URL: " + url);
			 if (page.getParseData() instanceof HtmlParseData) {
			 setSizeCount(page.getContentData().length);
		     setContentType(page.getContentType().split(";")[0]); 
			 HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			 String text = htmlParseData.getText();
			 String html = htmlParseData.getHtml();
			 Set<WebURL> links = htmlParseData.getOutgoingUrls();
			 //System.out.println("Text length: " + text.length());
			 //System.out.println("Html length: " + html.length());
			 //System.out.println("Number of outgoing links: " + links.size());
			 //String extension = ".html";
			 //String hashedName = UUID.randomUUID() + extension;
			 //String filename =  storageFolder.getAbsolutePath() + "/" + hashedName;
			 try {
				 	String newurl = "";
					
					if(url.charAt(url.length()-1) == '/')
					{
						newurl = url.substring(0,url.length()-1);				
					}
					else
					{
						newurl = url;			
					}
					int pos = newurl.indexOf("//",0);
					String striped_url = newurl.substring(pos+2,newurl.length());
				 	encodedName = URLEncoder.encode(striped_url,"UTF-8");

				 	if((page.getContentType().split(";")[0]).compareTo("text/html")== 0){
				 		if(encodedName.contains("cfm"))
				 			extension = "";
				 		else
				 			extension = ".html";
				 			
					}
				   }
				 
				 
				 catch (Exception iox) {
					      logger.error("Failed to write file: " + filename, iox);
					    }

				 // store image
				 filename =  storageFolder.getAbsolutePath() + "/" + encodedName + extension;
			 try {
			   Files.write(page.getContentData(), new File(filename));
			   logger.info("Stored: {}", url);
			 } catch (IOException iox) {
			   logger.error("Failed to write file: " + filename, iox);
			 }
			 
			 List<visit> VisitDataList = collectdata.getVisitdata();
			 visit visitdata = new visit();
			 visitdata.setUrl(url);
			 visitdata.setContentlength(page.getContentData().length);
			 visitdata.setOutlinksize(links.size());
			 visitdata.setContentType(page.getContentType());
			 for (WebURL outlink : links )
			 {
				 visitdata.setOutlinks(outlink.toString()); 
			 }
			 VisitDataList.add(visitdata);
			 collectdata.setVisitdata(VisitDataList);
	
			 }
			 else
			 {
			  

				 if (downloadPatterns.matcher(url).matches())
				 {
					setSizeCount(page.getContentData().length);
				    setContentType(page.getContentType().split(";")[0]); 	
				    extension = url.substring(url.lastIndexOf('.'));
				    //String hashedName = UUID.randomUUID() + extension;

				    // store image
				    //String filename =  storageFolder.getAbsolutePath() + "/" + hashedName;
				    try {
				    	int pos = url.indexOf("//",0);
						String striped_url = url.substring(pos+2,url.length());
				    	encodedName = URLEncoder.encode(striped_url,"UTF-8");
				    	if((page.getContentType().split(";")[0]).compareTo("application/pdf")== 0){
				    		if((encodedName.contains("pdf")))	{
				    			extension = "";
				    		}
				    		else
				    		{
				    			extension = "pdf";
				    		}
				    }
				    	else if((page.getContentType().split(";")[0]).compareTo("application/msword")== 0){
				    		if((encodedName.contains("doc"))){
				    			extension = "";
				    		}
				    		else
				    		{
				    			extension = "doc";
				    		}
				    }
				    	else if((page.getContentType().split(";")[0]).compareTo("application/vnd.openxmlformats-officedocument.wordprocessingml.document")== 0){
				    		if((encodedName.contains("docx")))	{
				    			extension = "";
				    		}
				    		else
				    		{
				    			extension = "docx";
				    		}
				    }
				    	
				    	
				      filename =  storageFolder.getAbsolutePath() + "/" + encodedName + extension;	
				      Files.write(page.getContentData(), new File(filename));
				      logger.info("Stored: {}", url);
				    } catch (IOException iox) {
				      logger.error("Failed to write file: " + filename, iox);
				    }
				    List<visit> VisitDataList = collectdata.getVisitdata();
				    visit visitdata = new visit();
				    visitdata.setUrl(url);
				    visitdata.setContentlength(page.getContentData().length);
				    visitdata.setOutlinksize(0);
				    visitdata.setContentType(page.getContentType());
				    VisitDataList.add(visitdata);
				    collectdata.setVisitdata(VisitDataList);
			 }
			 
			 
			 }
			 
			 }
}
