import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Main {
	

	
	public static void main(String[] args)  throws ParserConfigurationException{
		
		
		Database d = new Database();
		d.makeList();
	
	
	try {
		XMLEncoder x = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("FirstData.xml")));
		x.writeObject(d);
		x.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		

	}

}
