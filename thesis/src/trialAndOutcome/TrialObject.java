package trialAndOutcome;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import references.BookSectionReferenceObject;

import references.BookReferenceObject;
import references.CochraneProtocolReferenceObject;
import references.CochraneReviewReferenceObject;
import references.ConferenceReferenceObject;
import references.CorrespondenceReferenceObject;
import references.JournalReferenceObject;
import references.OtherReferenceObject;
import references.ReferenceObject;
import references.SoftwareReferenceObject;
import references.UnpublishedReferenceObject;


public class TrialObject{
	///////////////////////////////////////////////////empty constructor for xml creation only
	public TrialObject() {
		super();
	}
//comment
//////////////////////////////////////////////////////////////////attributes. the protected ones have getters and setters because they will end up in the finished XML
	//protected static int counter = 1;
	protected String mainAuthor = ""; //check
	protected int year;//will contain year of publication
	protected String aauthorYearLetter = ""; //for comparison with MeerKatBE
	
	protected String reviewTitle = "";
	

	protected String doi = "";
	
	protected String revManID;//check
	protected String[] references; //to contain all references to this trial
	
	protected String methodString = "";//holds whole methods paragraph in prose
	protected String participantString = "";//holds whole participants paragraph in prose

	

	protected DESIGN trialDesign = DESIGN.NOTAVAILABLE;////variables to do with trial design info
	private boolean crossoverTrial = false;
	private boolean paralellTrial = false;
	private boolean factorialTrial = false;
	private boolean otherDesign = false;
	protected String designProse = "";
	protected String designAddedInfo = "";
	
	protected String fundingProse = "";
	

	protected String otherMethodProse = "";
	protected String otherParticipantProse = "";
	
	protected String countries = "";//////////////to contain country or countries, in alphabetical order. Beware that it will not contain city names!!
	protected String meerKatCountry = "";//alphabetical, syntax: Either just "country" or ""Multi-Center//country//country//.."
	protected String countryCombined = ""; // this string is going to be big. but in case a country is not added in the list or a city is added instead of a country we need to screen these one by one
	protected String countryProse = "";
	
	protected SETTING trialSetting = SETTING.NOTAVAILABLE;	//will hold the final value for setting in which this trial was conducted
	private boolean outP = false;	//Setting info can appear in various fields and be easily falsified by the occurrence of simple words such as "hospital". These boolean turn true if a very clear indication of the setting appears. If the final setting variable later differs from these clear indications, the setting variable will be adjusted.
	private boolean inP = false;
	private boolean bothP = false;
	private boolean emergencyR = false;
	private boolean psychiatricH = false;
	private boolean hospitalSetting = true; 
	
	protected String settingProse = ""; //Is filled when a prose line contains the words "Setting:"
	protected String settingCombined = "";//Is filled with all the prose lines that could possibly contain info on setting
	
	
	protected String ratersProse = "";
	
	

	protected String allocationProse = "";
	protected String durationProse = "";
	protected String lostToFollowUpProse = "";
	protected String consentProse = "";
	protected String locationProse = "";
	protected String followUpProse = "";


	public String getFollowUpProse() {
		return followUpProse;
	}

	public void setFollowUpProse(String followUpProse) {
		this.followUpProse = followUpProse;
	}

	private BLINDNESS blindingMethod = BLINDNESS.NOTAVAILABLE;//all about blinding
	protected String blindnessCleaned = BLINDNESS.NOTAVAILABLE.getDescription();
	protected String blindingProse = "";
	
	////////////Strings that are extracted from Participants field of characteristics of included studies table
	protected String diagnosisProse = "";
	protected String historyProse = "";
	protected String ageProse= "";
	protected String genderProse = "";
	protected String excludedProse = "";
	protected String includedProse = "";
	protected String nProse = "";
	protected String durationIllProse= "";
	protected String ethnicityProse= "";
	
	
	

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
	
	
	
	protected DichotomousOutcomeObject dobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list and re-filled with the next outcome
	protected ContinuousOutcomeObject cobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list and re-filled with the next outcome
	protected ReferenceObject refObject;/// for one reference, can be of different types. Procedure similar to outcomeObject. 
	protected List<ReferenceObject> referenceList = new ArrayList<>(); ///////List that will contain all referenceObjects


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////all the patterns for cleaning and extraction
	private int breakBiasVerification;
	private Matcher m;
	
	//parallel
	private Pattern parallelDesign = Pattern.compile("([pP]arallel)+|([Bb]etween\\s[Pp]atient(s)?)+|([Nn]on(-|\\s)[Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern parallelDesignCleaner = Pattern.compile("([Dd]esign)+|([pP]arallel)+|([Gg]roup(s)?)+|([Ss]tud(y|ies))?+|[:(),.]+");
	//cross over, crossed over, crossover, cross-over, crossed-over + caps variations
	private Pattern crossoverDesign = Pattern.compile("([Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern crossoverDesignCleaner = Pattern.compile("([Dd]esign)+|([Cc]ross(ed)?-?\\s?[Oo](ver))+|[:(),.]+");
	//factorial, fully crossed + caps variation
	private Pattern factorialDesign = Pattern.compile("(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+");
	private Pattern factorialDesignCleaner = Pattern.compile("([Dd]esign)+|(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+|[:(),.]+");
	
	private Pattern inPatient = Pattern.compile("([Ii]n[-\\s]?\\s?((and\\s|or\\s|\\s?/\\s?)out)?patient(s)?)");//Setting patterns
	private Pattern outPatient = Pattern.compile("([Oo]ut[-\\s]?\\s?((and\\s|or\\s|\\s?/\\s?)in)?patient(s)?)");
	private Pattern emergencyRoom = Pattern.compile("([Ee]mergency\\s(rooms?|departments?))");
	private Pattern community = Pattern.compile("([Cc]ommunity|[Cc]ommunities)");
	private Pattern hospital = Pattern.compile("([Ii]n\\s)?([Hh]ospital(i[sz]ed)?)|([Mm]ental\\shealth\\scente?re?)|([Ii]ntensive\\scare)");
	private Pattern psychiatricHospital = Pattern.compile("([Pp]sychiatr(y|ic))");
	
	private Pattern blindness = Pattern.compile("([Bb]linding:)|([Bb]lindness:)|(([Dd]ouble|[Ss]ingle|[Tt]riple)\\s[Bb]lind:)|([Bb]lind:)");
	private Pattern doubleBlind = Pattern.compile("[Dd]ouble");
	private Pattern singleBlind = Pattern.compile("([Ss]ingle)");
	private Pattern tripleBlind = Pattern.compile("[Tt]riple");
	private Pattern quadrupleBlind = Pattern.compile("[Qq]uadruple");
	private Pattern openLabel = Pattern.compile("([Oo]pen)|([Nn]ot\\sblind(ed)?)|([Nn]on\\s?-?[Bb]lind(ing)?)|(:\\s?(no|none)\\Z)");
	private Pattern unclearBlinding = Pattern.compile("([Nn]ot\\sclear)|([Uu]nclear)");
	private Pattern notReported = Pattern.compile("([Nn]ot?\\s(specified|stated|report|reported|indicated|indication|described|description|available|information))");
	
	private Pattern countryPattern = Pattern.compile("(Locations?:)|([Cc]ountr(y|ies):)|([Ss]etting:)");
	
	//patterns for beginning/end of strings
	private Pattern beginningEndArray = Pattern.compile("^\\W+|[^\\w)]$"); //All special characters at beginning plus all special characters without closing brackets at end, because of factorial design
	private Pattern beginningEnd = Pattern.compile("^\\W+|\\W+$");	//All special chars at beginning and end
	private Pattern endPunctuationCleanerA = Pattern.compile("([^.!?\"']$)");	//matches when neither .!?"' at end of sentence. Needs 
	private Pattern endPunctuationCleanerB = Pattern.compile("([^!?.][\"'])$");	//to be applied after other punctuation regex. matches when there is no !?. before "'
	
	/////////////////////////////////////////////////////////////xml related attributes that are used between different methods in the big constructor
	private Element qualityItemsElement;
	Element studyElement;
	private NodeList qualityItemList;
	private NodeList referencesList;
	
	public TrialObject(Document review, int studyNumber){//////////the constructor that fills all attributes
					
					//Creates rootElement
					
					Element rootElement = null;
					try {
						NodeList rootList = review.getElementsByTagName("COCHRANE_REVIEW");
						Node rootNode = rootList.item(0);
						rootElement = (Element) rootNode;
						doi = rootElement.getAttribute("DOI");
						
					} catch (Exception e11) {
						e11.printStackTrace();
					}
					
					try {
						NodeList coverSheetList = rootElement.getElementsByTagName("COVER_SHEET");
						Element coverSheetElement = (Element) coverSheetList.item(0);
						
						NodeList titleList = coverSheetElement.getElementsByTagName("TITLE");
						Element titleElement = (Element) titleList.item(0);
						reviewTitle = titleElement.getTextContent().trim();
						System.out.println(reviewTitle);
					} catch (DOMException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
						e10.printStackTrace();
					}
					
					//Extracts information into variables
					//Gets revman ID for study
					revManID = studyToExtractElement.getAttribute("STUDY_ID"); 
					String cache = revManID.replaceAll("STD-", "").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&");// replaces space with hyphen and at first puts & back 
					String[] cacheArray = cache.split("-\\d+");	//Splits at "-"+ digit
					mainAuthor = cacheArray[0]; //Take name of author from ID
					
					String yearLetter;
					try {
						yearLetter = cacheArray[1];
					} catch (Exception e) {
						// if this trialname/year is unique in the review
						yearLetter = "";
					}
					
					
					
					NodeList qualityItemsList = rootElement.getElementsByTagName("QUALITY_ITEMS");
					Node qualityItemsNode = qualityItemsList.item(0);
					qualityItemsElement = (Element) qualityItemsNode;
					
					qualityItemList = qualityItemsElement.getElementsByTagName("QUALITY_ITEM");
					breakBiasVerification = qualityItemList.getLength();
					String[] biasArray;
					///////extracts all info on biases that the review author added. The Risk is a three field option (High, low or unclear Risk), judgement of bias includes : justification or quotes from the trials
					biasArray = biasAnalyser(0, "random sequence generation");
					selectionBiasRandomSequenceBiasRisk = biasArray[0];
					selectionBiasRandomSequenceJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "allocation concealment");
					selectionBiasAllocationConcealmentBiasRisk = biasArray[0];
					selectionBiasAllocationConcealmentJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "performance bias");
					performanceBiasRisk = biasArray[0];
					performanceBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "detection bias");
					detectionBiasRisk = biasArray[0];
					detectionBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "incomplete outcome data");
					attritionBiasRisk = biasArray[0];
					attritionBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "selective reporting");
					reportingBiasRisk = biasArray[0];
					reportingBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "other bias");
					otherBiasRisk = biasArray[0];
					otherBiasJudgement = biasArray[1];
					

					
					
					//////////////////////////////////////////////////////////////////////////////////////////////////
					//extracts prose about methods and tries to match this prose to create a standardised output
					NodeList charMethodsList = studyToExtractElement.getElementsByTagName("CHAR_METHODS");
					Node charMethodsNode = charMethodsList.item(0);
					Element charMethodsElement = (Element) charMethodsNode;
					
					methodString = charMethodsElement.getTextContent().trim();
					
					System.out.println("BIG STRING: " + methodString);
					//methodString = methodString.replace("\\n", "");
					
					splitMethods(methodString);
					
					
					designVerifyer(designProse);	
						
					
					
					cleanBlindness(blindingProse);
					
					
					
					//Extracts prose from participant section and tries to match
					NodeList charParticipantsList = studyToExtractElement.getElementsByTagName("CHAR_PARTICIPANTS");
					Node charParticipantsNode = charParticipantsList.item(0);
					Element charParticipantsElement = (Element) charParticipantsNode; 
					
					participantString = charParticipantsElement.getTextContent();
					splitParticipants(participantString);
					
					
					cleanSetting(settingProse);//Setting can appear in the 3 following Strings. They are searched for relevant information in the cleanSetting() method.
					cleanSetting(locationProse);
					cleanSetting(designProse);
					
					settingCombined = settingProse + " " + locationProse + " " + designProse; // setting in this context can have the values of the SETTING enum. Since this info is found in 3 different tyypes of prose Strings, these are combined here for double checking purposes
					settingCombined = settingCombined.replaceAll(" +", " ").trim();
					
					getCountry();	
					
					
					//getCountry(participantStringArray[i]);
					if (countryCombined != null)
					countryCombined = countryCombined.trim(); //in case 2 prose strings are appended to each other there will be an unnecessary whitespace in the end. this one is trimmed away
					
					
					
					
					
					
					////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Traverses the revman file's Studies and references section
					
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
						e9.printStackTrace();
					}
					
					
//test
					
					
referenceExtracting(); //Extracts all information on references of this trial. See method referenceExtracting() below for more details. Puts info into array of strings that will be further analysed below
					
					for (int i = 0; i < references.length; i++){
						if (references[i].equals("JOURNAL_ARTICLE")){
							refObject = new JournalReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14; 	//its plus 14 because +1 is added at the end of the loop and this array contains new info to check on every 15th index
						} else if (references[i].equals("CONFERENCE_PROC")){
							refObject = new ConferenceReferenceObject(references, i);
							referenceList.add(refObject);
							
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("UNPUBLISHED")){
							refObject = new UnpublishedReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("OTHER")){
							refObject = new OtherReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("BOOK_SECTION")){
							refObject = new BookSectionReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("CORRESPONDENCE")){
							refObject = new CorrespondenceReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("BOOK")){
							refObject = new BookReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("COCHRANE_REVIEW")){
							refObject = new CochraneReviewReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("COCHRANE_PROTOCOL")){
							refObject = new CochraneProtocolReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						}else if (references[i].equals("COMPUTER_PROGRAM")){
							refObject = new SoftwareReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						}
					}
					
					//tries to extract year when study was conducted
					try {
						cache = studyElement.getAttribute("YEAR");
						year = Integer.parseInt(cache); //converts String to int
					} catch (NumberFormatException e8) {
						try {
							// if year can't be extracted as attribute it is tried via revman-ID. In case there is a hyphen or..., the characters resulting from this are eliminated and the String is restored to normal
							cache = revManID.replaceAll("(_x002d_)", "-").replace("(_x0026_)", "&").replaceAll("[^\\d.]", "");
							year = Integer.parseInt(cache); //Takes year of publication from ID
						} catch (NumberFormatException e) {
							try {
								//tries to take year of publication from additional references, e.g.intensive case management review, when there is absolutely no date entered because this trial is just the description of one centre in the study, or in in vocational training, where authors deleted the years from RevMan ID or possibly forgot to enter it
								cache = refObject.getDate().replaceAll("[^\\d.]", "");
								year = Integer.parseInt(cache);
							} catch (NumberFormatException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								// last resort
								e.printStackTrace();
								year = 0000;
							}
							
							
							
						}
					}
					
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Extracts OutcomeObjects for this trial
					
						System.out.println("Main author of Study: " + mainAuthor);
						//System.out.println(trialSetting.getContent());
						//System.out.println(countries);
						//System.out.println(blindingMethod.getContent());
						//System.out.println(countries);
					
					//treverses xml to find data section
					NodeList analysesAndDataList = rootElement.getElementsByTagName("ANALYSES_AND_DATA");
					Node analysesAndDataNode = analysesAndDataList.item(0);
					Element analysesAndDataElement = (Element) analysesAndDataNode;
					
					NodeList comparisonList = analysesAndDataElement.getElementsByTagName("COMPARISON");
					for (int i = 0; i < comparisonList.getLength(); i++){
						Node comparisonNode = comparisonList.item(i);
						Element comparisonElement = (Element) comparisonNode;
						
						NodeList comparisonNameList = comparisonElement.getElementsByTagName("NAME");
						Node comparisonNameNode = comparisonNameList.item(0);
						Element comparisonNameElement = (Element) comparisonNameNode; //takeOver for comparisonname and groupnames
						
						
						try {////////////looks for dichotomous outcomes by their tag name. Elements are created and used as parameters for object creation. All
							//dichotomous outcome objects are traversed and it is checked if their ID equals the revman ID of the trial which outcomes 
							//are supposed to extracted in this call. All outcomes are added to the object list
							NodeList dichOutcomeList = comparisonElement.getElementsByTagName("DICH_OUTCOME");
							for (int l = 0; l < dichOutcomeList.getLength(); l++){
								Node dichOutcomeNode = dichOutcomeList.item(l);
								Element dichOutcomeElement = (Element) dichOutcomeNode;
								
								NodeList dichOutcomeNameList = dichOutcomeElement.getElementsByTagName("NAME");
								Node dichOutcomeNameNode = dichOutcomeNameList.item(0);
								Element dichOutcomeNameElement = (Element) dichOutcomeNameNode;
								
								
								NodeList dichSubgroupList = dichOutcomeElement.getElementsByTagName("DICH_SUBGROUP");
								for (int j = 0; j <dichSubgroupList.getLength(); j++){
									Node dichSubgroupNode = dichSubgroupList.item(j);
									Element dichSubgroupElement = (Element) dichSubgroupNode;
									
									NodeList dichDataList = dichSubgroupElement.getElementsByTagName("DICH_DATA");
									for (int k = 0; k < dichDataList.getLength(); k++){
										Node dichDataNode = dichDataList.item(k);
										Element dichDataElement = (Element) dichDataNode;
										
										if (dichDataElement.getAttribute("STUDY_ID").equals(revManID)){
											
											dobj = new DichotomousOutcomeObject(dichDataElement, comparisonNameElement, dichOutcomeNameElement, dichOutcomeElement, dichSubgroupElement);
											outcomeList.add(dobj);
											//System.out.println("Outcome added to list");
										}
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							NodeList contOutcomeList = comparisonElement.getElementsByTagName("CONT_OUTCOME");
							for (int l = 0; l < contOutcomeList.getLength(); l++){
								Node contOutcomeNode = contOutcomeList.item(l);
								Element contOutcomeElement = (Element) contOutcomeNode;
								
								NodeList contOutcomeNameList = contOutcomeElement.getElementsByTagName("NAME");
								Node contOutcomeNameNode = contOutcomeNameList.item(0);
								Element contOutcomeNameElement = (Element) contOutcomeNameNode;
								
								
								NodeList contSubgroupList = contOutcomeElement.getElementsByTagName("CONT_SUBGROUP");
								for (int j = 0; j <contSubgroupList.getLength(); j++){
									Node contSubgroupNode = contSubgroupList.item(j);
									Element contSubgroupElement = (Element) contSubgroupNode;
									
									NodeList contDataList = contSubgroupElement.getElementsByTagName("CONT_DATA");
									for (int k = 0; k < contDataList.getLength(); k++){
										Node contDataNode = contDataList.item(k);
										Element contDataElement = (Element) contDataNode;
										
										if (contDataElement.getAttribute("STUDY_ID").equals(revManID)){
											
											cobj = new ContinuousOutcomeObject(contDataElement, comparisonNameElement, contOutcomeNameElement, contOutcomeElement, contSubgroupElement);
											outcomeList.add(cobj);
											//System.out.println("Outcome added to list");
										}
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
//						NodeList comparisonNameList = comparisonElement.getElementsByTagName("NAME");
//						Node comparisonNameNode = comparisonNameList.item(0);
//						Element comparisonNameElement = (Element) comparisonNameNode;
//						
//						comparisonName = comparisonNameElement.getTextContent();
//						System.out.println(comparisonName);
					}
					
					
//					System.out.println(mainAuthor);
//					
//					System.out.println("RandomSequenceBias: " + selectionBiasRandomSequenceBiasRisk + ". " + selectionBiasRandomSequenceJudgement 
//							+ "\n" + "AllocationBias: " + selectionBiasAllocationConcealmentBiasRisk + ". " + selectionBiasAllocationConcealmentJudgement
//							+ "\n" + "PerformanceBias: " + performanceBiasRisk + ". " + performanceBiasJudgement
//							+ "\n" + "DetectionBias: " + detectionBiasRisk + ". " + detectionBiasJudgement
//							+ "\n" + "AttritionBias: " + attritionBiasRisk + ". " + attritionBiasJudgement
//							+ "\n" + "ReportingBias: " + reportingBiasRisk + ". " + reportingBiasJudgement
//							+ "\n" + "OtherBias: " + otherBiasRisk + ". " + otherBiasJudgement
//							);
//				
					
//					for (int j = 0; j<references.length; j++){
//						System.out.println(references[j]);
//					}
					
//								System.out.println(year);
//					
//								System.out.println(revManID);
								aauthorYearLetter = mainAuthor + year + yearLetter;
//								System.out.println(authorYearLetter);
//					
					//				
//					System.out.println("Country or countries : " + countries);
//					System.out.println(meerKatCountry);
					//aauthorYearLetter = mainAuthor+year+yearLetter;
					//System.out.println("Setting");
					//System.out.println(settingProse);
					System.out.println();
					
					
					
					
	}
	
	private void splitParticipants (String str) {
		ArrayList<String> storage = new ArrayList<String>();
		String[] splitParticipantParts;
		//location country setting
		splitParticipantParts = str.split("(?=(Locations?\\d?\\s?[:=]))|(?=([cC]ountr(y|ies)\\d?\\s?[:=]))|(?=([sS]etting\\d?\\s?[:=]))|(?=([Dd]iagnosis\\d?\\s?[:=]))|(?=(Types?\\d?\\s?:))|(?=([^(]\\s?\\b[Nn]\\b\\s?\\d?[:=]))|(?=(\\b[Aa]ge\\d?\\s?[:=]))|(?=((([Ss]ex)|([Gg]ender))\\d?\\s?[:=]))|(?=([Hh]istory\\d?\\s?[:=]))|(?=((([Ee]xcluded)|([Ee]x?cli?usions?(\\scriteria)?))\\d?\\s?[:=]))|(?=((([Ii]ncluded)|([Ii]nclusions?(\\scriteria)?))\\d?\\s?[:=]))|(?=([Dd]uration\\s[Ii]ll\\d?\\s?[:=]))|(?=(([Ee]thnicity\\d?\\s?[:=])|([Rr]ace\\d?\\s?[:=])))|(?=([cC]onsent(given)?\\d?\\s?[:=]))|(?=((?<!Lost\\sto\\s)Follow[-\\s]up\\d?\\s?[:=]))");
		
		for (int j = 0; j < splitParticipantParts.length; j++) {
			storage.add(splitParticipantParts[j].trim());
			System.out.println(splitParticipantParts[j]);
			
		}
		
		
		for (String output: storage) {
			if (output.matches("[Dd]iagnosis\\d?\\s?[:=].*") || output.matches("Types?\\d?\\s?:.*")) {//since the whole String needs to match, the .* is used as wildcard for the rest of the String
				diagnosisProse = diagnosisProse + output;
			} else if (output.matches("[Hh]istory\\d?\\s?[:=].*")) {
				historyProse = output;
			} else if (output.matches("[Aa]ge\\d?\\s?[:=].*")) {
				ageProse = output;
			} else if (output.matches("(([Ss]ex)|([Gg]ender))\\d?\\s?[:=].*")) {
				genderProse = output;
			} else if (output.matches("(([Ee]xcluded)|([Ee]xclusions?\\s?(criteria)?))\\d?\\s?([:=]).*")) {
				
				excludedProse = output;
			} else if (output.matches("(([Ii]ncluded)|([Ii]nclusions?(\\scriteria)?))\\d?\\s?[:=].*")) {
				includedProse = output;
			} else if (output.matches("[^(]\\s?\\b[Nn]\\b\\s?\\d?=?\\s?[:=]?.*")) {
				if (nProse != "") {
					JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green. " + revManID + ", " + reviewTitle);
				}
				nProse = output;
			} else if (output.matches("[lL]ocations?\\d?\\s?[:=].*")) {//next 3(location, country, setting) have special treatment because they can appear in methods section also. Sometimes they appear double, so Strings have to be appended to each other
				if (locationProse.equals("")) {//if it is empty, there is no double occurrence, so it can be filled as usual
					locationProse = output;
				} else {//it was not empty, so the new found String is appended to old content
					locationProse = locationProse + " " + output;
				}
			} else if (output.matches("[cC]ountr(y|ies)\\d?\\s?[:=].*")) {
				if (countryProse.equals("")) {
					countryProse = output;
				} else {
					countryProse = countryProse + " " + output;
				}
			} else if (output.matches("[sS]etting\\d?\\s?[:=].*")) {
				if (settingProse.equals("")) {
					settingProse = output;
				} else {
					settingProse = settingProse + " " + output;
				}
				
				
				
			} else if (output.matches("(?<!Lost\\sto\\s)Follow[-\\s]up\\d?\\s?[:=].*")) {
				
				if (followUpProse.equals("")) {
					followUpProse = output;
				} else {
					
					followUpProse = followUpProse + " " + output;
				}
				
			} 
			
			else if (output.matches("[cC]onsent(given)?\\d?\\s?[:=].*")) {
				consentProse = output;
			} else if (output.matches("[Dd]uration\\s[Ii]ll\\d?\\s?[:=].*")) {
				durationIllProse = output;
			} else if (output.matches("([Ee]thnicity)|([Rr]ace)\\d?\\s?[:=].*")) {
				ethnicityProse = output;
			} else {
				otherParticipantProse = output.trim();
				if (otherParticipantProse.contains("Exclusion criteria")) {
					excludedProse = output.trim();
					otherParticipantProse = "";
				}
				System.out.println("OTHER: " + otherParticipantProse);
				
			}
		}
		
		
	}
	
	private void splitMethods (String str) {//Uses positive lookahead regex to split method prose into Strings that are stored in Array and later identified ant written into their respective prose Strings																													//take care here
		ArrayList<String> storage = new ArrayList<String>();//this is used because lists are easier to have variables added to them
		String[] splitMethodParts;//to store all parts of the big methods string that gets broken by the regex
		
		
		if (str.contains("Lost\\sto\\s[Ff]ollow[\\s-]up:)")) {//necessary because there can be an appearance of only "Follow-up:", here we would loose the "Lost to" part of the String
			//splits the string at the occurrence of all the words that folllow in the regex. by including the ":", the splitting is safer because the words themselves can appear throughout the table randomly.
			splitMethodParts = str.split("(?=(([Ff]unding:)|([Ff]unded\\sby)))|(?=([rR]aters?))|(?=([aA]llocation:))|(?=([Bb]linding:)|([Bb]lindness:)|(([Dd]ouble|[Ss]ingle|[Tt]riple)\\s[Bb]lind:)|([Bb]lind:))|(?=([dD]uration:))|(?=([dD]esign:))|(?=(([lL]oss:)|([lL]ost\\sto\\s[Ff]ollow[\\s-]up:)))|(?=([cC]onsent(given)?:))|(?=(Locations?:))|(?=([cC]ountr(y|ies):))|(?=([sS]etting:))");
			
			for (int j = 0; j < splitMethodParts.length; j++) {
				
				if (splitMethodParts[j].contains("(?<!Lost\\sto\\s)Follow[-\\s]up:")) {//this negative lookbehind regex identifies a "Follow-up" that is not preceded by a "Lost to". therefore, this String has to be treated differently to prevent confusion and wrong classification, becauase it is possible to have both a "Lost to follow up:" and a "Follow up:" in the same methods paragraph
					String[] cache = splitMethodParts[j].split(("(?=(Follow[\\s-]up:))"));// before, only the "Lost to follow up" was split from the rest. now, the line above identified an additional "Follow up:" String, this one has to be split from the rest as well.
					storage.add(cache[0].trim());
					storage.add(cache[1].trim());
				} else {
					storage.add(splitMethodParts[j].trim());
				}
			}
			
		} else  {//since the first option did not come true, the String is checked for "Follow-up:" only
			
			splitMethodParts = str.split("(?=(([Ff]unding:)|([Ff]unded\\sby)))|(?=([rR]aters?))|(?=([aA]llocation:))|(?=([Bb]linding:)|([Bb]lindness:)|(([Dd]ouble|[Ss]ingle|[Tt]riple)\\s[Bb]lind:)|([Bb]lind:))|(?=([Dd]uration:))|(?=([dD]esign:))|(?=([fF]ollow[\\s-]up:))|(?=([lL]oss:))|(?=([cC]onsent:))|(?=(Locations?:))|(?=([cC]ountr(y|ies):))|(?=([sS]etting:))");
			
			for (int j = 0; j < splitMethodParts.length; j++) {
				storage.add(splitMethodParts[j].trim());
				System.out.println(splitMethodParts[j]);
			}
		
		}
		
		for (String output: storage) {
			
			if (output.matches("(Allocation:).*")) {//the wildcard asterisk is making sure that the whole string matches
				allocationProse = output;
				System.out.println("1. Allocation -- " + allocationProse);
			} else if (output.matches("([Bb]linding:).*|([Bb]lindness:).*|(([Dd]ouble|[Ss]ingle|[Tt]riple)\\s[Bb]lind:).*|([Bb]lind:).*")) {
				blindingProse = output;
				System.out.println("2. Blinding -- " + blindingProse);
			} else if (output.matches("Duration:.*")) {
				durationProse = output;
				System.out.println("3. Duration -- " + durationProse);
			} else if (output.matches("Design:.*")) {
				designProse = output;
				System.out.println("4. Design -- " + designProse);
			} else if (output.matches("(Loss:).*|(Lost\\sto\\follow[-\\s]up:).*")) {//to add the lost to follow up one
				lostToFollowUpProse = output;
				System.out.println("5. Loss Follow-up -- " + lostToFollowUpProse);
			} else if (output.matches("(?<!Lost\\sto\\s)Follow[-\\s]up:.*")) {
				followUpProse = output;
			} else if (output.matches("Consent:.*")) {
				consentProse = output;
				System.out.println("6. Consent -- " + consentProse);
			} else if (output.matches("Setting:.*")) {
				settingProse = output;
				System.out.println("7. Setting -- " + settingProse);
			} else if (output.matches("Locations?:.*")) {
				locationProse = output;
				System.out.println("8. Location -- " + locationProse);
			} else if (output.matches("Raters?:.*")) {
				ratersProse = output;
			} else if (output.matches("(Countr(y|ies):.*)")) {
				countryProse = output;
			} else if (output.matches("(?=(([Ff]unding:.*)|([Ff]unded\\sby:.*)))|")) {
				fundingProse = output;
			} else {
				otherMethodProse = output;
				System.out.println("OTHER: " + otherMethodProse);
			}
		}
		
	}
	
	private void cleanSetting(String prose){
		 
		 
			 
			 m = emergencyRoom.matcher(prose);//looks for emergency room
			 if (m.find() && outP == false && inP == false && bothP == false){//only allows to take the value emergency room setting if it was not previously specified that patients are in or outpatients
				 trialSetting = SETTING.EMERGENCY;
				 emergencyR = true;
			 }
			 
			 m = psychiatricHospital.matcher(prose); //looks for psychiatric hospital type of trial setting
			 if (m.find() && outP == false && inP == false && bothP == false){
				 trialSetting = SETTING.PSYCHIATRY;
				 psychiatricH = true;
			 }
			 
			 m = hospital.matcher(prose);//looks for hospital setting. If conflicting info came up before (eg if it said inpatient setting before and this string just states hospital name) this pure hospital setting gets lower priority than eg. psychiatric hospital. Otherwise, the mention of "psychiatric hospital" would end up being classified as hospital only
			 if (m.find() && emergencyR  == false && psychiatricH == false && outP == false && inP == false && bothP == false){	//to avoid emergency room information to be overwritten when hospital name appears - emergency rooms are naturally located in hospitals anyway
				 trialSetting = SETTING.HOSPITAL;
				 hospitalSetting = true;
			 }
			 
			 m = community.matcher(prose);//looks for community setting. has lower priority that hospital because hospitals can be named as community hospitals. Community settting therefore applies only as long as hospital boolean is false
			 if (m.find()&& hospitalSetting == false && outP == false && inP == false && bothP == false){
				 trialSetting = SETTING.COMMUNITY;
			 }
			 
			 m = outPatient.matcher(prose);//looks for outpatients
			 if (m.find() || outP == true){	// if a clear reference to in, out, or mixed setting was found before, and this is the nth time this method was called, or because it said things like "hospital inpatients, admitted through emergency room", the following booleans make sure that the right value is re-written to the setting variable.
				 trialSetting = SETTING.OUTPATIENT;
				 outP = true;
			 }
			 
			 m = inPatient.matcher(prose);//looks if this prose is about inpatiens
			 if (m.find() || inP == true ){
				 if (outP == true || bothP == true){	//outP was set true before if outpatients came up
					 trialSetting = SETTING.INANDOUT;
					 bothP = true;
				 } else {
					 trialSetting = SETTING.INPATIENT;//it did not find outpatients, so the population consists only of inpatients
					 inP = true;
				 }
			 }
			 
			 
		 
	 }
	
	 
	private void getCountry(){
		
		
		
		String[] extractedCountries;
		String[] countryList = new String[] {"Afghanistan","Albania","Algeria","Andorra","Angola","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Basutoland","Belarus","Belgium","Belize","Benin","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina Faso","Burma", "Burundi","Cambodia","Cameroon","Canada","Cabo Verde","China", "Central African Republic","Ceylon","Chad","Chile","Colombia","Comoros","Congo","Costa Rica","Cote d'Ivoire","Croatia","Cuba","Curacao","Cyprus","Czechia","Czech Republic","Czechoslovakia","Denmark","Djibouti","Dominica","Dominican Republic","East Germany","East Pakistan","East Timor","Ecuador", "Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia", "Europe", "Fiji","Finland","France","Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada","Guatemala"," Guinea","Guinea-Bissau","Guyana","Haiti","Holy See","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","international","Iran"," Iraq","Ireland","Israel","Italy","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati", "Kosovo","Korea","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia", "Madagascar","Malawi","Malaysia","Maldives","Mali", "Malta","Marshall Islands","Mauritania","Mauritius","Mexico","Micronesia","Moldova", "Monaco","Mongolia","Montenegro","Morocco","Mozambique","multinational", "Myanmar","Namibia","Nauru","Nepal","Netherlands","New Zealand","Nicaragua", "Niger","Nigeria","Norway","Oman","other countries","Pakistan","Palau","Palestinian Territories","Panama","Papua New Guinea","Paraguay","Peru", "Puerto Rico","Philippines","Poland","Portugal","Qatar","Rhodesia","Romania","Rwanda","Russia","Saint Kitts and Nevis","Saint Lucia","Saint Vincent and the Grenadines","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Sikkim","Singapore","Sint Maarten","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Sudan","South Vietnam","Southwest Africa","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Tanganyika","Taiwan","Tajikistan","Tanzania"," Thailand"," Timor-Leste"," Togo"," Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","UK", "Ukraine","UAE","Union of Soviet Socialist Republics","United Arab Emirates","United Arab Republic","United Kingdom","United States","Uruguay","USA","USSR","Uzbekistan","Vanuatu","Venezuela","Vietnam", "Western Samoa","West Germany","Yemen","Yugoslavia","Zaire","Zambia","Zanzibar","Zimbabwe"};
		
		
		for (int i = 0; i < countryList.length; i++){//country info pops up in these strings, therefore they are checked for ocurrence of any country in the world, and for countries that existed after ww2
			
			if (locationProse.contains(countryList[i])){
				countries = countries + countryList[i] + ", ";
				
			} else if (countryProse.contains(countryList[i])){
				countries = countries + countryList[i] + ", ";
				
			} else if (settingProse.contains(countryList[i])) {
				countries = countries + countryList[i] + ", ";
				
			} else if (designProse.contains(countryList[i])) {
				countries = countries + countryList[i] + ", ";
			}
		}
		
		Pattern pattern = Pattern.compile("(((\\d+)|(one)|(two)|(three)|(four)|(five)|(six)|(seven)|(eight)|(nine)|(ten))\\scountries)");//it can possibly alse say "x countries" instead of actual country names, so this is covered below. If the original says "4 countries", this will appear in cleaned version as well
		
		m = pattern.matcher(locationProse);
		if (m.find()) {
			countries = countries + m.group(1) + ", ";
		} else {
			m = pattern.matcher(countryProse);
			if (m.find()) {
				countries = countries + m.group(1) + ", ";
			} else {
				m = pattern.matcher(settingProse);
				if (m.find()) {
					countries = countries + m.group(1) + ", ";
				} else {
					m = pattern.matcher(designProse);
					if (m.find()) {
						countries = countries + m.group(1) + ", ";
					}
				}
			}
		}
		
		
		
		
	countries = countries.trim().replaceAll(",$", "");
	extractedCountries = countries.split("(,\\s)");
	if (extractedCountries.length > 1){
		meerKatCountry = "Multi-Center";
		for (int i = 0; i < extractedCountries.length; i++){
			meerKatCountry = meerKatCountry + "//" + extractedCountries[i];
		}
	} else {
		meerKatCountry = countries;
	}
		
	
	}
	 
	private void referenceExtracting(){
		//This following for-loop runs through all references for the included study and saves relevant fields into an array that is created to hold information in the following positions:
		//0th,  ..index -> Type of publication, eg. Journal ->"TYPE"
		//1st..., ..index -> Primary attribute: Yes or No ->"PRIMARY"
		//2nd... ->Names of all authors of this publication ->"AU"
		//3rd...->Title of publication->"TI"
		//4th...->Name of Journal->Journal->"SO"
		//5...-> Year published->"YR"
		//6...->Volume ->"VL" 
		//7...-> Issue ->"NO"
		//8...-> Pages ->"PG"
		
		//9: TO original name
		//10: EN: edition
		//11: ED Editor
		//12: PB Publisher
		//13: CY City
		//14: MD medium
		
		//if a field is not available, eg. if the reference refers to a conference protocol that lacks page numbers, empty "" space is inserted and next position of array is tried to be filled
	
		
		referencesList = studyElement.getElementsByTagName("REFERENCE");
		int numberReferences = referencesList.getLength();
		references = new String[numberReferences * 15];
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
				e4.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element tiElement = null;
			try {
				NodeList tiList = referenceElement.getElementsByTagName("TI");
				Node tiNode = tiList.item(0);
				tiElement = (Element) tiNode;
			} catch (Exception e4) {
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
				e3.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element soElement = null;
			try {
				NodeList soList = referenceElement.getElementsByTagName("SO");
				Node soNode = soList.item(0);
				soElement = (Element) soNode;
			} catch (Exception e3) {
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
				e2.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element yrElement = null;
			try {
				NodeList yrList = referenceElement.getElementsByTagName("YR");
				Node yrNode = yrList.item(0);
				yrElement = (Element) yrNode;
			} catch (Exception e2) {
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
				e1.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element vlElement = null;
			try {
				NodeList vlList = referenceElement.getElementsByTagName("VL");
				Node vlNode = vlList.item(0);
				vlElement = (Element) vlNode;
			} catch (Exception e1) {
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
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element noElement = null;
			try {
				NodeList noList = referenceElement.getElementsByTagName("NO");
				Node noNode = noList.item(0);
				noElement = (Element) noNode;
			} catch (Exception e1) {
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
				e.printStackTrace();
			}
			
			arrayCounter++;
			
			Element pgElement = null;
			try {
				NodeList pgList = referenceElement.getElementsByTagName("PG");
				Node pgNode = pgList.item(0);
				pgElement = (Element) pgNode;
			} catch (Exception e1) {
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
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element toElement = null;
			try {
				NodeList toList = referenceElement.getElementsByTagName("TO");
				Node toNode = toList.item(0);
				toElement = (Element) toNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (toElement != null){
				references[arrayCounter] = toElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element enElement = null;
			try {
				NodeList enList = referenceElement.getElementsByTagName("EN");
				Node enNode = enList.item(0);
				enElement = (Element) enNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (enElement != null){
				references[arrayCounter] = enElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element edElement = null;
			try {
				NodeList edList = referenceElement.getElementsByTagName("ED");
				Node edNode = edList.item(0);
				edElement = (Element) edNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (edElement != null){
				references[arrayCounter] = edElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element pbElement = null;
			try {
				NodeList pbList = referenceElement.getElementsByTagName("PB");
				Node pbNode = pbList.item(0);
				pbElement = (Element) pbNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (pbElement != null){
				references[arrayCounter] = pbElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element cyElement = null;
			try {
				NodeList cyList = referenceElement.getElementsByTagName("CY");
				Node cyNode = cyList.item(0);
				cyElement = (Element) cyNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (cyElement != null){
				references[arrayCounter] = cyElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element mdElement = null;
			try {
				NodeList mdList = referenceElement.getElementsByTagName("MD");
				Node mdNode = mdList.item(0);
				mdElement = (Element) mdNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (mdElement != null){
				references[arrayCounter] = mdElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
		}
	}
	
	private void cleanBlindness(String str){//looks which kind of blinding methods were extracted for this trial
		
	
		m = blindness.matcher(str);//checks if this String is about blindness at all
		
		
		boolean doubt = false;
		if (performanceBiasRisk.contains("High")) {//indicates, that authors had doubts about trial methodology
			doubt = true;
		}
		
		
		
			
			
			
			
			m = doubleBlind.matcher(str);//looks for indication that the trial is double-blind
			if (m.find()){
				if (doubt) {
					blindingMethod = BLINDNESS.BIASEDDOUBLE;//review authors had significant doubts about this trial, therefore the value becomes unclear
					blindnessCleaned = blindingMethod.getDescription(); //this recurring line fills the cleaned String that is written into the database. Content of this String is defined in the BLINDNESS enum.
				} else {
					blindingMethod = BLINDNESS.DOUBLE;//the pattern matched so this trial is classified as double-blind
					blindnessCleaned = blindingMethod.getDescription(); //this recurring line fills the cleaned String that is written into the database. Content of this String is defined in the BLINDNESS enum.
				}
				
			} else {//the pattern did not match so other options are explored. Blindness options are checked in order of decreasing likelihood.
				m = singleBlind.matcher(str);
				if (m.find()){
					if (doubt) {
						blindingMethod = BLINDNESS.BIASEDSINGLE;
						blindnessCleaned = blindingMethod.getDescription(); 
					} else {
						blindingMethod = BLINDNESS.SINGLE;
						blindnessCleaned = blindingMethod.getDescription(); 
					}
					
					} else {
						m = openLabel.matcher(str);
						if (m.find()){
							if (doubt) {
								blindingMethod = BLINDNESS.BIASEDOPEN;
								blindnessCleaned = blindingMethod.getDescription(); 
							} else {
								blindingMethod = BLINDNESS.OPENTRIAL;
								blindnessCleaned = blindingMethod.getDescription(); 
							}
							
						} else {
							m = tripleBlind.matcher(str);
							if (m.find()){
								if (doubt) {
									blindingMethod = BLINDNESS.BIASEDTRIPLE;
									blindnessCleaned = blindingMethod.getDescription(); 
								} else {
									blindingMethod = BLINDNESS.TRIPLE;
									blindnessCleaned = blindingMethod.getDescription(); 
								}
								
							} else {
								m = quadrupleBlind.matcher(str);
								if (m.find()){
									if (doubt) {
										blindingMethod = BLINDNESS.BIASEDQUADRUPLE;
										blindnessCleaned = blindingMethod.getDescription(); 
									} else {
										blindingMethod = BLINDNESS.QUADRUPLE;
										blindnessCleaned = blindingMethod.getDescription(); 
									}
									
								} else {
									m = notReported.matcher(str);
									if (m.find()) {
										blindingMethod = BLINDNESS.NOTREPORTED;
										blindnessCleaned = blindingMethod.getDescription(); 
									} else {
										m = unclearBlinding.matcher(str);
										if (m.find()) {
											blindingMethod = BLINDNESS.UNCLEAR;
											blindnessCleaned = blindingMethod.getDescription();
										} else {
											blindingMethod = BLINDNESS.OTHER;
											blindnessCleaned = blindingMethod.getDescription(); 
										}
										
									} 
									
								}
							}
						}
					}
				}
			
		}
		

		
	private void designVerifyer(String str){
		
		//To see if this String contains info on trial design
		m = parallelDesign.matcher(str); 	//Uses regex pattern for identifying parallel trials
		if (m.find()){	//To see if this trial is a parallel trial. Returns true if the trial is parallel
			paralellTrial = true;	//Boolean to store that this trial is parallel
			trialDesign = DESIGN.PARALLEL;
			designProse = str.trim();	//stores intact description of the trial
			
			m = parallelDesignCleaner.matcher(str);	//uses regex pattern for cleaning parellel trial
			designAddedInfo = m.replaceAll("").trim();	//cleans additional info from prose
			m = beginningEnd.matcher(designAddedInfo);	//regex pattern for cleaning beginning/end from non-word characters
			designAddedInfo = m.replaceAll("");	//cleans additional info from prose
			
			if (designAddedInfo.length() != 0)	//capitalises first letter
				designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
			
	//		System.out.println("Design: parallel" + ". PROSE DESIGN: " + designProse);
//			
		} else {
			m = crossoverDesign.matcher(str); 
			if (m.find()){
				crossoverTrial = true;
				trialDesign = DESIGN.CROSSOVER;
				designProse = str.trim();
				//System.out.println("DesignProse: " + designProse);
				m = crossoverDesignCleaner.matcher(str);
				designAddedInfo = m.replaceAll("").trim();
				m = beginningEnd.matcher(designAddedInfo);
			
				designAddedInfo = m.replaceAll("");
				if (designAddedInfo.length() != 0)
					designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
				
	//		System.out.println("Design: Crossover" + ". PROSE DESIGN: " + designProse);
			} else {
				m = factorialDesign.matcher(str);
				if (m.find()){
					factorialTrial = true;
					trialDesign = DESIGN.FACTORIAL;
					designProse = str.trim();
					//System.out.println("DesignProse: " + designProse);
					m = factorialDesignCleaner.matcher(str);
					designAddedInfo = m.replaceAll("").trim();
					m = beginningEnd.matcher(designAddedInfo);
				
					designAddedInfo = m.replaceAll("");
					if (designAddedInfo.length() != 0)
							designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
					
					
	//			System.out.println("Design: factorial" + ". PROSE DESIGN: " + designProse);
				} else {
					otherDesign = true;
					trialDesign = DESIGN.OTHER;
					designProse = str.trim();
					//System.out.println("DesignProse: " + designProse);
					designAddedInfo = "";
				}
			}
		}
		
	}
	
	private String[] biasAnalyser(int index, String description){
		String[] forReturn = {"", ""}; //index 0 will hold choice of bias risk, index 1 will hold judgement/quotes
		
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
					
					m = endPunctuationCleanerA.matcher(forReturn[1]);
					if (m.find())										//Cleaning data: Patterns match when there is no either .!?"' at the very end 
						forReturn[1] = forReturn[1] + ".";				//and while there is also no .!? before the last "'  -> in this case a full stop will be inserted
					m = endPunctuationCleanerB.matcher(forReturn[1]);
					if (m.find())
						forReturn[1] = forReturn[1] + ".";
				}
			} 
		} else {
			if (breakBiasVerification > (index + 1)){ 	// breakBiasVerification contains amount of nodes that can be analysed
				return biasAnalyser((index + 1), description); 	// as long as there are more nodes to be analysed the recursion is active
			} else {
				return new String[] {"Information could not be retrieved", "Information could not be retrieved"};	//no more nodes, possibly the description String has to be changed or the bias item is not there
			}
		}
		return forReturn;
	}
	
	public String getMainAuthor() {
		return mainAuthor;
	}



	public void setMainAuthor(String mainAuthor) {
		this.mainAuthor = mainAuthor;
	}



	public int getYear() {
		return year;
	}



	public void setYear(int year) {
		this.year = year;
	}



	public String getAuthorYearLetter() {
		return aauthorYearLetter;
	}



	public void setAuthorYearLetter(String authorYearLetter) {
		this.aauthorYearLetter = authorYearLetter;
	}



	

	public String getRevManID() {
		return revManID;
	}



	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}


@XmlElement
	public String[] getReferences() {
		return references;
	}



	public void setReferences(String[] references) {
		this.references = references;
	}



	



	public String getDesignProse() {
		return designProse;
	}



	public void setDesignProse(String designProse) {
		this.designProse = designProse;
	}



	public String getDesignAddedInfo() {
		return designAddedInfo;
	}



	public void setDesignAddedInfo(String designAddedInfo) {
		this.designAddedInfo = designAddedInfo;
	}



	public String getCountries() {
		return countries;
	}



	public void setCountries(String countries) {
		this.countries = countries;
	}



	public String getSelectionBiasRandomSequenceJudgement() {
		return selectionBiasRandomSequenceJudgement;
	}



	public void setSelectionBiasRandomSequenceJudgement(String selectionBiasRandomSequenceJudgement) {
		this.selectionBiasRandomSequenceJudgement = selectionBiasRandomSequenceJudgement;
	}



	public String getSelectionBiasRandomSequenceBiasRisk() {
		return selectionBiasRandomSequenceBiasRisk;
	}



	public void setSelectionBiasRandomSequenceBiasRisk(String selectionBiasRandomSequenceBiasRisk) {
		this.selectionBiasRandomSequenceBiasRisk = selectionBiasRandomSequenceBiasRisk;
	}



	public String getSelectionBiasAllocationConcealmentBiasRisk() {
		return selectionBiasAllocationConcealmentBiasRisk;
	}



	public void setSelectionBiasAllocationConcealmentBiasRisk(String selectionBiasAllocationConcealmentBiasRisk) {
		this.selectionBiasAllocationConcealmentBiasRisk = selectionBiasAllocationConcealmentBiasRisk;
	}



	public String getSelectionBiasAllocationConcealmentJudgement() {
		return selectionBiasAllocationConcealmentJudgement;
	}



	public void setSelectionBiasAllocationConcealmentJudgement(String selectionBiasAllocationConcealmentJudgement) {
		this.selectionBiasAllocationConcealmentJudgement = selectionBiasAllocationConcealmentJudgement;
	}



	public String getPerformanceBiasRisk() {
		return performanceBiasRisk;
	}



	public void setPerformanceBiasRisk(String performanceBiasRisk) {
		this.performanceBiasRisk = performanceBiasRisk;
	}



	public String getPerformanceBiasJudgement() {
		return performanceBiasJudgement;
	}



	public void setPerformanceBiasJudgement(String performanceBiasJudgement) {
		this.performanceBiasJudgement = performanceBiasJudgement;
	}



	public String getDetectionBiasRisk() {
		return detectionBiasRisk;
	}



	public void setDetectionBiasRisk(String detectionBiasRisk) {
		this.detectionBiasRisk = detectionBiasRisk;
	}



	public String getDetectionBiasJudgement() {
		return detectionBiasJudgement;
	}



	public void setDetectionBiasJudgement(String detectionBiasJudgement) {
		this.detectionBiasJudgement = detectionBiasJudgement;
	}



	public String getAttritionBiasRisk() {
		return attritionBiasRisk;
	}



	public void setAttritionBiasRisk(String attritionBiasRisk) {
		this.attritionBiasRisk = attritionBiasRisk;
	}



	public String getAttritionBiasJudgement() {
		return attritionBiasJudgement;
	}



	public void setAttritionBiasJudgement(String attritionBiasJudgement) {
		this.attritionBiasJudgement = attritionBiasJudgement;
	}



	public String getReportingBiasRisk() {
		return reportingBiasRisk;
	}



	public void setReportingBiasRisk(String reportingBiasRisk) {
		this.reportingBiasRisk = reportingBiasRisk;
	}



	public String getReportingBiasJudgement() {
		return reportingBiasJudgement;
	}



	public void setReportingBiasJudgement(String reportingBiasJudgement) {
		this.reportingBiasJudgement = reportingBiasJudgement;
	}



	public String getOtherBiasRisk() {
		return otherBiasRisk;
	}



	public void setOtherBiasRisk(String otherBiasRisk) {
		this.otherBiasRisk = otherBiasRisk;
	}



	public String getOtherBiasJudgement() {
		return otherBiasJudgement;
	}



	public void setOtherBiasJudgement(String otherBiasJudgement) {
		this.otherBiasJudgement = otherBiasJudgement;
	}



	



	public String getMeerKatCountry() {
		return meerKatCountry;
	}



	public void setMeerKatCountry(String meerKatCountry) {
		this.meerKatCountry = meerKatCountry;
	}


	public void setOutcomeList(List<OutcomeObject> outcomeList) {
		this.outcomeList = outcomeList;
	}
/////////////////////////////////complicated ones

	public ReferenceObject getRefObject() {
		return refObject;
	}


	public void setRefObject(ReferenceObject refObject) {
		this.refObject = refObject;
	}
	
	
	public List<ReferenceObject> getReferenceList() {
		return referenceList;
	}


	public void setReferenceList(List<ReferenceObject> referenceList) {
		this.referenceList = referenceList;
	}


	public ContinuousOutcomeObject getCobj() {
		return cobj;
	}


	public void setCobj(ContinuousOutcomeObject cobj) {
		this.cobj = cobj;
	}


	public DichotomousOutcomeObject getDobj() {
		return dobj;
	}

	public void setDobj(DichotomousOutcomeObject dobj) {
		this.dobj = dobj;
	}

	protected List<OutcomeObject> outcomeList = new ArrayList<>();///array list that will contain all outcomes and their data
	
	@XmlElement(name = "OUTCOME")
	public List<OutcomeObject> getOutcomeList() {
		return outcomeList;
	}
	public SETTING getSetting() {
		return trialSetting;
	}


	public void setSetting(SETTING setting) {
		this.trialSetting = setting;
	}
	public DESIGN getTrialDesign() {
		return trialDesign;
	}


	public void setTrialDesign(DESIGN trialDesign) {
		this.trialDesign = trialDesign;
	}
	public BLINDNESS getBlindingMethod() {
		return blindingMethod;
	}


	public void setBlindingMethod(BLINDNESS blindingMethod) {
		this.blindingMethod = blindingMethod;
	}
	public String getCountryProse() {
		return countryCombined;
	}

	public void setCountryProse(String countryProse) {
		this.countryCombined = countryProse;
	}
	
	public String getSettingProse() {
		return settingProse;
	}

	public void setSettingProse(String settingProse) {
		this.settingProse = settingProse;
	}
	public String getBlindingProse() {
		return blindingProse;
	}

	public void setBlindingProse(String blindingProse) {
		this.blindingProse = blindingProse;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getBlindnessCleaned() {
		return blindnessCleaned;
	}

	public void setBlindnessCleaned(String blindnessCleaned) {
		this.blindnessCleaned = blindnessCleaned;
	}

	public String getRatersProse() {
		return ratersProse;
	}

	public void setRatersProse(String ratersProse) {
		this.ratersProse = ratersProse;
	}

	public String getAllocationProse() {
		return allocationProse;
	}

	public void setAllocationProse(String allocationProse) {
		this.allocationProse = allocationProse;
	}

	public String getDurationProse() {
		return durationProse;
	}

	public void setDurationProse(String durationProse) {
		this.durationProse = durationProse;
	}

	public String getLostToFollowUpProse() {
		return lostToFollowUpProse;
	}

	public void setLostToFollowUpProse(String lostToFollowUpProse) {
		this.lostToFollowUpProse = lostToFollowUpProse;
	}

	public String getConsentProse() {
		return consentProse;
	}

	public void setConsentProse(String consentProse) {
		this.consentProse = consentProse;
	}

	public String getLocationProse() {
		return locationProse;
	}

	public void setLocationProse(String locationProse) {
		this.locationProse = locationProse;
	}
	public String getSettingCombined() {
		return settingCombined;
	}

	public void setSettingCombined(String settingCombined) {
		this.settingCombined = settingCombined;
	}
	public String getParticipantString() {
		return participantString;
	}

	public void setParticipantString(String participantString) {
		this.participantString = participantString;
	}

	public String getMethodString() {
		return methodString;
	}

	public void setMethodString(String methodString) {
		this.methodString = methodString;
	}
	
	public String getFundingProse() {
		return fundingProse;
	}

	public void setFundingProse(String fundingProse) {
		this.fundingProse = fundingProse;
	}

	public String getOtherMethodProse() {
		return otherMethodProse;
	}

	public void setOtherMethodProse(String otherMethodProse) {
		this.otherMethodProse = otherMethodProse;
	}

	public String getOtherParticipantProse() {
		return otherParticipantProse;
	}

	public void setOtherParticipantProse(String otherParticipantProse) {
		this.otherParticipantProse = otherParticipantProse;
	}
}
