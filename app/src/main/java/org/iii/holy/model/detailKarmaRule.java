package org.iii.holy.model;

/**
 * Created by Holy on 2016/6/14.
 */
public class detailKarmaRule {

    private int create_flag;
    private int vote_down;
    private int create_comment;
    private int moderate_edits;
    private int edit_question;
    private int vote_up;
    private int answer_own_question;
    private int inactivate_question;
    private int edit_answer;

    public detailKarmaRule() {

        create_flag = 10;
        vote_down = 100;
        create_comment = 0;
        moderate_edits = 2000;
        edit_question = 20;
        vote_up = 10;
        answer_own_question = 20;
        inactivate_question = 100;
        edit_answer = 20;

    }

    public int getCreate_flag() {
        return create_flag;
    }

    public int getVote_down() {
        return vote_down;
    }

    public int getCreate_comment() {
        return create_comment;
    }


    public int getModerate_edits() {
        return moderate_edits;
    }


    public int getEdit_question() {
        return edit_question;
    }


    public int getVote_up() {
        return vote_up;
    }


    public int getAnswer_own_question() {
        return answer_own_question;
    }


    public int getInactivate_question() {
        return inactivate_question;
    }


    public int getEdit_answer() {
        return edit_answer;
    }

}
