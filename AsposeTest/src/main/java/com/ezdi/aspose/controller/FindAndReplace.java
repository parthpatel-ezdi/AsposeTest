/**
 * 
 */
package com.ezdi.aspose.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.FindReplaceDirection;
import com.aspose.words.FindReplaceOptions;
import com.ezdi.aspose.Util;
import com.ezdi.aspose.bean.Person;
import com.samskivert.mustache.Mustache;

/**
 * @author parth.m
 *
 */
@RestController
public class FindAndReplace {

	@GetMapping("/findAndReplace")
	public void findAndReplace(@RequestParam("filename") String filename) throws Exception{
		String fileName = Util.getDataDir() + filename;
		Document doc = new Document(fileName);
		List<Person> list = new ArrayList<Person>();
		list.add(new Person("Keval", "Prajapati"));
		list.add(new Person("Hardik", null));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cc_physicians", list);
		String parsedData = Mustache.compiler().defaultValue("      ").compile("{{#cc_physicians}}{{^-first}}\t{{/-first}}{{this.firstName}} {{this.lastName}}\n{{/cc_physicians}}").execute(map);
		System.out.println(parsedData);
		doc.getRange().replace("bad", parsedData, new FindReplaceOptions(FindReplaceDirection.FORWARD));
		doc.save(fileName);
	}
}
