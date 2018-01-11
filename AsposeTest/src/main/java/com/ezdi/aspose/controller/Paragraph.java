/**
 * 
 */
package com.ezdi.aspose.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.FindReplaceDirection;
import com.aspose.words.FindReplaceOptions;
import com.ezdi.aspose.Util;

/**
 * @author parth.m
 *
 */
@RestController
public class Paragraph {

	@GetMapping("/getSpectificPara")
	public void getSpectificPara() throws Exception{
		String dataDir = Util.getDataDir();

		Document doc = new Document(dataDir + "/htmlToDocx_output.docx");
		doc.getRange().replace("sad", "bad", new FindReplaceOptions(FindReplaceDirection.FORWARD));
	}
}
