package dalecom.com.br.agendamobileprofessional.helpers;

/**
 * Created by daniellessa on 12/05/16.
 */
public class Helper {

    public static String getFirstName(String fullName){
        String[] s = null;
        s = fullName.split(" ");
        return s[0];
    }
}
