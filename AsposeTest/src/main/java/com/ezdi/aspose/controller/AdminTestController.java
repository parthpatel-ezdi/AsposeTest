/**
 * 
 */
package com.ezdi.aspose.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.BookmarkEnd;
import com.aspose.words.BookmarkStart;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.SaveFormat;
import com.ezdi.aspose.Util;
import com.ezdi.common.util.document.AsposeUtil;

/**
 * @author parth.m
 *
 */
@RestController
public class AdminTestController {
	
	@Autowired
	AsposeUtil asposeUtil;

	@GetMapping("/adminRemoveTest")
	public void adminRemoveTest(@RequestParam("filename") String filename) throws Exception{
		
		String dataDir = Util.getDataDir();

		Document doc = new Document(dataDir + "/" + filename);
		
		Document blankDoc = getMarkedBody(doc);
		String str = getBodyHtml(blankDoc);
		System.out.println(str);
		
		removeMarkedBody(doc);
		
		doc.save(dataDir + "/"+filename+"_sourceRemoved.docx", SaveFormat.DOCX);
		
	}
	
	 private void removeMarkedBody(Document doc) throws Exception {
			Paragraph start = null;
			Paragraph end = null;
			NodeCollection<Paragraph> paragraphs = doc.getChildNodes(NodeType.PARAGRAPH, true);

			for (Paragraph para : paragraphs) {
				if (para.toString(SaveFormat.TEXT).startsWith("==START==")) {
					start = para;
					start.getRuns().get(0).setText("BODY_CONTENT");
				}
				if (para.toString(SaveFormat.TEXT).startsWith("==END==")) {
					end = para;
				}
			}
			if (start == null || end == null) {
				throw new Exception("");
			}
			removeMarkedBodyBetweenParagraph(doc, start, end);
	    }
	 
	 
	 private void removeMarkedBodyBetweenParagraph(Document doc, Paragraph start, Paragraph end) throws Exception {
	    	if (start != null && end != null)
			{
			    DocumentBuilder builder = new DocumentBuilder(doc);
			    builder.moveTo(start);

			    BookmarkStart bmStart = builder.startBookmark("bm");
			    BookmarkEnd bmEnd = builder.endBookmark("bm");

			    end.appendChild(bmEnd);

			    bmStart.getBookmark().setText("");
			    bmStart.getBookmark().remove();
			}
	    }
	 
	 private Document getMarkedBody(Document from) throws Exception {
	        Paragraph start = null;
			Paragraph end = null;
			NodeCollection<Paragraph> paragraphs = from.getChildNodes(NodeType.PARAGRAPH, true);

			for (Paragraph para : paragraphs) {
				if (para.toString(SaveFormat.TEXT).startsWith("==START==")) {
					start = para;
					//start.getRuns().get(0).setText("BODY_CONTENT");
				}
				if (para.toString(SaveFormat.TEXT).startsWith("==END==")) {
					end = para;
				}
			}
			if ( start == null || end == null)
			{
				throw new Exception("");
			}
			return asposeUtil.extractContentBetweenParagraphs(paragraphs.indexOf(start) + 1, paragraphs.indexOf(end) - 1, from);
	    }
	 
	 private String getBodyHtml(Document doc) throws Exception {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        doc.save(baos,SaveFormat.HTML);
	        return getBodyOnly(baos.toString());
	    }
		
		private String getBodyOnly(String html) throws IOException {
	        org.jsoup.nodes.Document doc = Jsoup.parse(html);
	        doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
	        doc.outputSettings().charset("UTF-8");
	        return doc.body().child(0).toString();
	    }
}
