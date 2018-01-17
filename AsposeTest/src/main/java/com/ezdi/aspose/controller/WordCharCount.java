/**
 * 
 */
package com.ezdi.aspose.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.ezdi.aspose.Util;

/**
 * @author parth.m
 *
 */
@RestController
public class WordCharCount {

	/**
	 * There are three types of headers/footers in a Word document 
	 * i.e. HeaderFirst/FooterFirst (displayed only on the first page of the section), 
	 * HeaderPrimary/FooterPrimary (displayed on other pages of the document) and 
	 * HeaderEven/FooterEven (displayed on even pages).
	 */
	
	@GetMapping("charCount")
	public void charCount(@RequestParam("filename") String filename) throws Exception{
		String fileName = Util.getDataDir() + filename;
		
		Document doc = new Document(fileName);
		
		System.out.println(doc.getText());
		
		for (int i = 0; i < doc.getSections().getCount(); i++) {
		    Section sect = doc.getSections().get(i);
		    System.out.println("Section:" + i + " HEADER_FIRST words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.HEADER_FIRST));
		    System.out.println("Section:" + i + " HEADER_PRIMARY words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.HEADER_PRIMARY));
		    System.out.println("Section:" + i + " HEADER_EVEN words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.HEADER_EVEN));
		    System.out.println("Section:" + i + " FOOTER_FIRST words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.FOOTER_FIRST));
		    System.out.println("Section:" + i + " FOOTER_PRIMARY words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.FOOTER_PRIMARY));
		    System.out.println("Section:" + i + " FOOTER_EVEN words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.FOOTER_EVEN));
		}
		
	}
	
	int getWordsCountHeaderFooter(Section sect, int type) throws Exception {
	    HeaderFooter hf = sect.getHeadersFooters().getByHeaderFooterType(type);
	    if (hf == null) {
	        return -1;
	    }

	    String text = hf.toString(SaveFormat.TEXT);
	    return text.length();
	}
}
