package com.offcn.catchs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.offcn.po.baicai;
import com.offcn.service.shopService;

//*****
public class catchNum {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml","spring-service.xml");
		shopService service = context.getBean(shopService.class);
		
		for (int i = 1; i < 35; i++) {
			
		
		String url="http://www.xinfadi.com.cn/marketanalysis/0/list/"+i+".shtml?prodname=%E5%A4%A7%E7%99%BD%E8%8F%9C&begintime=2017-01-01&endtime=2018-11-01";
		
		String gethtml = gethtml(url);
		List<baicai> partsHtml = partsHtml(gethtml);
		
		service.saves(partsHtml);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	//获取源码
	public static String gethtml(String url){
		String html=null;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity();
				html=EntityUtils.toString(entity, "utf-8");
			}
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//System.out.println(html);
		
		return html;
	}
//	/解析数据
	public static List<baicai> partsHtml(String html){
		List<baicai> list = new ArrayList<baicai>();
		
		Document doc = Jsoup.parse(html);
		Elements table = doc.select(".hq_table");
		Elements trs = table.select("tr");
		trs.remove(0);
		for (Element e : trs) {
			String row = e.text();
			String[] ds = row.split(" ");
			baicai b = new baicai();
			b.setName(ds[0]);
			b.setLowprice(Double.valueOf(ds[1]));
			b.setAvgprice(Double.valueOf(ds[2]));
			b.setHighprice(Double.valueOf(ds[3]));
			b.setGuige(ds[4]);
			b.setUnit(ds[5]);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				b.setCreatedate(df.parse(ds[6]));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(b);
			list.add(b);
			
			
		}
		
		
		
		return list;
		
	}
	
	

}
