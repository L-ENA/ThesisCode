import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	protected String[] references; //check

	protected boolean crossoverTrial = false;
	protected boolean paralellTrial = false;
	protected boolean factorialTrial = false;
	protected boolean otherDesign = false;
	protected String designProse;
	protected String designAddedInfo;
	//protected RANDOMIZATIONTYPE randomisation:
	//protected ALLOCATIONTYPE allocation;
	//protected BLINDINGTYPE blinding;
	//protected ATTRITIONTYPE attrition;
	//protected REPORTINGTYPE reporting;
	
	protected String selectionBiasRandomSequenceJudgement;
	protected String selectionBiasRandomSequenceBiasRisk;
	protected String selectionBiasAllocationConcealmentBiasRisk;
	protected String selectionBiasAllocationConcealmentJudgement;
	protected String performanceBiasRisk;
	protected String performanceBiasJudgement;
	protected String detectionBiasRisk;
	protected String detectionBiasJudgement;
	protected String attritionBiasRisk;
	protected String attritionBiasJudgement;
	protected String reportingBiasRisk;
	protected String reportingBiasJudgement;
	protected String otherBiasRisk;
	protected String otherBiasJudgement;
	
	protected int nrOfParticipants;
	protected String interventionGroup;
	protected String controlGroup;

	protected int nrIntervention;
	protected int nrControl;
	//protected SETTING setting;
	//protected outcomeList : OutcomeObjects
	
	private Matcher m;
	private Pattern design = Pattern.compile("([Dd]esign)+");
	//parallel
	private Pattern parallelDesign = Pattern.compile("([pP]arallel)+|([Bb]etween\\s[Pp]atient(s)?)+|([Nn]on(-|\\s)[Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern parallelDesignCleaner = Pattern.compile("([Dd]esign)+|([pP]arallel)+|([Gg]roup(s)?)+|([Ss]tud(y|ies))?+|[:(),.]+");
	//cross over, crossed over, crossover, cross-over, crossed-over + caps variations
	private Pattern crossoverDesign = Pattern.compile("([Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern crossoverDesignCleaner = Pattern.compile("([Dd]esign)+|([Cc]ross(ed)?-?\\s?[Oo](ver))+|[:(),.]+");
	//factorial, fully crossed + caps variation
	private Pattern factorialDesign = Pattern.compile("(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+");
	private Pattern factorialDesignCleaner = Pattern.compile("([Dd]esign)+|(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+|[:(),.]+");
	
	//patterns for beginning/end of strings
	private Pattern beginningEndArray = Pattern.compile("^\\W+|[^\\w)]$"); //All special characters at beginning plus all special characters without closing brackets at end, because of factorial design
	private Pattern beginningEnd = Pattern.compile("^\\W+|\\W+$");	//All special chars at beginning and end
	private Pattern endPunctuationCleaner = Pattern.compile("([^.!?\"']$)");	//matches when neither .!?"' at end of sentence
	private Pattern endPunctuationCleaner2 = Pattern.compile("([^!?.][\"'])$");	//to be applied afer other punctuation regex. matches when there is no !?. before "'
	private Element qualityItemsElement;
	private NodeList qualityItemList;
	
	public TrialObject(Document review, int studyNumber){
					
					//Creates rootElement
					
					Element rootElement = null;
					try {
						NodeList rootList = review.getElementsByTagName("COCHRANE_REVIEW");
						Node rootNode = rootList.item(0);
						rootElement = (Element) rootNode;
					} catch (Exception e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();
					}
					
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Traverses the characteristics of included studies part of the revman file. This is where bias tables and prose information are located
					Element studyToExtractElement = null;
					Element characteristicsOfStudiesElement = null;
					try {
						NodeList characteristicsOfStudiesList = rootElement.getElementsByTagName("CHARACTERISTICS_OF_STUDIES");
						Node characteristicsOfStudiesNode = characteristicsOfStudiesList.item(0);
						characteristicsOfStudiesElement = (Element) characteristicsOfStudiesNode;
						
						NodeList characteristicsOfIncludedStudiesList = characteristicsOfStudiesElement.getElementsByTagName("CHARACTERISTICS_OF_INCLUDED_STUDIES");
						Node characteristicsOfIncludedStudiesNode = characteristicsOfIncludedStudiesList.item(0);
						Element characteristicsOfIncludedStudiesElement = (Element) characteristicsOfIncludedStudiesNode;
						
						NodeList includedStudiesList = characteristicsOfIncludedStudiesElement.getElementsByTagName("INCLUDED_CHAR");
						Node studyToExtractNode = includedStudiesList.item(studyNumber);
						studyToExtractElement = (Element) studyToExtractNode;
					} catch (Exception e10) {
						// TODO Auto-generated catch block
						e10.printStackTrace();
					}
					
					//Extracts information into variables
					//Gets revman ID for study
					revManID = studyToExtractElement.getAttribute("STUDY_ID"); 
					String cache = revManID.replaceAll("STD-", "");
					String[] cacheArray = cache.split("-");
					mainAuthor = cacheArray[0]; //Take name of author from ID
					
					NodeList qualityItemsList = rootElement.getElementsByTagName("QUALITY_ITEMS");
					Node qualityItemsNode = qualityItemsList.item(0);
					qualityItemsElement = (Element) qualityItemsNode;
					
					String[] biasArray;
					
					biasArray = biasAnalyser(0, "random sequence generation");
					selectionBiasRandomSequenceBiasRisk = biasArray[0];
					selectionBiasRandomSequenceJudgement = biasArray[1];
					
					biasArray = biasAnalyser(1, "allocation concealment");
					selectionBiasAllocationConcealmentBiasRisk = biasArray[0];
					selectionBiasAllocationConcealmentJudgement = biasArray[1];
					
					biasArray = biasAnalyser(2, "performance bias");
					performanceBiasRisk = biasArray[0];
					performanceBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(3, "detection bias");
					detectionBiasRisk = biasArray[0];
					detectionBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(4, "incomplete outcome data");
					attritionBiasRisk = biasArray[0];
					attritionBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(5, "selective reporting");
					reportingBiasRisk = biasArray[0];
					reportingBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(6, "other bias");
					otherBiasRisk = biasArray[0];
					otherBiasJudgement = biasArray[1];
					
					System.out.println(mainAuthor + "\n" +
							" RandomSequenceBias: " + selectionBiasRandomSequenceBiasRisk + ". " + selectionBiasRandomSequenceJudgement 
							+ "\n" + ". allocationBias: " + selectionBiasAllocationConcealmentBiasRisk + ". " + selectionBiasAllocationConcealmentJudgement
							+ "\n" + ". performanceBias: " + performanceBiasRisk + ". " + performanceBiasJudgement
							+ "\n" + ". detectionBias: " + detectionBiasRisk + ". " + detectionBiasJudgement
							+ "\n" + ". attritionBias: " + attritionBiasRisk + ". " + attritionBiasJudgement
							+ "\n" + ". reportingBias: " + reportingBiasRisk + ". " + reportingBiasJudgement
							+ "\n" + ". otherBias: " + otherBiasRisk + ". " + otherBiasJudgement
							);
					
					
					//////////////////////////////////////////////////////////////////////////////////////////////////
					//extracts prose about methods and tries to match this prose to create a standardised output
					NodeList charMethodsList = studyToExtractElement.getElementsByTagName("CHAR_METHODS");
					Node charMethodsNode = charMethodsList.item(0);
					Element charMethodsElement = (Element) charMethodsNode;
					
					//NodeList methodParagraphList = charMethodsElement.getElementsByTagName("P");
					//Node methodNode = methodParagraphList.item(0);
					//Element methodElement = (Element) methodNode;
						
					String methodString = charMethodsElement.getTextContent();
					String[] methodStringArray = methodString.split("\\.");
					
					
					
					for (int k = 0; k<methodStringArray.length; k++){
						
						m = beginningEndArray.matcher(methodStringArray[k]);	//Factorial design with brackets causes problems otherwise
						methodStringArray[k] = m.replaceAll("");
						
						m = design.matcher(methodStringArray[k]);	
						if (m.find()){	//Uses regex pattern to identify if this line is about design of trial
							designVerifyer(methodStringArray[k]);
						}
					}
					
					
					////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Traverses the revman file's Studies and references section
					Element studyElement = null;
					try {
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
						studyElement = (Element) studyNode;
					} catch (Exception e9) {
						// TODO Auto-generated catch block
						e9.printStackTrace();
					}
					
					try {
						cache = studyElement.getAttribute("YEAR");
						year = Integer.parseInt(cache); //converts String to int
					} catch (NumberFormatException e8) {
						// if year can't be extracted as attribute it is tried via revman-ID
						cache = revManID.replaceAll("[^\\d.]", "");
						year = Integer.parseInt(cache); //Takes year of publication from ID
					}
					
					
					
					//System.out.println("Year of publication: " + year);
				//	System.out.println("Main author of Study: " + mainAuthor);
					//System.out.println("RevMan ID: "+ revManID);
					
					//This following for-loop runs through all references for the included study and saves relevant fields into an array that is created to hold information in the following positions:
					//0th, 9th ..index -> Type of publication, eg. Journal ->"TYPE"
					//1st..., 10th..index -> Primary attribute: Yes or No ->"PRIMARY"
					//2nd... ->Names of all authors of this publication ->"AU"
					//3rd...->Title of publication->"TI"
					//4th...->Name of Journal->Journal->"SO"
					//5...-> Year published->"YR"
					//6...->Volume ->"VL" 
					//7...-> Issue ->"NO"
					//8...-> Pages ->"PG"
					
					//if a filed is not available, eg. if the reference refers to a conference protocol that lacks page numbers, empty "" space is inserted and next position of array is tried to be filled
				
					
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
						}
					
						try {
							if (referenceElement != null){
							references[arrayCounter] = referenceElement.getAttribute("TYPE"); 
							//System.out.println(referenceElement.getAttribute("TYPE"));
							} else {
								references[arrayCounter] = "";
							}
						} catch (Exception e6) {
						}
						arrayCounter++;
						
						try {
							if (referenceElement != null){
							references[arrayCounter] = referenceElement.getAttribute("PRIMARY");
							//System.out.println(referenceElement.getAttribute("PRIMARY"));
							} else {
								references[arrayCounter] = "";
							}
						} catch (Exception e5) {
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
							//System.out.println(auElement.getTextContent().replaceAll("\n", "").trim());
							} else {
								references[arrayCounter] = "";
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
							//System.out.println(tiElement.getTextContent().replaceAll("\n", "").trim());
							} else {
								references[arrayCounter] = "";
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
							//System.out.println(soElement.getTextContent().replaceAll("\n", "").trim());
							} else {
								references[arrayCounter] = "";
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
							//System.out.println(yrElement.getTextContent().replaceAll("\n", "").trim());
							} else {
								references[arrayCounter] = "";
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
							//System.out.println(vlElement.getTextContent().replaceAll("\n", "").trim());
							} else {
								references[arrayCounter] = "";
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
							//System.out.println(noElement.getTextContent().replaceAll("\n", "").trim());
							} else {
								references[arrayCounter] = "";
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
							//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
							} else {
								references[arrayCounter] = "";
							}
						} catch (DOMException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						arrayCounter++;
						
						
					
						
						
					}
					
					
							
					for (int j = 0; j<references.length; j++){
						//System.out.println(references[j]);
					}
					
					
					System.out.println();
					
					
					
					
	}
	
	private void designVerifyer(String str){
		
		//To see if this String contains info on trial design
		m = parallelDesign.matcher(str); 	//Uses regex pattern for identifying parallel trials
		if (m.find()){	//To see if this trial is a parallel trial. Returns true if the trial is parallel
			paralellTrial = true;	//Boolean to store that this trial is parallel
			designProse = str.trim();	//stores intact description of the trial
			m = parallelDesignCleaner.matcher(str);	//uses regex pattern for cleaning parellel trial
			designAddedInfo = m.replaceAll("").trim();	//cleans additional info from prose
			m = beginningEnd.matcher(designAddedInfo);	//regex pattern for cleaning beginning/end from non-word characters
			designAddedInfo = m.replaceAll("");	//cleans additional info from prose
			
			if (designAddedInfo.length() != 0)	//capitalises first letter
				designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
			
			//System.out.println(mainAuthor+ ". Added Info: " + designAddedInfo + ". Prose: " + designProse);
			
		} else {
			m = crossoverDesign.matcher(str); 
			if (m.find()){
				crossoverTrial = true;
				designProse = str.trim();
				m = crossoverDesignCleaner.matcher(str);
				designAddedInfo = m.replaceAll("").trim();
				m = beginningEnd.matcher(designAddedInfo);
			
				designAddedInfo = m.replaceAll("");
				if (designAddedInfo.length() != 0)
					designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
				
				//System.out.println(mainAuthor+ ". Added Info: " + designAddedInfo + ". Prose: " + designProse);
			} else {
				m = factorialDesign.matcher(str);
				if (m.find()){
					factorialTrial = true;
					designProse = str.trim();
					m = factorialDesignCleaner.matcher(str);
					designAddedInfo = m.replaceAll("").trim();
					m = beginningEnd.matcher(designAddedInfo);
				
					designAddedInfo = m.replaceAll("");
					if (designAddedInfo.length() != 0)
							designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
					
					
					//System.out.println(mainAuthor+ ". Added Info: " + designAddedInfo + " .Prose: " + designProse);
				} else {
					otherDesign = true;
					designProse = str.trim();
					designAddedInfo = "";
				}
			}
		}
		
	}
	
	private String[] biasAnalyser(int index, String description){
		
		String[] forReturn = {"", ""}; //index 0 will hold choice of bias risk, index 1 will hold judgement/quotes
		qualityItemList = qualityItemsElement.getElementsByTagName("QUALITY_ITEM");
		Node specificItemNode = qualityItemList.item(index);	//Index navigates to the desired bias item
		Element specificItemElement = (Element) specificItemNode;
		
		NodeList nameList = specificItemElement.getElementsByTagName("NAME");
		Node nameNode = nameList.item(0);
		Element nameElement = (Element) nameNode;
		
		NodeList qualityItemDataList = specificItemElement.getElementsByTagName("QUALITY_ITEM_DATA");
		Node qualityItemDataNode = qualityItemDataList.item(0);
		Element qualityItemDataElement = (Element) qualityItemDataNode;
		
		NodeList specificItemDataEntryList = qualityItemDataElement.getElementsByTagName("QUALITY_ITEM_DATA_ENTRY");
		int entryLength = specificItemDataEntryList.getLength();
		
		String biasVerification = nameElement.getTextContent().toLowerCase();
		if (entryLength != 0 && biasVerification.contains(description) ) {
			for (int l = 0; l < entryLength; l++) {
				Node specificDataEntryNode = specificItemDataEntryList.item(l);
				Element specificDataEntryElement = (Element) specificDataEntryNode;
				if (specificDataEntryElement.getAttribute("STUDY_ID").equals(revManID)) { //searches the desired trial for this object
					if (specificDataEntryElement.getAttribute("RESULT").equals("UNKNOWN")) { //These are the options from RevMans dropdown menu in the bias table
						forReturn[0] = "Unclear Risk";
					} else if (specificDataEntryElement.getAttribute("RESULT").equals("YES")) {
						forReturn[0] = "Low risk";
					} else if (specificDataEntryElement.getAttribute("RESULT").equals("NO")) {
						forReturn[0] = "High risk";
					} else {
						forReturn[0] = "Information on " + description + " could not be extracted";	//if anything unexpected happens
					}
					forReturn[1] = specificDataEntryElement.getTextContent().trim().replaceAll("\n", ", ");
					
					m = endPunctuationCleaner.matcher(forReturn[1]);
					if (m.find())										//Cleaning data: Patterns match when there is NO .!?"' at the very end 
						forReturn[1] = forReturn[1] + ".";				//and while there is also no .!? before the last "'  -> in this case a full stop will be inserted
					m = endPunctuationCleaner2.matcher(forReturn[1]);
					if (m.find())
						forReturn[1] = forReturn[1] + ".";
				}
			} 
		} else {
			index++;
			return biasAnalyser(index, description);
		}
		return forReturn;
	}
	


}
