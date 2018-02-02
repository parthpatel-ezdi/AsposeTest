package com.ezdi.aspose.bean;

/**
 * Created by neeraj.p on 7/12/2017.
 */
public class CharacterCounts {
    private Integer template;
    private Integer buffer;
    private Integer footer;
    private Integer header;

    public CharacterCounts() {
    }

    public CharacterCounts(Integer template, Integer buffer, Integer footer, Integer header) {
        this.template = template;
        this.buffer = buffer;
        this.footer = footer;
        this.header = header;
    }

    public Integer getTemplate() {
        return template;
    }

    public void setTemplate(Integer template) {
        this.template = template;
    }

    public Integer getBuffer() {
        return buffer;
    }

    public void setBuffer(Integer buffer) {
        this.buffer = buffer;
    }

    public Integer getFooter() {
        return footer;
    }

    public void setFooter(Integer footer) {
        this.footer = footer;
    }

    public Integer getHeader() {
        return header;
    }

    public void setHeader(Integer header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "CharacterCounts{" +
                "template=" + template +
                ", buffer=" + buffer +
                ", footer=" + footer +
                ", header=" + header +
                '}';
    }
}
