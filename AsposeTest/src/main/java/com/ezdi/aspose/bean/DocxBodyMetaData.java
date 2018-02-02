package com.ezdi.aspose.bean;

/**
 * Created by neeraj.p on 7/12/2017
 */

public class DocxBodyMetaData {

    private String bodyContent;
    private CharacterCounts charCountWithoutSpace;
    private CharacterCounts charCountWithSpace;

    public DocxBodyMetaData() {
        bodyContent = new String();
        charCountWithoutSpace = new CharacterCounts();
        charCountWithSpace = new CharacterCounts();
    }

    public DocxBodyMetaData(String bodyContent, CharacterCounts charCountWithoutSpace, CharacterCounts charCountWithSpace) {
        this.bodyContent = bodyContent;
        this.charCountWithoutSpace = charCountWithoutSpace;
        this.charCountWithSpace = charCountWithSpace;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public CharacterCounts getCharCountWithoutSpace() {
        return charCountWithoutSpace;
    }

    public void setCharCountWithoutSpace(CharacterCounts charCountWithoutSpace) {
        this.charCountWithoutSpace = charCountWithoutSpace;
    }

    public CharacterCounts getCharCountWithSpace() {
        return charCountWithSpace;
    }

    public void setCharCountWithSpace(CharacterCounts charCountWithSpace) {
        this.charCountWithSpace = charCountWithSpace;
    }

    @Override
    public String toString() {
        return "DocxBodyMetaData{" +
                "bodyContent=" + bodyContent +
                ", charCountWithoutSpace=" + charCountWithoutSpace +
                ", charCountWithSpace=" + charCountWithSpace +
                '}';
    }
}
