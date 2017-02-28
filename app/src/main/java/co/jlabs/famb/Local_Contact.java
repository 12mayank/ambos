package co.jlabs.famb;

/**
 * Created by Jlabs-Win on 06/02/2017.
 */

public class Local_Contact {

    String number;
    String name;

    public Local_Contact(){


    }

   public Local_Contact(String number, String name){

        this.number = number;
       this.name = name;
    }


    public String getName(){

        return  name;
    }

    public String getNumber(){

        return number;
    }
}
