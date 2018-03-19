import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Main {
	

	
	public static void main(String[] args)  throws ParserConfigurationException{
		
		
		Database d = new Database();
		d.makeList();
		StringWriter sw = new StringWriter();
	
	
	try {
		JAXBContext jaxbContext = JAXBContext.newInstance(Database.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(d, new File("prayer.xml"));
		//String xmlString = sw.toString();
		//System.out.println(xmlString);
		
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		

	}

}
