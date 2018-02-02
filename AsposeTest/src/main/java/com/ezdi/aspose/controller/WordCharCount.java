/**
 *
 */
package com.ezdi.aspose.controller;

import com.aspose.words.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezdi.aspose.Util;

import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author parth.m
 */
@RestController
public class WordCharCount
{

    /**
     * There are three types of headers/footers in a Word document
     * i.e. HeaderFirst/FooterFirst (displayed only on the first page of the section),
     * HeaderPrimary/FooterPrimary (displayed on other pages of the document) and
     * HeaderEven/FooterEven (displayed on even pages).
     */

    enum Type
    {
        WithSpace, WithoutSpace
    }

    ;

    @GetMapping("charCount")
    public void charCount(@RequestParam("filename") String filename) throws Exception
    {
        String fileName = Util.getDataDir() + filename;

        Type type = Type.WithoutSpace;
        Document doc = new Document(fileName);

        //To Remove Section Break from Document
        removeSectionBreaks(doc);

        //To remove field code from document
        UnlinkFields(doc);

        System.out.println(doc.getText());

        System.out.println("Character Count : " + doc.getBuiltInDocumentProperties().getCharacters());
        for (int i = 0; i < doc.getSections().getCount(); i++)
        {
            Section sect = doc.getSections().get(i);

            System.out.println("Character Count without Space");

            getWordCountBody(sect, Type.WithoutSpace);
            getWordsCountHeaderFooter(sect, Type.WithoutSpace);

            System.out.println("Character Count with Space");
            getWordCountBody(sect, Type.WithSpace);
            getWordsCountHeaderFooter(sect, Type.WithSpace);

/*
            System.out.println("Section:" + i + " HEADER_FIRST words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.HEADER_FIRST));
            System.out.println("Section:" + i + " HEADER_PRIMARY words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.HEADER_PRIMARY));
            System.out.println("Section:" + i + " HEADER_EVEN words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.HEADER_EVEN));
            System.out.println("Section:" + i + " FOOTER_FIRST words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.FOOTER_FIRST));
            System.out.println("Section:" + i + " FOOTER_PRIMARY words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.FOOTER_PRIMARY));
            System.out.println("Section:" + i + " FOOTER_EVEN words count:" + getWordsCountHeaderFooter(sect, HeaderFooterType.FOOTER_EVEN));*/
        }

    }

    int getWordCountBody(Section section, Type type) throws Exception
    {
        int count = 0;
        String text = section.getBody().getText();

        if (type == Type.WithoutSpace)
        {
            count = getCharacterCountWithoutSpace(text);
        }
        else
        {
            count = getCharacterCountWithSpace(text);
        }
        System.out.println(String.format("%s ==> %d", text, count));
        return count;
    }

    int getWordsCountHeaderFooter(Section section, Type type) throws Exception
    {
        int count = 0;
        String text = "";

        HeaderFooterCollection headerFooterCollection = section.getHeadersFooters();

        for (HeaderFooter hf : headerFooterCollection)
        {
            if (type == Type.WithoutSpace)
            {
                text = hf.getText();
                count += getCharacterCountWithoutSpace(text);
            }
            else
            {
                text = hf.getText();
                count += getCharacterCountWithSpace(text);
            }
            System.out.println(String.format("%s ==> %d", text, count));
        }
        return count;
    }

    int getCharacterCountWithSpace(String text)
    {
        /*text = text.replaceAll("[\\n\\f\\r]","");
        return text.length();*/

        String str = "";
        int counter = 0;
        for (char c : text.toCharArray())
        {
            if (c == ' ' || c == '\t')
            {
                str += c;
                counter++;
            }
            else if (isAsciiPrintable(c))
            {
                str += c;
                counter++;
            }
        }
        return counter++;
    }

    int getCharacterCountWithoutSpace(String text)
    {
        /*text = text.replaceAll("[\\n\\t\\f\\r ]", "");
        return text.length();
*/
        String str = "";
        int counter = 0;
        for (char c : text.toCharArray())
        {
            if (isAsciiPrintable(c))
            {
                str += c;
                counter++;
            }
        }
        return counter++;
    }


    boolean isAsciiPrintable(char ch)
    {
        return ch > 32 && ch < 127;
    }

    void removeSectionBreaks(Document doc) throws Exception
    {

        while (doc.getSections().getCount() > 1)
        {
            doc.getFirstSection().appendContent((Section) doc.getFirstSection().getNextSibling());
            doc.getFirstSection().getNextSibling().remove();
        }
    }

    void UnlinkFields(Document doc)
    {
        //Get collection of FieldStart nodes
        NodeCollection<FieldStart> fieldStarts = doc.getChildNodes(NodeType.FIELD_START, true);
        //Get collection of FieldSeparator nodes
        NodeCollection fieldSeparators = doc.getChildNodes(NodeType.FIELD_SEPARATOR, true);
        //And get collection of FieldEnd nodes
        NodeCollection fieldEnds = doc.getChildNodes(NodeType.FIELD_END, true);

        //Loop through all FieldStart nodes
        for (FieldStart start : fieldStarts)
        {
            //Search for FieldSeparator node. it is needed to remove field code from the document
            Node curNode = start;
            while (curNode.getNodeType() != NodeType.FIELD_SEPARATOR && curNode.getNodeType() != NodeType.FIELD_END)
            {
                curNode = curNode.nextPreOrder(doc);
                if (curNode == null)
                    break;
            }

            //Remove all nodes between Fieldstart and FieldSeparator (of FieldEnd, depending from field type)
            if (curNode != null)
            {
                RemoveSequence(start, curNode);
            }
        }

        //Now we can remove FieldStart, FieldSeparator and FieldEnd nodes
        fieldStarts.clear();
        fieldSeparators.clear();
        fieldEnds.clear();
    }

    void RemoveSequence(Node start, Node end)
    {
        Node curNode = start.nextPreOrder(start.getDocument());
        while (curNode != null && !curNode.equals(end))
        {
            //Move to next node
            Node nextNode = curNode.nextPreOrder(start.getDocument());
            //Check whether current contains end node
            if (curNode.isComposite())
            {
                if (!((CompositeNode) curNode).getChildNodes(NodeType.ANY, true).contains(end) &&
                        !((CompositeNode) curNode).getChildNodes(NodeType.ANY, true).contains(start))
                {
                    nextNode = curNode.getNextSibling();
                    curNode.remove();
                }
            }
            else
            {
                curNode.remove();
            }
            curNode = nextNode;
        }
    }

}
