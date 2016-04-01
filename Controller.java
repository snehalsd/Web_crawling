import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class Controller {

	public static void main(String[] args) throws Exception {
		
		// TODO Auto-generated method stub
		System.setProperty("jsse.enableSNIExtension", "false");
		String crawlStorageFolder = "/Users/snehalsurendradesai/Documents/workspace/data/crawl";
        String[] crawlDomains={"http://dornsife.usc.edu/"};
		int numberOfCrawlers = 7;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        config.setMaxPagesToFetch(5000);
        config.setMaxDepthOfCrawling(5);
        //config.setPolitenessDelay(2500);
        config.setConnectionTimeout(10000);
        config.setIncludeBinaryContentInCrawling(true);
        config.setMaxDownloadSize(30000000);
        PageFetcher pageFetcher = new PageFetcher(config);
        config.setCrawlStorageFolder(crawlStorageFolder);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        FileWriter fw = null;
        FileWriter fw1 = null;
        FileWriter fw2 = null;
        FileWriter fw3 = null;
        { 
       	 try
       	 {   fw = new FileWriter("fetch.csv");
       		 fw.append("URL");
       		 fw.append(',');
       		 fw.append("Status Code");
       		 fw.append('\n');
       		 fw.flush();	 
       		 fw.close();
       	 }
       	 catch(Exception e){
       		 
       	 }
       	 try
       	 {   fw1 = new FileWriter("visit.csv");
       		 fw1.append("URL");
       		 fw1.append(',');
       		 fw1.append("SIZE");
       		 fw1.append(',');
       		 fw1.append("OUTLINKS");
       		 fw1.append(',');
       		 fw1.append("CONTENT-TYPE");
       		 fw1.append('\n');
       		 fw1.flush();	 
       		 fw1.close();
       	 }
       	 catch(Exception e){
       		 
       	 }
       	 try
       	 {   fw2 = new FileWriter("urls.csv");
       		 fw2.append("URL");
       		 fw2.append(',');
       		 fw2.append("LOCATION");
       		 fw2.append('\n');
       		 fw2.flush();	 
       		 fw2.close();
       	 }
       	 catch(Exception e){
       		 
       	 }
       }
        fw3 = new FileWriter("pagerankdata.csv");
        fw3.append("URL,Outgoing URLs \n");
        fw3.flush();
        fw3.close();
        controller.addSeed("http://www.dornsife.usc.edu");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        for (String domain : crawlDomains) {
            controller.addSeed(domain);
          }
       
        MyCrawler.configure(crawlDomains, crawlStorageFolder);
        controller.start(MyCrawler.class, numberOfCrawlers);
        List<Object> datalist = controller.getCrawlersLocalData();
        List<fileSync> collectdatalist = new ArrayList<fileSync>();
        for(Object data : datalist){	
        	collectdatalist.add((fileSync) data);}
        fw = new FileWriter("fetch.csv",true);
        fw1 = new FileWriter("visit.csv",true);
        fw2 = new FileWriter("urls.csv",true);
        fw3 = new FileWriter("pagerankdata.csv",true);
        int fetch_attempted = 0;
		 int fetch_succeeded  = 0;
		 int fetch_aborted  = 0;
		 int fetch_failed = 0;
		 int total_urls = 0;
		 int unique_urls = 0;
		 int urls_school = 0;
		 int urls_usc = 0;
		 int urls_outusc = 0;
		 int statusok  = 0;
		 int statusmovedp = 0;
		 int statusmovedt = 0;
		 int status_unauthorized = 0;
		 int status_forbidden = 0;
		 int status_notfound = 0;
		 int size1 = 0;
		 int size2 = 0;
		 int size3 = 0;
		 int size4 = 0;
		 int size5 = 0;
		 int type_html = 0;
		 int type_pdf = 0;
		 int type_doc = 0;
		 int type_docx = 0;
		 int type_htm = 0;
		 int type_jpeg = 0;
		 int type_gif = 0;
		 int type_png = 0;
        Set<String>uniqueURLInSchoolSet = new HashSet<String>();
        Set<String>uniqueURLOutSchoolSet = new HashSet<String>();
        Set<String>uniqueURLOutUSCSet = new HashSet<String>();
        for(fileSync collectdata : collectdatalist){
        	List<Fetch> fetchDataList = collectdata.getFetchdata();
       	 	for (Fetch fetchdata : fetchDataList){
       	 		fw.append(fetchdata.getUrl()+ "," + fetchdata.getStatuscode() + "\n");
       	 	}
       	 	List<visit> visitDataList = collectdata.getVisitdata();
       	 	for (visit visitdata : visitDataList){
       	 		fw1.append(visitdata.getUrl()+ "," + visitdata.getContentlength() + "," +visitdata.getOutlinksize() 
       	 		+ "," + visitdata.getContentType() + "\n");
       	 	String check_url = visitdata.getUrl();
   		 String newurl = "";
   		 String striped_url="";
   			
   			if(check_url.charAt(check_url.length()-1) == '/')
   			{
   				newurl = check_url.substring(0,check_url.length()-1)  + ".html";
   				
   			}

   			else
   			{   
   				String exti = FilenameUtils.getExtension("/Users/snehalsurendradesai/Desktop/crawl/" + check_url);
   				if(exti!="" || exti != null)
   				{
   					newurl = check_url + ".html";

   		 
   			    }
   				else
   					newurl = check_url;
   			}
   			int posi = newurl.indexOf("//",0);
   			striped_url = newurl.substring(posi+2,newurl.length());
       	 	fw3.append("/Users/snehalsurendradesai/Desktop/crawl/"+URLEncoder.encode(striped_url,"UTF-8"));
       	 for (int i=0;i<visitdata.getOutlinks().size();i++)
		  {   
       		check_url = visitdata.getOutlinks().get(i);
			 newurl = "";
			 striped_url="";
				
				if(check_url.charAt(check_url.length()-1) == '/')
				{
					newurl = check_url.substring(0,check_url.length()-1) + ".html";
					

				}
				else
				{   
					String exti = FilenameUtils.getExtension("/Users/snehalsurendradesai/Desktop/crawl/" + check_url);
					if(exti!="" || exti != null)
					{
						newurl = check_url;
				    }
					else
						newurl = check_url + ".html";
						
				}
				posi = newurl.indexOf("//",0);
				striped_url = newurl.substring(posi+2,newurl.length());
				 
       		 fw3.append( ","+ "/Users/snehalsurendradesai/Desktop/crawl/"+URLEncoder.encode(striped_url,"UTF-8"));
			  //System.out.println(visitdata.getOutlinks().get(i));
		  }
		  fw3.append("\n");}
    		List<url> urlsDataList = collectdata.getUrldata();
    		for (url urlsdata : urlsDataList){
    			 fw2.append(urlsdata.getUrl()+ "," + urlsdata.getLocation()+ "\n");
    		
    	 }
    		fetch_attempted += collectdata.getFetch_attempted();
    		 fetch_succeeded  += collectdata.getFetch_succeeded();
    		 fetch_aborted  += collectdata.getFetch_aborted();
    		 fetch_failed += collectdata.getFetch_failed();
    		 total_urls += collectdata.getTotal_urls();
    		 unique_urls += collectdata.getUnique_urls();
    		 uniqueURLInSchoolSet.addAll(collectdata.getUniqueUrlinSchool());
    		 uniqueURLOutSchoolSet.addAll(collectdata.getUniqueUrlOutsideSchool());
    		 uniqueURLOutUSCSet.addAll(collectdata.getUniqueUrlOutUsc());
    		 statusok  += collectdata.getStatusok();
    		 statusmovedp += collectdata.getStatusmovedp();
    		 statusmovedt += collectdata.getStatusmovedt();
    		 status_unauthorized += collectdata.getStatus_unauthorized();
    		 status_forbidden += collectdata.getStatus_forbidden();
    		 status_notfound += collectdata.getStatus_notfound();
    		 size1 += collectdata.getSize1();
    		 size2 += collectdata.getSize2();
    		 size3 += collectdata.getSize3();
    		 size4 += collectdata.getSize4();
    		 size5 += collectdata.getSize5();
    		 type_html += collectdata.getType_html();
    		 type_pdf += collectdata.getType_pdf();
    		 type_doc += collectdata.getType_doc();
    		 type_docx += collectdata.getType_docx();
    		 type_htm += collectdata.getType_htm();
    		 type_jpeg += collectdata.getType_jpeg();
    		 type_gif += collectdata.getType_gif();
    		 type_png += collectdata.getType_png();
        }
        fw.close();
        fw1.close();
        fw2.close();
        fw3.close();
        urls_school = uniqueURLInSchoolSet.size();
        urls_usc = uniqueURLOutSchoolSet.size();
        urls_outusc = uniqueURLOutUSCSet.size();
        FileWriter report = new FileWriter("CrawlerReport.txt");
        report.append("Name: Snehal Surendra Desai\n");
        report.append("USC ID: 2159150427\n");
        report.append("School crawled: Dornsife (College)\n");
        
        report.append("\nFetch Statistics\n");
        report.append("================\n");
        report.append("# fetches attempted: " + fetch_attempted +"\n");
        report.append("# fetches succeeded: " + fetch_succeeded + "\n");
        report.append("# fetches aborted: " + fetch_aborted + "\n");
        report.append("# fetches failed: " + fetch_failed + "\n");
        
        report.append("\nOutgoing URLs:\n");
        report.append("================\n");
        report.append("Total URLs extracted: " + total_urls +"\n");
        report.append("# unique URLs extracted: " + (urls_school+ urls_usc + urls_outusc)+ "\n");
        report.append("# unique URLs within School: " + urls_school + "\n");
        report.append("# unique USC URLs outside School: " + urls_usc + "\n");
        report.append("# unique URLs outside USC: " + urls_outusc + "\n");
        
        report.append("\nStatus Codes:\n");
        report.append("================\n");
        report.append("200 OK: " + statusok + "\n");
        report.append("301 Moved Permanently: " + statusmovedp + "\n");
        report.append("302 Moved Temporarily:" + statusmovedt + "\n");
        report.append("401 Unauthorized: " + status_unauthorized + "\n");
        report.append("403 Forbidden: " + status_forbidden + "\n");
        report.append("404 Not Found: " + status_notfound + "\n");
        
        
        report.append("\nFile Sizes:\n");
        report.append("================\n");
        report.append("< 1KB: " + size1 + "\n");
        report.append("1KB ~ <10KB: " + size2 + "\n");
        report.append("10KB ~ <100KB: " + size3 + "\n");
        report.append("100KB ~ <1MB: " + size4 + "\n");
        report.append(">= 1MB: " + size5 + "\n");
        
        report.append("\nContent Types:\n");
        report.append("================\n");
        report.append("text/html: " + type_html + "\n");
        report.append("text/htm: " + type_htm + "\n");
        report.append("application/doc: " +  type_doc +"\n");
        report.append("application/docx: " +  type_docx + "\n");
        report.append("image/gif: " + type_gif + "\n");
        report.append("image/jpeg: " + type_jpeg + "\n");
        report.append("image/png: " + type_png + "\n");
        report.append("application/pdf: " + type_pdf +  "\n");
        report.flush();
        report.close();
        
        	
	}

}
