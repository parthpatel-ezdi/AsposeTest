/**
 * 
 */
package com.ezdi.aspose.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author parth.m
 *
 */
@RestController
public class JsoupController {

	@GetMapping("parse")
	public void parse(){
		String html = "<html><head><title>First parse</title></head><body><p class=\"abc\">Parsed HTML into a doc.</p><p class=\"xyz\">Parsed HTML into a doc.</p><p class=\"qwe\">Parsed HTML into a doc.</p></body></html>";
			/*Document doc = Jsoup.parse(html);
			for (Element element : doc.select("p.abc,p.xyz")) {
				TextNode text = new TextNode("foo");
				element.replaceWith(text);
				System.out.println(doc.html());
			}*/
			System.out.println(html);
			org.jsoup.nodes.Document doc = Jsoup.parse(html);
			for (org.jsoup.nodes.Element element : doc.select("p.abc,p.xyz")) {
				element.remove();
			}
			System.out.println(doc.html());
	}
	
	@GetMapping("addElement")
	public void addElement(){
		String html = "<div>\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>First line after start</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>&nbsp;</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>Hello there, how you doing, sounds good, okay then see you later.</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>&nbsp;</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>~!@#$%^&amp;*()_+|`1234567890-=\\</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>&nbsp;</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span style=\"display:inline-block; width:36pt\">&nbsp;</span><span>Tab1</span><span style=\"display:inline-block; width:14.17pt\">&nbsp;</span><span style=\"display:inline-block; width:36pt\">&nbsp;</span><span>tab2</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>qwertyuiopasdfghjklzxcvbnm</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>QWERTYUIOPASDFGHJKLZXCVBNM</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>&nbsp;</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>[];&rsquo;./{}:&rdquo;&gt;?</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>&nbsp;</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>1234567890.+-*/</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>&nbsp;</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>Last line before end</span></p>\n" + 
				"\n" + 
				"<p style=\"margin-top:0pt;  margin-bottom:0pt;\"><span>&nbsp;</span></p>\n" + 
				"</div>";
		 org.jsoup.nodes.Document doc = Jsoup.parse(html);
	        doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
	        doc.outputSettings().charset("UTF-8");
	        for (org.jsoup.nodes.Element element : doc.select("p > span")) {
	        	if (element.attr("style").isEmpty() && element.text().trim().replace("\u00a0","").isEmpty()) {
	        		 element.attr("style","-aw-import:ignore");
	        	 }
	        }
	        System.out.println(doc.body().child(0).toString());
	}
}
