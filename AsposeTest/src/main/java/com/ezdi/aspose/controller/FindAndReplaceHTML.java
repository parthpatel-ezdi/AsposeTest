/**
 * 
 */
package com.ezdi.aspose.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.IReplacingCallback;
import com.aspose.words.Node;
import com.aspose.words.ReplaceAction;
import com.aspose.words.ReplacingArgs;
import com.ezdi.aspose.Util;

/**
 * @author parth.m
 *
 */
@RestController
public class FindAndReplaceHTML {
	
	@GetMapping("FindAndReplaceHTML")
	public void findAndReplaceHTML() throws Exception{
		 // ExStart:ReplaceHtmlTextWithMetaCharacters
        // The path to the documents directory.
        String dataDir = Util.getDataDir();
        String html = "<p>&ldquo;Some Text&rdquo;</p>";

        // Initialize a Document.
        Document doc = new Document();

        // Use a document builder to add content to the document.
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.write("{PLACEHOLDER}");

        FindReplaceOptions findReplaceOptions = new FindReplaceOptions();
        findReplaceOptions.setReplacingCallback(new FindAndInsertHtml());
        findReplaceOptions.setPreserveMetaCharacters(true);

        doc.getRange().replace("{PLACEHOLDER}", html, findReplaceOptions);

        dataDir = dataDir + "ReplaceHtmlTextWithMetaCharacters_out.doc";
        doc.save(dataDir);
        // ExEnd:ReplaceHtmlTextWithMetaCharacters
        System.out.println("\nText replaced with meta characters successfully.\nFile saved at " + dataDir);
    }

    // ExStart:ReplaceHtmlFindAndInsertHtml
    static class FindAndInsertHtml implements IReplacingCallback {
        public int replacing(ReplacingArgs e) throws Exception {
            // This is a Run node that contains either the beginning or the complete match.
            Node currentNode = e.getMatchNode();
            // create Document Buidler and insert MergeField
            DocumentBuilder builder = new DocumentBuilder((Document) e.getMatchNode().getDocument());
            builder.moveTo(currentNode);
            builder.insertHtml(e.getReplacement());
            currentNode.remove();
            //Signal to the replace engine to do nothing because we have already done all what we wanted.
            return ReplaceAction.SKIP;
        }
	}

}
