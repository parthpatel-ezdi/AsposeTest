package com.ezdi.aspose.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.SaveFormat;
import com.ezdi.aspose.Util;

@RestController
public class TemplateDownloadController {

	@GetMapping("/downloadTemplate")
	public void downloadTemplate() throws Exception {
		String dataDir = Util.getDataDir();

		Document doc = new Document(dataDir + "/source.docx");
		
		Paragraph start = null;
		
		NodeCollection<Paragraph> paragraphs = doc.getChildNodes(NodeType.PARAGRAPH, true);

		//methodOne(doc, start, paragraphs);
		methodTwo(doc, start, paragraphs);
		doc.save(dataDir + "output.docx");
		
	}

	private void methodTwo(Document doc, Paragraph start, NodeCollection<Paragraph> paragraphs) throws Exception {
		for (Paragraph para : paragraphs) {
		    if (para.toString(SaveFormat.TEXT).startsWith("MED_BODY_CONTENT")) {
		        DocumentBuilder builder = new DocumentBuilder(doc);
		        builder.moveTo(para);
		        builder.writeln();
		        builder.writeln("==START==");
		        builder.writeln("INSERT_HTML");
		        builder.write("==END==");
		        para.remove();
		        break;
		    }
		}
	}

	private void methodOne(Document doc, Paragraph start, NodeCollection<Paragraph> paragraphs) throws Exception {
		for (Paragraph para : paragraphs) {
			if (para.toString(SaveFormat.TEXT).startsWith("MED_BODY_CONTENT")) {
				start = para;
				start.getRuns().get(0).setText("==START==");
			}
		}
		
		DocumentBuilder builder = new DocumentBuilder(doc);
		builder.moveToParagraph(paragraphs.indexOf(start) , -1);
		builder.writeln();
		builder.writeln("INSERT_HTML");
		builder.writeln("==END==");
	}
}
