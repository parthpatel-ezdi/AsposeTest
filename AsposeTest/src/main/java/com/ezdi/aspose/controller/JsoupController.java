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
}
