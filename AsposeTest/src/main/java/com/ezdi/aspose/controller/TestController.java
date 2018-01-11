/**
 * 
 */
package com.ezdi.aspose.controller;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.IReplacingCallback;
import com.aspose.words.License;
import com.aspose.words.Node;
import com.aspose.words.ReplaceAction;
import com.aspose.words.ReplacingArgs;
import com.aspose.words.SaveFormat;

/**
 * @author parth.m
 *
 */

@RestController
public class TestController {
	
	@Autowired
	License license;

	@GetMapping("createDocx")
	public void createDocx() throws Exception{
		String dataDir = getDataDir();

        // Load the document.
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.write("hello world");
        doc.save(dataDir + "output.docx");
		//ExEnd:CreateDocument

        System.out.println("Document created successfully.");
	}
	
	@GetMapping("test")
	public void test() throws Exception{
		
		if (license.isLicensed()) {
		    System.out.println("License is Set!");
		}
		
		Document doc = new Document("/home/local/EZDI/parth.m/Desktop/in.html");
		doc.save("/home/local/EZDI/parth.m/Desktop/Out.docx", SaveFormat.DOCX);
	}
	
	public static String getDataDir() {
        File dir = new File("/home/local/EZDI/parth.m/Desktop/AsposeTest");
            if (dir.isDirectory() == false)
                dir.mkdir();
        System.out.println("Using data directory: " + dir.toString());
        return dir.toString() + File.separator;
    }
	
	@GetMapping("createDocxFromHtml")
	public void createDocxFromHtml() throws Exception{

		Document doc = new Document();
		 DocumentBuilder builder = new DocumentBuilder(doc);
		 builder.insertHtml(new String(Files.readAllBytes(Paths.get("/home/local/EZDI/parth.m/Desktop/in.html"))));
		doc.save("/home/local/EZDI/parth.m/Desktop/Out.docx", SaveFormat.DOCX);
	}
}
