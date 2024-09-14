package auth.kayodeo1.com;

import java.sql.SQLException;

public class test {
    public static void main(String[] args) throws SQLException {


	    String email = System.getenv("APP_EMAIL");

	    System.out.println(email+System.getenv("DATABASE_PASSWORD"));

    }
//    public boolean isPilandrom(String word){
//        int i= 0 ;
//        int j = word.length()
//        while (i<word.length() && j > 0){
//                if (!(word.charAt(i).equals(word.charAt(j)))){
//
//                return false;
//                }
//        i++;
//        j--;
//    }
//    return true;
//
//    }

}
