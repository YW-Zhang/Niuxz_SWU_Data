package com.nxz.crawler;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.nxz.dao.Company_Persistant;
import com.nxz.model.Company;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class SWU_Crawler implements PageProcessor{
	@Override
	public Site getSite() {
		return this.site;
	}
	
	@Override
	public void process(Page page) {
		if(page.getUrl().get() == ENTITY_LIST) {
			for(int i = 2; i < 7; i++){
				page.addTargetRequest(ENTITY_LIST + "&page=" + i);
			}
			page.addTargetRequests(page.getHtml().xpath("//div[@class='xxgk']/table/tbody/tr/td")
					.links().regex(ENTITY_URL).all());	
		}
		else if(page.getUrl().regex(ENTITY_PAGE_LIST).match()) {
			page.addTargetRequests(page.getHtml().xpath("//div[@class='xxgk']/table/tbody/tr/td")
					.links().regex(ENTITY_URL).all());
		}
		else if(page.getUrl().regex(ENTITY_URL).match()){
			try {
				process_info(page);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//针对特定招聘页面进行信息处理
	public void process_info(Page page) throws IOException {
		Company company = new Company();
		String result = "";
		
		Document doc = Jsoup.parse(page.getHtml().get());
		//处理头部信息
		company.setCompanyName(page.getHtml().xpath("//div[@class='xxgk']/h3/text()").get().trim());
		System.out.println(company.getCompanyName());
		
		company.setCompanyDate(page.getHtml().xpath("//div[@class='xxgk']/h6[@class='empinfo']/div[1]/font/text()").get().trim());
		System.out.println(company.getCompanyDate());
		
		company.setCompanyAddress(page.getHtml().xpath("//div[@class='xxgk']/h6[@class='empinfo']/div[2]/font/text()").get().trim());
		System.out.println(company.getCompanyAddress());
		
		//取得正文部分根结点
		Node all_content = doc.select("div.xxgk div:has(p)").get(0);
		
		result = format_content(extract_content(all_content)).trim();
		company.setCompanyContent(result);
//		System.out.println(company.getCompanyContent());
		
		company.setPositions(positions);
		System.out.println("positions:");
		for(String position : company.getPositions()) {
			System.out.println(position);
		}
		
		Company_Persistant company_p = new Company_Persistant();
		company_p.insert_to_file(company);
	}
	
	//提取内容
	public String extract_content(Node node) {
		String result = "";
		List<Node> all_nodes = node.childNodes();
		
		for(Node sub_node : all_nodes) {
			if(sub_node.nodeName().equals("div") || sub_node.nodeName().equals("p")) {
				result += extract_content(sub_node);
				result += "\n";
			}
			if(sub_node.nodeName().equals("table")) {
				result += "\n";
				extract_content(sub_node);
				result += sub_node.toString();
				result += "\n";
			}
			else if(sub_node.nodeName().equals("#text")) {
				extract_position(sub_node.toString());
				result += sub_node.toString();
			}
			else {
				result += extract_content(sub_node);
			}
		}
		return result;
	}
	
	public String format_content(String content) {
		return content.replace("&nbsp;", " ");
	}
	
	public void extract_position(String content) {
		String position_features = ".+(师|员|岗|助理)$";
		String separator_symbol = "[,|、|。|，|/|\\d|:|：|（|）|(|)|；|;|“|的]";
		
		String[] after_split = content.split(separator_symbol);
		for(String word : after_split) {
			word = word.trim();
			if(word.length() >= 4) {
				Pattern pattern = Pattern.compile(position_features);
				Matcher matcher = pattern.matcher(word);
				while (matcher.find()) {
					String position = matcher.group(0);
					if (position.length() >= 4 && position.length() <= 12) {
						this.positions.add(position);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Spider.create(new SWU_Crawler())
		.addUrl("http://bkjyw.swu.edu.cn/index.php/employ/listemp.html?url=xyzp")
		.thread(5)
		.run();
	}
	
	private List<String> positions = null;
	
	private static final String ENTITY_LIST = "http://bkjyw.swu.edu.cn/index.php/employ/listemp.html?url=xyzp";
	
	private static final String ENTITY_PAGE_LIST = "http://bkjyw\\.swu\\.edu\\.cn/index\\.php/employ/listemp\\.html\\?url=xyzp&page=\\d+";
	
	private static final String ENTITY_URL = "http://bkjyw\\.swu\\.edu\\.cn/index\\.php/employ/detailemp/\\d{4}\\.html\\?cid=4";
	
	private Site site = Site.me();
	
}
