/**
 * 
 */
package com.ezdi.aspose.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import com.aspose.words.BookmarkEnd;
import com.aspose.words.BookmarkStart;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.SaveFormat;
import com.ezdi.aspose.bean.DocxBodyMetaData;
import com.ezdi.common.util.document.AsposeUtil;

/**
 * @author parth.m
 *
 */
public class AdminCode {
	
	@Autowired
	AsposeUtil asposeUtil;


	//Override
    public String getBodyHtml(InputStream docxStream) throws Exception {
		Document doc = new Document(docxStream);
		Document blankDoc = getMarkedBody(doc);
		return getBodyHtml(blankDoc);
	}
    
  //Override
    public void removeMarkedBody(InputStream inStream, OutputStream outStream) throws Exception {
		Document doc = new Document(inStream);
		removeMarkedBody(doc);
		doc.save(outStream, SaveFormat.DOCX);
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
    
    // Override
    public DocxBodyMetaData getMetaData(InputStream inputStream) throws Exception {
    	DocxBodyMetaData metadata = new DocxBodyMetaData();
    	Document doc = new Document(inputStream);
    	Document blankDoc = getMarkedBody(doc);
    	removeMarkedBody(doc);
    	
    	//TODO : character counting
    	metadata.setBodyContent(getBodyHtml(blankDoc));
		return metadata;
    }
    
    private Document getMarkedBody(Document from) throws Exception {
        Paragraph start = null;
		Paragraph end = null;
		NodeCollection<Paragraph> paragraphs = from.getChildNodes(NodeType.PARAGRAPH, true);

		for (Paragraph para : paragraphs) {
			if (para.toString(SaveFormat.TEXT).startsWith("==START==")) {
				start = para;
				start.getRuns().get(0).setText("BODY_CONTENT");
			}
			if (para.toString(SaveFormat.TEXT).startsWith("==END==")) {
				end = para;
			}
		}
		if ( start == null || end == null)
		{
			throw new Exception("");
		}
		return asposeUtil.extractContentBetweenParagraphs(paragraphs.indexOf(start), paragraphs.indexOf(end) - 1, from);
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
