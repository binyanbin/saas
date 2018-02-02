package com.bzw.api.module.third.params;

/**
 * @author yanbin
 */
public class WcTemplateData {

    private Keyword keyword1;
    private Keyword keyword2;
    private Keyword keyword3;
    private Keyword keyword4;
    private static final String COLOR = "#173177";

    public WcTemplateData(){
        keyword1 = new Keyword();
        keyword2 = new Keyword();
        keyword3 = new Keyword();
        keyword4 = new Keyword();
    }

    public Keyword getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(Keyword keyword1) {
        this.keyword1 = keyword1;
    }

    public void setKeyword1(String value){
        this.keyword1.setValue(value);
        this.keyword1.setColor(COLOR);
    }

    public Keyword getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(Keyword keyword2) {
        this.keyword2 = keyword2;
    }

    public void setKeyword2(String value){
        this.keyword2.setValue(value);
        this.keyword2.setColor(COLOR);
    }


    public Keyword getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(Keyword keyword3) {
        this.keyword3 = keyword3;
    }

    public void setKeyword3(String value){
        this.keyword3.setValue(value);
        this.keyword3.setColor(COLOR);
    }

    public Keyword getKeyword4() {
        return keyword4;
    }

    public void setKeyword4(Keyword keyword4) {
        this.keyword4 = keyword4;
    }

    public void setKeyword4(String value){
        this.keyword4.setValue(value);
        this.keyword4.setColor(COLOR);
    }

    class Keyword {

        private String value;
        private String color;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
