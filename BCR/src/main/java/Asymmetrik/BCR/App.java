package Asymmetrik.BCR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {
	
	public static String fileToString(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		String str = "";
	    String line;
	    while ((line = br.readLine()) != null) {
	        str += line + ". ";
	    }
	    
	    br.close();
	    
		return str;
	}
	
    public static void main(String[] args) throws IOException {    
        String text = fileToString(args[0]);
     	ContactInfo info = BusinessCardParser.getContactInfo(text);
        
        System.out.println("Name: " + info.getName());
        System.out.println("Email: " + info.getEmail());
        System.out.println("Number: " + info.getNumber());
    }
}
