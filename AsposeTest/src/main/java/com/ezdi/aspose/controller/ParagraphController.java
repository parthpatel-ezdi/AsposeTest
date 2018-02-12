/**
 * 
 */
package com.ezdi.aspose.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.words.BookmarkEnd;
import com.aspose.words.BookmarkStart;
import com.aspose.words.CompositeNode;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeImporter;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.TxtSaveOptions;
import com.ezdi.aspose.Util;

/**
 * @author parth.m
 *
 */
@RestController
public class ParagraphController {
	
	@GetMapping("/adminExtract")
	public void getadminExtract() throws Exception {
		String dataDir = Util.getDataDir();

		Document doc = new Document(dataDir + "/source.docx");

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
		
		if (start != null && end != null)
		{
			
			ExtractContentBetweenParagraphs(paragraphs.indexOf(start), paragraphs.indexOf(end), doc);
		}
		
		
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
		doc.save(dataDir + "/source1.docx", SaveFormat.DOCX);
	}
	
	private void ExtractContentBetweenParagraphs(int start, int end, Document doc) throws Exception{
		 Paragraph startPara = (Paragraph) doc.getFirstSection().getChild(NodeType.PARAGRAPH, start, true);
	        Paragraph endPara = (Paragraph) doc.getFirstSection().getChild(NodeType.PARAGRAPH, end - 1, true);
	        ArrayList<Node> extractedNodes = extractContent(startPara, endPara, true);
	        Document dstDoc = generateDocument(doc, extractedNodes);

	        dstDoc.save(Util.getDataDir() + "output.html",SaveFormat.HTML);
	        System.out.println("Content extracted between the paragraphs successfully.");
	}
	
	 /**
     * Extracts a range of nodes from a document found between specified markers and returns a copy of those nodes. Content can be extracted
     * between inline nodes, block level nodes, and also special nodes such as Comment or Boomarks. Any combination of different marker types can used.
     *
     * @param startNode   The node which defines where to start the extraction from the document. This node can be block or inline level of a body.
     * @param endNode     The node which defines where to stop the extraction from the document. This node can be block or inline level of body.
     * @param isInclusive Should the marker nodes be included.
     */

    public static ArrayList extractContent(Node startNode, Node endNode, boolean isInclusive) throws Exception {
        // First check that the nodes passed to this method are valid for use.
        verifyParameterNodes(startNode, endNode);

        // Create a list to store the extracted nodes.
        ArrayList nodes = new ArrayList();

        // Keep a record of the original nodes passed to this method so we can split marker nodes if needed.
        Node originalStartNode = startNode;
        Node originalEndNode = endNode;

        // Extract content based on block level nodes (paragraphs and tables). Traverse through parent nodes to find them.
        // We will split the content of first and last nodes depending if the marker nodes are inline
        while (startNode.getParentNode().getNodeType() != NodeType.BODY)
            startNode = startNode.getParentNode();

        while (endNode.getParentNode().getNodeType() != NodeType.BODY)
            endNode = endNode.getParentNode();

        boolean isExtracting = true;
        boolean isStartingNode = true;
        boolean isEndingNode;
        // The current node we are extracting from the document.
        Node currNode = startNode;

        // Begin extracting content. Process all block level nodes and specifically split the first and last nodes when needed so paragraph formatting is retained.
        // Method is little more complex than a regular extractor as we need to factor in extracting using inline nodes, fields, bookmarks etc as to make it really useful.
        while (isExtracting) {
            // Clone the current node and its children to obtain a copy.
            CompositeNode cloneNode = (CompositeNode) currNode.deepClone(true);
            isEndingNode = currNode.equals(endNode);

            if (isStartingNode || isEndingNode) {
                // We need to process each marker separately so pass it off to a separate method instead.
                if (isStartingNode) {
                    processMarker(cloneNode, nodes, originalStartNode, isInclusive, isStartingNode, isEndingNode);
                    isStartingNode = false;
                }

                // Conditional needs to be separate as the block level start and end markers maybe the same node.
                if (isEndingNode) {
                    processMarker(cloneNode, nodes, originalEndNode, isInclusive, isStartingNode, isEndingNode);
                    isExtracting = false;
                }
            } else
                // Node is not a start or end marker, simply add the copy to the list.
                nodes.add(cloneNode);

            // Move to the next node and extract it. If next node is null that means the rest of the content is found in a different section.
            if (currNode.getNextSibling() == null && isExtracting) {
                // Move to the next section.
                Section nextSection = (Section) currNode.getAncestor(NodeType.SECTION).getNextSibling();
                currNode = nextSection.getBody().getFirstChild();
            } else {
                // Move to the next node in the body.
                currNode = currNode.getNextSibling();
            }
        }

        // Return the nodes between the node markers.
        return nodes;
    }

    /**
     * Checks the input parameters are correct and can be used. Throws an exception if there is any problem.
     */

    private static void verifyParameterNodes(Node startNode, Node endNode) throws Exception {
        // The order in which these checks are done is important.
        if (startNode == null)
            throw new IllegalArgumentException("Start node cannot be null");
        if (endNode == null)
            throw new IllegalArgumentException("End node cannot be null");

        if (!startNode.getDocument().equals(endNode.getDocument()))
            throw new IllegalArgumentException("Start node and end node must belong to the same document");

        if (startNode.getAncestor(NodeType.BODY) == null || endNode.getAncestor(NodeType.BODY) == null)
            throw new IllegalArgumentException("Start node and end node must be a child or descendant of a body");

        // Check the end node is after the start node in the DOM tree
        // First check if they are in different sections, then if they're not check their position in the body of the same section they are in.
        Section startSection = (Section) startNode.getAncestor(NodeType.SECTION);
        Section endSection = (Section) endNode.getAncestor(NodeType.SECTION);

        int startIndex = startSection.getParentNode().indexOf(startSection);
        int endIndex = endSection.getParentNode().indexOf(endSection);

        if (startIndex == endIndex) {
            if (startSection.getBody().indexOf(startNode) > endSection.getBody().indexOf(endNode))
                throw new IllegalArgumentException("The end node must be after the start node in the body");
        } else if (startIndex > endIndex)
            throw new IllegalArgumentException("The section of end node must be after the section start node");
    }

    /**
     * Checks if a node passed is an inline node.
     */
    private static boolean isInline(Node node) throws Exception {
        // Test if the node is desendant of a Paragraph or Table node and also is not a paragraph or a table a paragraph inside a comment class which is decesant of a pararaph is possible.
        return ((node.getAncestor(NodeType.PARAGRAPH) != null || node.getAncestor(NodeType.TABLE) != null) && !(node.getNodeType() == NodeType.PARAGRAPH || node.getNodeType() == NodeType.TABLE));
    }

    /**
     * Removes the content before or after the marker in the cloned node depending on the type of marker.
     */
    private static void processMarker(CompositeNode cloneNode, ArrayList nodes, Node node, boolean isInclusive, boolean isStartMarker, boolean isEndMarker) throws Exception {
        // If we are dealing with a block level node just see if it should be included and add it to the list.
        if (!isInline(node)) {
            // Don't add the node twice if the markers are the same node
            if (!(isStartMarker && isEndMarker)) {
                if (isInclusive)
                    nodes.add(cloneNode);
            }
            return;
        }

        // If a marker is a FieldStart node check if it's to be included or not.
        // We assume for simplicity that the FieldStart and FieldEnd appear in the same paragraph.
        if (node.getNodeType() == NodeType.FIELD_START) {
            // If the marker is a start node and is not be included then skip to the end of the field.
            // If the marker is an end node and it is to be included then move to the end field so the field will not be removed.
            if ((isStartMarker && !isInclusive) || (!isStartMarker && isInclusive)) {
                while (node.getNextSibling() != null && node.getNodeType() != NodeType.FIELD_END)
                    node = node.getNextSibling();

            }
        }

        // If either marker is part of a comment then to include the comment itself we need to move the pointer forward to the Comment
        // node found after the CommentRangeEnd node.
        if (node.getNodeType() == NodeType.COMMENT_RANGE_END) {
            while (node.getNextSibling() != null && node.getNodeType() != NodeType.COMMENT)
                node = node.getNextSibling();

        }

        // Find the corresponding node in our cloned node by index and return it.
        // If the start and end node are the same some child nodes might already have been removed. Subtract the
        // difference to get the right index.
        int indexDiff = node.getParentNode().getChildNodes().getCount() - cloneNode.getChildNodes().getCount();

        // Child node count identical.
        if (indexDiff == 0)
            node = cloneNode.getChildNodes().get(node.getParentNode().indexOf(node));
        else
            node = cloneNode.getChildNodes().get(node.getParentNode().indexOf(node) - indexDiff);

        // Remove the nodes up to/from the marker.
        boolean isSkip;
        boolean isProcessing = true;
        boolean isRemoving = isStartMarker;
        Node nextNode = cloneNode.getFirstChild();

        while (isProcessing && nextNode != null) {
            Node currentNode = nextNode;
            isSkip = false;

            if (currentNode.equals(node)) {
                if (isStartMarker) {
                    isProcessing = false;
                    if (isInclusive)
                        isRemoving = false;
                } else {
                    isRemoving = true;
                    if (isInclusive)
                        isSkip = true;
                }
            }

            nextNode = nextNode.getNextSibling();
            if (isRemoving && !isSkip)
                currentNode.remove();
        }

        // After processing the composite node may become empty. If it has don't include it.
        if (!(isStartMarker && isEndMarker)) {
            if (cloneNode.hasChildNodes())
                nodes.add(cloneNode);
        }
    }

    public static Document generateDocument(Document srcDoc, ArrayList nodes) throws Exception {

        // Create a blank document.
        Document dstDoc = new Document();
        // Remove the first paragraph from the empty document.
        dstDoc.getFirstSection().getBody().removeAllChildren();

        // Import each node from the list into the new document. Keep the original formatting of the node.
        NodeImporter importer = new NodeImporter(srcDoc, dstDoc, ImportFormatMode.KEEP_SOURCE_FORMATTING);

        for (Node node : (Iterable<Node>) nodes) {
            Node importNode = importer.importNode(node, true);
            dstDoc.getFirstSection().getBody().appendChild(importNode);
        }

        // Return the generated document.
        return dstDoc;
    }
}
