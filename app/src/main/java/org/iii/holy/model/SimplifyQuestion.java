package org.iii.holy.model;

/**
 * Created by Holy on 2016/5/26.
 */
public class SimplifyQuestion {
    private String mTitle=null;
    private String mContent=null;
    private String mTag=null;
    public String getTitle() {
        return mTitle;
    }
    public String getContent() {
        return mContent;
    }
    public String getTag(){return mTag;}
    public void setTitle(String Title) {
        this.mTitle = Title;
    }
    public void setContent(String Content) { this.mContent = Content;}
    public void setTag(String Tag){this.mTag=Tag;}
}
