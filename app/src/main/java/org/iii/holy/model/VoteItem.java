package org.iii.holy.model;

/**
 * Created by Holy on 2016/6/14.
 */
public class VoteItem {
    private String onWhat = null;
    private long onWhatId = 0;

    public String getOnWhat() {
        return onWhat;
    }
    public VoteItem(){};
    public VoteItem(boolean bQuestion,long AID){
        setOnWhat(bQuestion);
        setOnWhatId(AID);
    };

    public void setOnWhat(boolean bQuestion) {
        if (bQuestion)
            this.onWhat = "question";
        else
            this.onWhat = "answer";
    }

    public long getOnWhatId() {
        return onWhatId;
    }

    public void setOnWhatId(long AID) {
        this.onWhatId = AID;
    }
}
