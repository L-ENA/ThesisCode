import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TrialObject {
	protected static int counter = 1;
	protected String mainAuthor; //check
	protected int year;//check
	protected String doi;
	protected int crgID;
	protected String revManID;//check
	protected String[] references;
	//protected RANDOMIZATIONTYPE randomisation:
	//protected ALLOCATIONTYPE allocation;
	//protected BLINDINGTYPE blinding;
	//protected ATTRITIONTYPE attrition;
	//protected REPORTINGTYPE reporting;
	protected int nrOfParticipants;
	protected String interventionGroup;
	protected String controlGroup;

	protected int nrIntervention;
	protected int nrControl;
	//protected SETTING setting;
	//protected outcomeList : OutcomeObjects
	
	public TrialObject(Document review, int studyNumber){
					
		//Traverses XML until the desired study is found
					NodeList rootList = review.getElementsByTagName("COCHRANE_REVIEW");
					Node rootNode = rootList.item(0);
					Element rootElement = (Element) rootNode;
					
					NodeList characteristicsOfStudiesList = rootElement.getElementsByTagName("CHARACTERISTICS_OF_STUDIES");
					Node characteristicsOfStudiesNode = characteristicsOfStudiesList.item(0);
					Element characteristicsOfStudiesElement = (Element) characteristicsOfStudiesNode;
					
					NodeList characteristicsOfIncludedStudiesList = characteristicsOfStudiesElement.getElementsByTagName("CHARACTERISTICS_OF_INCLUDED_STUDIES");
					Node characteristicsOfIncludedStudiesNode = characteristicsOfIncludedStudiesList.item(0);
					Element characteristicsOfIncludedStudiesElement = (Element) characteristicsOfIncludedStudiesNode;
					
					NodeList includedStudiesList = characteristicsOfIncludedStudiesElement.getElementsByTagName("INCLUDED_CHAR");
					Node studyToExtractNode = includedStudiesList.item(studyNumber);
					Element studyToExtractElement = (Element) studyToExtractNode;
					
					//Extracts information into variables
					revManID = studyToExtractElement.getAttribute("STUDY_ID"); //Gets revman ID for study
					String cache = revManID.replaceAll("STD-", "");
					String[] cacheArray = cache.split("-");
					mainAuthor = cacheArray[0]; //Taken name of author from ID
					
					
					NodeList studiesAndReferencesList = rootElement.getElementsByTagName("STUDIES_AND_REFERENCES");
					Node studiesAndReferencesNode = studiesAndReferencesList.item(0);
					Element studiesAndReferencesElement = (Element) studiesAndReferencesNode;
					
					NodeList studiesList = studiesAndReferencesElement.getElementsByTagName("STUDIES");
					Node studiesNode = studiesList.item(0);
					Element studiesElement = (Element) studiesNode;
					
					
					NodeList includedStudiesBList = studiesElement.getElementsByTagName("INCLUDED_STUDIES");
					Node includedStudiesNode = includedStudiesBList.item(0);
					Element includedStudiesElement = (Element) includedStudiesNode;
					
					
					NodeList studyList = includedStudiesElement.getElementsByTagName("STUDY");
					Node studyNode = studyList.item(studyNumber);
					Element studyElement = (Element) studyNode;
					
					try {
						cache = studyElement.getAttribute("YEAR");
						year = Integer.parseInt(cache); //converts String to int
					} catch (NumberFormatException e8) {
						// if year can't be extracted as attribute it is tried via revman-ID
						cache = revManID.replaceAll("[^\\d.]", "");
						year = Integer.parseInt(cache); //Takes year of publication from ID
					}
					
					
					
					System.out.println("Year of publication: " + year + " Count of included studies: " + counter);
					counter++;
					System.out.println("Main author of Study: " + mainAuthor);
					System.out.println("RevMan ID: "+ revManID);
					
					//This for-loop runs through all references for the included study and saves relevant fields into an array that is created to hold information in the following positions:
					//0th, 9th ..index -> Type of publication, eg. Journal ->"TYPE"
					//1st, 10th..index -> Primary attribute: Yes or No ->"PRIMARY"
					//2nd ->Names of all authors of this publication ->"AU"
					//3rd->Title of publication->"TI"
					//4th->Name of Journal->Journal->"SO"
					//5-> Year published->"YR"
					//6->"VL"
					//7-> Number ->"NO"
					//8-> Pages ->"PG"
				
					
					NodeList referencesList = studyElement.getElementsByTagName("REFERENCE");
					int numberReferences = referencesList.getLength();
					references = new String[numberReferences * 9];
					int arrayCounter = 0;
					
					for (int i = 0; i < numberReferences; i++){
						Element referenceElement = null;
						try {
							Node referenceNode = referencesList.item(i);
							referenceElement = (Element) referenceNode;
						} catch (Exception e7) {
							// TODO Auto-generated catch block
							e7.printStackTrace();
						}
					
						try {
							if (referenceElement != null){
							references[arrayCounter] = referenceElement.getAttribute("TYPE"); // 0th, 9th ... index of array represents type of publication, e.g. Journal article
							System.out.println(referenceElement.getAttribute("TYPE"));
							}
						} catch (Exception e6) {
							// TODO Auto-generated catch block
							e6.printStackTrace();
						}
						
						arrayCounter++;
						
						try {
							if (referenceElement != null){
							references[arrayCounter] = referenceElement.getAttribute("PRIMARY"); //1st, 10th index of array represents primary attribute: Yes or No
							System.out.println(referenceElement.getAttribute("PRIMARY"));
							}
						} catch (Exception e5) {
							// TODO Auto-generated catch block
							e5.printStackTrace();
						}
						
						arrayCounter++;
						
						
						Element auElement = null;
						try {
							NodeList auList = referenceElement.getElementsByTagName("AU");
							Node auNode = auList.item(0);
							auElement = (Element) auNode;
						} catch (Exception e5) {
							// TODO Auto-generated catch block
							e5.printStackTrace();
						}
						
						try {
							if (auElement != null){
							references[arrayCounter] = auElement.getTextContent().replaceAll("\n", "").trim(); //2nd, 11th... Authors names for this reference
							System.out.println(auElement.getTextContent().replaceAll("\n", "").trim());
							}
						} catch (DOMException e4) {
							// TODO Auto-generated catch block
							e4.printStackTrace();
						}
						
						arrayCounter++;
						
						
						Element tiElement = null;
						try {
							NodeList tiList = referenceElement.getElementsByTagName("TI");
							Node tiNode = tiList.item(0);
							tiElement = (Element) tiNode;
						} catch (Exception e4) {
							// TODO Auto-generated catch block
							e4.printStackTrace();
						}
						
						try {
							if (tiElement != null){
							references[arrayCounter] = tiElement.getTextContent().replaceAll("\n", "").trim();
							System.out.println(tiElement.getTextContent().replaceAll("\n", "").trim());
							}
						} catch (DOMException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
						
						arrayCounter++;
						
						
						Element soElement = null;
						try {
							NodeList soList = referenceElement.getElementsByTagName("SO");
							Node soNode = soList.item(0);
							soElement = (Element) soNode;
						} catch (Exception e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
						
						try {
							if (soElement != null){
							references[arrayCounter] = soElement.getTextContent().replaceAll("\n", "").trim();
							System.out.println(soElement.getTextContent().replaceAll("\n", "").trim());
							}
						} catch (DOMException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						arrayCounter++;
						
						
						Element yrElement = null;
						try {
							NodeList yrList = referenceElement.getElementsByTagName("YR");
							Node yrNode = yrList.item(0);
							yrElement = (Element) yrNode;
						} catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						try {
							if (yrElement != null){
							references[arrayCounter] = yrElement.getTextContent().replaceAll("\n", "").trim();
							System.out.println(yrElement.getTextContent().replaceAll("\n", "").trim());
							}
						} catch (DOMException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						arrayCounter++;
						
						
						Element vlElement = null;
						try {
							NodeList vlList = referenceElement.getElementsByTagName("VL");
							Node vlNode = vlList.item(0);
							vlElement = (Element) vlNode;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							if (vlElement != null){
							references[arrayCounter] = vlElement.getTextContent().replaceAll("\n", "").trim();
							System.out.println(vlElement.getTextContent().replaceAll("\n", "").trim());
							}
						} catch (DOMException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						arrayCounter++;
						
						Element noElement = null;
						try {
							NodeList noList = referenceElement.getElementsByTagName("NO");
							Node noNode = noList.item(0);
							noElement = (Element) noNode;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							if (noElement != null){
							references[arrayCounter] = noElement.getTextContent().replaceAll("\n", "").trim();
							System.out.println(noElement.getTextContent().replaceAll("\n", "").trim());
							}
						} catch (DOMException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						arrayCounter++;
						
						Element pgElement = null;
						try {
							NodeList pgList = referenceElement.getElementsByTagName("PG");
							Node pgNode = pgList.item(0);
							pgElement = (Element) pgNode;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							if (pgElement != null){
							references[arrayCounter] = pgElement.getTextContent().replaceAll("\n", "").trim();
							System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
							}
						} catch (DOMException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						arrayCounter++;
						
						
					
						
						
					}
					
					
							
					
					
					
					System.out.println();
					
					
					
					
	}
	


}
