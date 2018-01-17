/**
 * 
 */
package com.ezdi.aspose.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.DocumentProperty;
import com.ezdi.aspose.Util;

/**
 * @author parth.m
 *
 */
@RestController
public class PropertiesController {
	
	@GetMapping("/getAllProperties")
	public void getAllPropeties(@RequestParam("filename") String filename) throws Exception{
		
		String fileName = Util.getDataDir() + filename;
		
		Document doc = new Document(fileName);

		System.out.println("1. Document name: " + fileName);

		System.out.println("2. Built-in Properties");
		for (DocumentProperty prop : doc.getBuiltInDocumentProperties())
		    System.out.println(prop.getName() + " : " + prop.getValue());

		System.out.println("3. Custom Properties");
		for (DocumentProperty prop : doc.getCustomDocumentProperties())
		    System.out.println(prop.getName() + " : " + prop.getValue());
	}

}
