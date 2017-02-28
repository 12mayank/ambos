package co.jlabs.famb;

import java.io.Serializable;

/**
 * Created by JLabs on 12/21/16.
 */

public class Models  implements Serializable{

    int id;
    public String text;
    public int pic;
    private boolean isSelected = false;
    int user_id;


    public Models(){


        text = new String();
        pic = new Integer("0");
       // user_id  =  new Integer("0");
    }

    public Models(String text, int pic, int user_id) {
        this.text = text;

        this.pic = pic;
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public int getPic() {
        return pic;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setId(int user_id){

        this.user_id = user_id;
    }

    public int getId(){

        return user_id;
    }
}
