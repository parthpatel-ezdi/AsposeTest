package com.ezdi.aspose.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Run;
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
		//methodTwo(doc, paragraphs);
		methodThree(paragraphs);
		doc.save(dataDir + "output.docx");
		
	}

	private void methodThree(NodeCollection<Paragraph> paragraphs) throws Exception {
		for (Paragraph para : paragraphs) {
		    if (para.toString(SaveFormat.TEXT).startsWith("MED_BODY_CONTENT")) {
		        if(para.getRuns().getCount() > 0)
		        {
		            Run run = (Run)para.getRuns().get(0).deepClone(true);
		            run.setText("==START==");
		            para.removeAllChildren();
		            para.getRuns().add(run);

		            Paragraph para2 =  (Paragraph) para.deepClone(true);
		            para2.getRuns().get(0).setText("INSERT_HTML");
		            para2 = (Paragraph) para.getParentNode().insertAfter(para2, para);

		            Paragraph para3 =  (Paragraph) para.deepClone(true);
		            para3.getRuns().get(0).setText("==END==");

		            para2.getParentNode().insertAfter(para3, para2);
		            break;
		        }
		    }
		}
	}

	private void methodTwo(Document doc, NodeCollection<Paragraph> paragraphs) throws Exception {
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
