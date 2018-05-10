package trialAndOutcome;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import trialAndOutcome.ALLOCATION;
import trialAndOutcome.BLINDNESS;
import trialAndOutcome.DESIGN;
import trialAndOutcome.SETTING;

public class CharacteristicsObject {
	
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
	
	protected String methodString = "";//holds whole methods paragraph in prose
	protected String participantString = "";//holds whole participants paragraph in prose
	
	private DESIGN trialDesign = DESIGN.NOTAVAILABLE;////variables to do with trial design info
	protected String designCleaned = trialDesign.getContent();
	
	private boolean crossoverTrial = false;
	private boolean paralellTrial = false;
	private boolean factorialTrial = false;
	private boolean otherDesign = false;
	protected String designProse = "";
	protected String designAddedInfo = "";
	
	protected String fundingProse = "";
	protected String powerCalculationProse = "";

	
	protected String otherMethodProse = "";
	protected String otherParticipantProse = "";
	
	protected String countries = "";//////////////to contain country or countries, in alphabetical order. Beware that it will not contain city names!!
	
	protected String meerKatCountry = "";//alphabetical, syntax: Either just "country" or ""Multi-Center//country//country//.."
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
	private ALLOCATION allocation = ALLOCATION.UNCLEAR;
	protected String allocationCleaned = ALLOCATION.UNCLEAR.getContent();
	public String getAllocationCleaned() {
		return allocationCleaned;
	}

	public void setAllocationCleaned(String allocationCleaned) {
		this.allocationCleaned = allocationCleaned;
	}
	protected String additionalAllocationInfo = "";
	
	public String getAdditionalAllocationInfo() {
		return additionalAllocationInfo;
	}

	public void setAdditionalAllocationInfo(String additionalAllocationInfo) {
		this.additionalAllocationInfo = additionalAllocationInfo;
	}

	
	protected String durationProse = "";
	protected String lostToFollowUpProse = "";
	protected String consentProse = "";
	protected String locationProse = "";
	protected String followUpOrAnalysisProse = "";
	
	

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
	protected String phaseProse = "";
	protected String assessmentPointProse = "";
	protected String multicentreProse = "";
	
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

private Pattern doubleBlind = Pattern.compile("(?<!(not|non)[\\s-]?)([Dd]ouble)");
private Pattern singleBlind = Pattern.compile("(?<!(not|non)[\\s-]?)([Ss]ingle)");
private Pattern tripleBlind = Pattern.compile("(?<!(not|non)[\\s-]?)([Tt]riple)");
private Pattern quadrupleBlind = Pattern.compile("(?<!(not|non)[\\s-]?)([Qq]adruple)");
private Pattern openLabel = Pattern.compile("([Oo]pen)|([Nn]ot\\sblind(ed)?)|([Nn]on\\s?-?[Bb]lind(ing)?)|(:\\s?(no|none)\\Z)");
private Pattern unclearBlinding = Pattern.compile("([Nn]ot\\sclear)|([Uu]nclear)");
private Pattern notReported = Pattern.compile("([Nn]ot?\\s(specified|stated|report|reported|indicated|indication|described|description|available|information))");

private Pattern countryPattern = Pattern.compile("(Locations?:)|([Cc]ountr(y|ies):)|([Ss]etting:)");

//patterns for beginning/end of strings
private Pattern beginningEndArray = Pattern.compile("^\\W+|[^\\w)]$"); //All special characters at beginning plus all special characters without closing brackets at end, because of factorial design
private Pattern beginningEnd = Pattern.compile("^\\W+|\\W+$");	//All special chars at beginning and end
private Pattern endPunctuationCleanerA = Pattern.compile("([^.!?\"']$)");	//matches when neither .!?"' at end of sentence. Needs 
private Pattern endPunctuationCleanerB = Pattern.compile("([^!?.][\"'])$");	//to be applied after other punctuation regex. matches when there is no !?. before "'

private Pattern randomisationPattern = Pattern.compile("(?<!(([Nn]on|[Nn]ot|[Nn]o|[Uu]nclear)(\\sdetails?|\\sclear|\\sstate(d|ment)|\\sdescri(bed|ption)|\\sreport(ed)?)?(\\sif|\\swhether|\\sof)?)).([Rr]andom(.+?)?\\b|[Bb]y\\schance|([Tt]oss|[Ss]pin)\\sof(\\sa)?\\scoin)");//recognises if a trial was randomised
private Pattern quasiPattern = Pattern.compile("\\b[Qq]uasi[\\s-]");
	
private NodeList qualityItemList;

protected String revManID = "";
protected String reviewTitle = "";
	

protected CharacteristicsObject() {
	//empty constructor for xml marshalling
}

	protected CharacteristicsObject(Element studyToExtractElement, NodeList qualityItemList, String revManID,String reviewTitle) {
		this.qualityItemList = qualityItemList;
		this.revManID = revManID;
		this.reviewTitle = reviewTitle;
		breakBiasVerification = qualityItemList.getLength();
		
		///////extracts all info on biases that the review author added. The Risk is a three field option (High, low or unclear Risk), judgement of bias includes : justification or quotes from the trials
		String[] biasArray;
		
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
		
		//System.out.println("BIG STRING: " + methodString);
		//methodString = methodString.replace("\\n", "");
		
		splitMethods(methodString);
		
		
		designVerifyer(designProse);
		designCleaned = trialDesign.getContent();
		cleanBlindness(blindingProse);
		allocationCleaner();
		
		
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
		
		revManID = revManID.replaceAll("_x00df_", "�").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&").replaceAll("_x00e8_", "�").replaceAll("_x00f6_", "�").replaceAll("_x00fc_", "�").replaceAll("_x002b_", "+").replaceAll("_x002f_", "/").replaceAll("_x00a0_", " ").replaceAll("_x002c_", ",").replaceAll("_x0028_", "(").replaceAll("_x0029_", ")").replaceAll("_x00e7_", "�").replaceAll("_x0027_", "'").replaceAll("_x002a_", "*").replaceAll("_x00e9_", "�").replaceAll("_x00e4_", "�").replaceAll("_x00b4_", "�");
	
	}
	
private void allocationCleaner() {
		
		m = randomisationPattern.matcher(allocationProse);//the pattern detects if the trial was randomised
		
		if (m.find()) {
			m = quasiPattern.matcher(allocationProse);{//detects if it was quasi-randomised
				if (m.find()) {
					allocation = ALLOCATION.QUASI;
					allocationCleaned = allocation.getContent();//gets cleaned prose from allocation enum
				} else {
					allocation = ALLOCATION.RANDOM;
					allocationCleaned = allocation.getContent();//gets cleaned prose from allocation enum
				}
			}
		}
		String[] additionalAllocationInfoArray;
		
		additionalAllocationInfoArray = allocationProse.split("(?=(,|\\s-\\s|\\(|;))", 2);//by splitting on the specified delimiters, additional information will be transferred into the last index of the array. The String is only split once because sometimes more delimiters occur and by splitting too often sense could get lost. The positive lookahead makes sure that the delimiter character does not get lost.
		
		try {
			additionalAllocationInfo = additionalAllocationInfoArray[1];//additional info is written into its String. If there is no additional info, the exception is caught by leaving the String empty
		} catch (Exception e) {
		}
		
		
	}
	private void splitParticipants (String str) {
		ArrayList<String> storage = new ArrayList<String>();
		String[] splitParticipantParts;
		//location country setting: b is word boundary, d is any digit, s is space char//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////(?(?=Location\\sand\\s[Ss]etting)(?=(Setting\\sand\\s[Ll]ocation\\d?\\s?[:=])|((?=(\\b[sS]etting\\b\\d?\\s?[:=]))|(?=(\\b[Ll]ocations?\\b\\d?\\s?[:=]))))
		splitParticipantParts = str.split("(?=(\\bPhase\\b\\d?\\s?[:=]))|(?=(Consent\\sand\\sethics\\d?\\s?[:=]))|(?=(\\b[Cc]ompleted?\\sstudy\\b:?\\s[Nn]))|(?=(((\\b[Ff]unding\\b)|(\\b[Ff]unded\\sby\\b))\\d?\\s?[:=]))|(?=(\\b[Ll]ocations?\\b\\d?\\s?[:=]))|(?=(\\b[cC]ountr(y|ies)\\b\\d?\\s?[:=]))|(?=(\\b[sS]etting\\b\\d?\\s?[:=]))|(?=(\\bDiagnosis\\b\\d?\\s?[:=]))|(?=(\\bTypes?\\b\\d?\\s?:))|(?=([.]\\s?[^(]?([Tt]otal\\s)?\\b[Nn]\\b\\s?\\d?[:=]))|(?=(([Mm]ean\\s)?[Aa]ge((\\srange)|(,\\syears))?[*\\d]?\\s?[:=]))|(?=((([Ss]ex)|(\\b[Gg]ender))[*\\d]?\\s?[:=]))|(?=(([Mm]edication\\s[Hh]istory\\b\\d?\\s?[:=])))|(?=((?![Mm]edication\\s)\\bHistory\\b\\d?\\s?[:=]))|(?=((([Ee]xcluded)|([Ee]x?cli?usions?(\\scriteria)?))\\d?\\s?[:=]))|(?=((([Ii]ncluded)|([Ii]nclusions?(\\scriteria)?))\\d?\\s?[:=]))|(?=(\\b[Dd]uration\\sof\\s[Ii]llness\\d?\\s?[:=]))|(?=(Average\\slength\\sof\\sillness\\d?\\s?[:=]))|(?=(\\b[Dd]uration\\b\\s\\b[Ii]ll\\b\\d?\\s?[:=]))|(?=(Length\\sof\\sillness\\d?\\s?[:=]))|(?=((\\b[Ee]thnicit(y|ies)\\b\\d?\\s?[:=])|(\\b[Rr]ace\\b\\d?\\s?[:=])))|(?=(\\b[cC]onsent(given)?\\d?\\s?[:=]))|(?=((?<!Lost\\sto\\s)//b[Ff]ollow[-\\s]up\\b\\d?\\s?[:=]))");
		
		for (int j = 0; j < splitParticipantParts.length; j++) {
			
			if (splitParticipantParts[j].startsWith(".")) {//when splitting the big string for number of participants, it happens that a full stop is misplaced.
				splitParticipantParts[j] = splitParticipantParts[j].substring(1);//full stop is deleted from the String by cropping away first character in String
				storage.set(j-1, storage.get(j-1) + ".");//full stop is appended to the right string, which is always the string that was added to storage just before
			}
			
			storage.add(splitParticipantParts[j].trim());
			//System.out.println(splitParticipantParts[j]);
			
		}
		
		String output = "";//to hold current String that is stored in storage Array
		
		for (int i = 0; i < storage.size(); i++) {//iterates through storage to attempt to match Strings
			output = storage.get(i);
			
			
			if (output.matches("\\bDiagnosis\\b\\d?\\s?[:=].*") || output.matches("\\bTypes?\\b\\d?\\s?:.*")) {//since the whole String needs to match, the .* is used as wildcard for the rest of the String
				
				if (diagnosisProse != "") {
					
					diagnosisProse = diagnosisProse + " " + output;//this happens in Becker 1983 and others in the review Antidepressants for people with both schizophrenia and depression, because a field called "Medication history:" comes up. it is added to the standard participant history prose.
				//System.out.println("Diagnosis stuff-- " + diagnosisProse);
				} else {
					diagnosisProse = output;
				}
				
				
				//System.out.println(diagnosisProse);
			} else if (output.matches("\\b[Hh]istory\\b\\d?\\s?[:=].*") || output.matches("[Mm]edication\\s[Hh]istory\\b\\d?\\s?[:=].*")) {
				if (historyProse != "") {
					
					historyProse = historyProse + " " + output;//this happens in Becker 1983 and others in the review Antidepressants for people with both schizophrenia and depression, because a field called "Medication history:" comes up. it is added to the standard participant history prose.
					//System.out.println("history stuff-- " + historyProse);
				} else {
					historyProse = output;
					
				}
				
			} else if (output.matches("([Mm]ean\\s)?[Aa]ge((\\srange)|(,\\syears))?[*\\d]?\\s?[:=].*")) {//the word boundaries before age and sex are deleted because of the possible missing . character after "n"
				if (ageProse != "") {
					
					ageProse = ageProse + " " + output; // antipsychotics childhood schizophrenia, xiaong study has "Age:" and "Mean age:", ocurrences like this are combined here.
					//System.out.println("age stuff-- " + ageProse);
				} else {
					ageProse = output;
				}
				
			} else if (output.matches("(\\b[Gg]ender)[*\\d]?\\s?[:=].*") || output.matches("([Ss]ex)[*\\d]?\\s?[:=].*")) {
				if (genderProse != "") {
					genderProse = genderProse + " " + output;
					//System.out.println("gender stuff-- " + genderProse);
				} else {
					genderProse = output;
				}
				
			} else if (output.matches("(\\b[Ee]xcluded\\b)\\d?\\s?[:=].*") || output.matches("\\b[Ee]xclusion\\scriteria?\\d?\\s?[:=].*") || output.matches("\\b[Ee]xclusions?\\b\\d\\s?[:=].*")) {
				if (excludedProse != "") {
					excludedProse = excludedProse + " " + output;
					//System.out.println("excluded stuff-- " + excludedProse);
				} else {
					excludedProse = output;
				}
				
			} else if (output.matches("(\\b[Ii]ncluded\\b)\\d?\\s?[:=].*") || output.matches("\\b[Ii]nclusion\\scriteria\\d?\\s?[:=].*") || output.matches("\\b[Ii]nclusions?\\b\\d\\s?[:=].*")) {
				if (includedProse != "") {
					includedProse = includedProse + " " + output;
					//System.out.println("included stuff-- " + includedProse);
				} else {
					includedProse = output;
				}
				
			} else if (output.matches("([Tt]otal\\s)?\\b[Nn]\\b\\s?\\d?\\s?[:=].*") || output.matches("\\b[Cc]ompleted?\\s(study\\b)?:?\\s[Nn].*")) {
				if (nProse != "") {
					
					if (output.matches(".*([Tt]otal\\s)?\\b[Nn]\\b\\s?[:=].*")) {
					nProse = nProse +" " + output;
					otherParticipantProse = otherParticipantProse.replace(output, "");
					//System.out.println("nprose stuff--" + nProse);
					//System.out.println("OTHER: " + otherParticipantProse);
					//JOptionPane.showMessageDialog(null, "Beware n " + revManID + ", " + reviewTitle);
					}
				} else {
					nProse = output;
				}
				
			} else if (output.matches("\\b[lL]ocations?\\b\\d?\\s?[:=].*")) {//next 3(location, country, setting) have special treatment because they can appear in methods section also. Sometimes they appear double, so Strings have to be appended to each other
				if (locationProse.equals("")) {//if it is empty, there is no double occurrence, so it can be filled as usual
					locationProse = output;
					
				} else {//it was not empty, so the new found String is appended to old content
					locationProse = locationProse + " " + output;
					//System.out.println("Location stuff-- " + locationProse);
				}
			} else if (output.matches("\\b[cC]ountr(y|ies)\\b\\d?\\s?[:=].*")) {
				if (countryProse.equals("")) {
					countryProse = output;
					
				} else {
					countryProse = countryProse + " " + output;
					//System.out.println("country stuff-- " + countryProse);
				}
			} else if (output.matches("\\b[sS]etting\\b\\d?\\s?[:=].*")) {
				if (settingProse != "") {
					
					settingProse = settingProse + " " + output; //Xu 2007, Yan 2008a, Yang 2006.. in aripiprazole vs other atypials has setting twice, here the Strings are appended.
					//System.out.println("setting stuff-- " + settingProse);
				} else {
					settingProse = output;
				}
					
				
				
				
				
			} else if (output.matches("(?<!Lost\\sto\\s)\\b[Ff]ollow[-\\s]up\\d?\\s?[:=].*")) {
				
				if (followUpOrAnalysisProse.equals("")) {
					followUpOrAnalysisProse = output;
				} else {
					
					followUpOrAnalysisProse = followUpOrAnalysisProse + " " + output;
					//System.out.println("followUp stuff-- " + followUpProse);
				}
				
			} 
			
			else if (output.matches("\\b[cC]onsent(given)?\\d?\\s?[:=].*") || output.matches("Consent\\sand\\sethics\\d?\\s?[:=].*")) {
				if (consentProse != "") {
					consentProse = consentProse + " " + output;
					//System.out.println("consent stuff-- " + consentProse);
				} else {
					consentProse = output;
				}
				
			} else if (output.matches("\\b[Dd]uration\\s[Ii]ll\\d?\\s?[:=].*") || output.matches("\\b[Dd]uration\\sof\\s[Ii]llness\\d?\\s?[:=].*") || output.matches("Average\\slength\\sof\\sillness\\d?\\s?[:=].*") || output.matches("Length\\sof\\sillness\\d?\\s?[:=].*")) {
				if (durationIllProse != "") {
					durationIllProse = durationIllProse + " " + output;
					//System.out.println("durationIll stuff-- " + durationIllProse);
				} else {
					durationIllProse = output;
				}
				
			} else if (output.matches("\\b([Rr]ace)\\b\\d?\\s?[:=].*") || output.matches("\\b([Ee]thnicit(y|ies))\\b\\d?\\s?[:=].*")) {
				if (ethnicityProse != "") {
					ethnicityProse = ethnicityProse + " " + output;
					//System.out.println("ethnicity stuff-- " + ethnicityProse);
				} else {
					ethnicityProse = output;
				}
				
			} else if (output.matches("\\b[Ff]unding\\b\\d?\\s?[:=].*") || output.matches("(\\b[Ff]unded\\sby\\b)\\d?\\s?[:=].*")) {
				if (fundingProse != "") {
					fundingProse = fundingProse + " " + output;
					//System.out.println("funding stuff-- " + fundingProse);
				} else {
					fundingProse = output;
				}
				
			} else if (output.matches("\\bPhase\\b\\d?\\s?[:=].*")){ 
				if (phaseProse != "") {
					phaseProse = phaseProse + " " + output;
					//System.out.println("phase stuff-- " + phaseProse);
				} else {
					phaseProse = output;
				}
			} else {
				otherParticipantProse = output.trim();
				if (otherParticipantProse.contains("Exclusion criteria:") || otherParticipantProse.contains("Exclusion:") || otherParticipantProse.contains("Excluded:") || otherParticipantProse.contains("Exclusions:")) {
					excludedProse = excludedProse + " " + output.trim();
					
				} else if (otherParticipantProse.contains("Inclusion criteria:") || otherParticipantProse.contains("Inclusion:")|| otherParticipantProse.contains("Included:") || otherParticipantProse.contains("Inclusions:")) {
					includedProse = includedProse + " " + output.trim();
					
				} else {
					//if (otherParticipantProse.matches(".*\\w.*"))
					//System.out.println("OTHER participant: " + otherParticipantProse);
				}
				
				
			}
		}
		
		
	}
	
	private void splitMethods (String str) {//Uses positive lookahead regex to split method prose into Strings that are stored in Array and later identified ant written into their respective prose Strings																													//take care here
		ArrayList<String> storage = new ArrayList<String>();//this is used because lists are easier to have variables added to them
		String[] splitMethodParts;//to store all parts of the big methods string that gets broken by the regex
		
		
		//since the first option did not come true, the String is checked for "Follow-up:" only
			
			splitMethodParts = str.split("(?=([pP]ower\\scalculation\\d?\\*?\\s?[:=]))(?=([Pp]laces?\\d?\\*?\\s?[:=]))|(?=((Intention[\\s-]to[\\s-]treat[\\s-])?[Aa]nalysis\\d?\\*?\\s?[:=]))|(?=(Sites?\\*?\\s[A-Za-z0-9]\\d?[=:-]))|(?=(Methods?\\d?\\*?\\s?[:=]))|(?=(Cente?re?\\d?\\*?\\s?[:=]))|(?=(Loss\\d?\\*?\\s?[:=]))|(?=(Assessment\\spoints\\d?\\*?\\s?[=:]))|(?=(Objectivity\\sof\\srating\\sof\\soutcome\\d?\\*?\\s?[:=]))|(?=(([Ff]unding\\d?\\*?\\s?[:=])|(Funded\\sby\\d?\\*?\\s?[:=]?)))|(?=([rR]aters?\\d?\\*?\\s?[:=]))|(?=([aA]ll?ocations?\\d?\\*?\\s?[:=]))|(?=(([Rr]andomi[sz]ed|[Rr]andom(i[sz]ation)?)\\d?\\*?\\s?[:=]))|(?=([Bb]lind(n)?ing\\d?\\*?\\s?[:=])|([Bb]linde?(ed)?n?ess\\d?\\*?\\s?[:=])|(([Dd]ouble|[Ss]ingle|[Tt]riple)[\\s-]?[Bb]lind\\d?\\*?\\s?[:=])|(Blind\\d?\\*?\\s?[:=]))|(?=([Dd]uration\\d?\\*?\\s?[:=]))|(?=([dD]esign\\d?\\*?\\s?[:=]))|(?=(Follow[\\s-]up\\d?\\*?\\s?[:=]))|(?=(Lost\\sto\\sfollow[\\s-]up\\d?\\*?\\s?[:=]))|(?=([lL]oss\\d?\\*?\\s?[:=]))|(?=([cC]onsent\\d?\\*?\\s?[:=]))|(?=(((Locations?)|(Locations?\\sand\\ssetting))\\d?\\*?\\s?[:=]))|(?=([cC]ountr(y|ies)\\d?\\*?\\s?[:=]))|(?=(Settings?\\d?\\*?\\s?[:=]))");
			
			for (int j = 0; j < splitMethodParts.length; j++) {
				storage.add(splitMethodParts[j].trim());
				System.out.println(splitMethodParts[j]);
			}
		
		
		
			
		for (String output: storage) {
			
			if (output.matches("(All?ocations?\\d?\\*?\\s?[:=]).*") || output.matches("([Rr]andomi[sz]ed|[Rr]andom(i[sz]ation)?)\\d?\\*?\\s?[:=].*")) {//the wildcard asterisk is making sure that the whole string matches
			
				allocationProse = addProse(output, allocationProse);//this method reacts to String content status and adds prose;
				System.out.println("1. Allocation -- " + allocationProse);
				
			} else if (output.matches("([Bb]lind(n)?ing\\d?\\*?\\s?[:=]).*")|| output.matches("[Bb]linde?(ed)?n?ess\\d?\\*?\\s?[:=].*") ||output.matches("(([Dd]ouble|[Ss]ingle|[Tt]riple)[\\s-]?[Bb]lind\\d?\\*?\\s?[:=]).*") || output.matches("Blind\\d?\\*?\\s?[:=].*")) {
				
				blindingProse = addProse(output, blindingProse);
				System.out.println("2. Blinding -- " + blindingProse);
				
			} else if (output.matches("[dD]uration\\d?\\*?\\s?[:=].*")) {
				durationProse = addProse(output, durationProse);
				System.out.println("3. Duration -- " + durationProse);
			} else if (output.matches("[dD]esign\\d?\\*?\\s?[:=].*")) {
				designProse = addProse(output, designProse);
				System.out.println("4. Design -- " + designProse);
			} else if (output.matches("Lost\\sto\\sfollow[\\s-]up\\d?\\*?\\s?[:=].*") || output.matches("Follow[\\s-]up\\d?\\*?\\s?[:=].*") || output.matches("Loss\\d?\\*?\\s?[:=].*") || output.matches("(Intention[\\s-]to[\\s-]treat[\\s-])?[Aa]nalysis\\d?\\*?\\s?[:=].*")) {
				followUpOrAnalysisProse = addProse(output, followUpOrAnalysisProse);
				System.out.println("Follow up ----- " + followUpOrAnalysisProse);
			} else if (output.matches("Consent\\d?\\*?\\s?[:=].*")) {
				consentProse = addProse(output, consentProse);
				System.out.println("6. Consent -- " + consentProse);
			} else if (output.matches("Settings?\\d?\\*?\\s?[:=].*")) {
				settingProse = addProse(output, settingProse);
				System.out.println("7. Setting -- " + settingProse);
			} else if (output.matches("Locations?\\d?\\*?\\s?[:=].*") || output.matches("Locations?\\sand\\ssetting\\d?\\*?\\s?:.*") || output.matches("[Pp]laces?\\d?\\*?\\s?[:=]")) {
				locationProse = addProse(output, locationProse);
				if (output.matches("Locations?\\sand\\ssetting\\d?\\*?\\s?[:=].*")) {//because this contains info on setting as well if it comes true
					settingProse = addProse(output, settingProse);
				}
				System.out.println("8. Location -- " + locationProse);
			} else if (output.matches("Raters?\\d?\\*?\\s?[:=].*") || output.matches("Objectivity\\sof\\sratings?\\sof\\soutcomes?\\d?\\*?\\s??[:=].*")) {
				ratersProse = addProse(output, ratersProse);
				
				System.out.println("9. Raters -- " + ratersProse);
			} else if (output.matches("(Countr(y|ies)\\d?\\*?\\s?[:=].*)")) {
				countryProse = addProse(output, countryProse);
				System.out.println("10. Countries -- " + countryProse);
			} else if (output.matches("[Ff]unding\\d?\\*?\\s?[:=].*") || output.matches("Funded\\sby\\d?\\*?\\s?[:=]?.*")) {
				fundingProse = addProse(output, fundingProse);
				System.out.println("11. Funding -- " + fundingProse);
			} else if (output.matches("Assessment\\spoints\\d?\\*?\\s?[=:].*")) {
				assessmentPointProse = addProse(output, assessmentPointProse);
				System.out.println("12. Assessment -- " + assessmentPointProse);
			} else if (output.matches("Methods?\\d?\\*?\\s?[:=].*") || output.matches("Cente?re?\\d?\\*?\\s?[:=].*") || output.matches("Sites?\\s[A-Za-z0-9]\\d?\\*?\\s?[=:-].*")) {
				multicentreProse = addProse(output, multicentreProse);
				//System.out.println("13. multicentre -- " + multicentreProse);
			} else if (output.matches("[pP]ower\\scalculation\\d?\\*?\\s?[:=]")) {
				powerCalculationProse = addProse(output, powerCalculationProse);
				//System.out.println("14. power calc -- " + powerCalculationProse);
			}
			else {
				otherMethodProse = addProse(output, otherMethodProse);
				//System.out.println("OTHER methods: " + otherMethodProse);
			}
		}
		
	}
	private String addProse(String output, String toAddto){
		
		if (toAddto != "") {
			toAddto = toAddto + " " + output;
			return toAddto;
		} else {
			return output;
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
	
	private void checkSpecialCountry() {
		Pattern specialCountries = Pattern.compile("((?<!(North|South)\\s)Korea)|((?<!(East|West)\\s)Germany)|(US\\b)|(Czech\\b)");// special countries that cause duplicates when checked without regex
		String[] countriesStrings = {locationProse, countryProse, settingProse, designProse};//Prose that has to be checked for country info
		
		for (int i = 0; i < countriesStrings.length; i++) {//checks every prose String in the Array
			m = specialCountries.matcher(countriesStrings[i]);//fills matcher with the prose that is to be checked
			if (m.find()) {//if this becomes true, a special country was found. In the following, it will be checked which country it is
				
				if (countriesStrings[i].contains("Germany")) {
					countries = countries + "Germany" + ", ";
				} 
				if (countriesStrings[i].contains("Korea")){
					countries = countries + "Korea" + ", ";
				}
				if (countriesStrings[i].contains("US")){
					countries = countries + "USA" + ", ";//"USA" is added instead of "US"
				}
				if (countriesStrings[i].contains("Czech")){
					countries = countries + "Czech" + ", ";//this is how it appears in MeerKat sometimes. 
				}
				
				if (countryProse.contains(countriesStrings[i]) == false) {
					countryProse = addProse(countriesStrings[i], countryProse);
				}
			}
		}
		
		
	}
	private void getCountry(){
		
		checkSpecialCountry();//countries that can not be identified below without causing duplicates
		
		String[] extractedCountries;
		String[] countryList = new String[] {"Afghanistan","Albania","Algeria","Andorra","Angola","Antigua and Barbuda","Argentina","Armenia","Aruba","Asia", "Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Basutoland","Belarus","Belgium","Belize","Benin","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina Faso","Burma", "Burundi","Cabo Verde","Cambodia","Cameroon","Canada","CAR", "Caribbean", "China", "Central African Republic","Ceylon","Chad","Chile","Colombia","Comoros","Congo","Costa Rica","Cote d'Ivoire","Croatia","Cuba","Curacao","Cyprus","Czechia","Czech Republic","Czechoslovakia","Denmark","Djibouti","Dominica","Dominican Republic","East Germany","East Pakistan","East Timor","Ecuador", "Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia", "Europe", "Fiji","Finland","France","Gabon","Gambia","Georgia","Ghana","Greece","Grenada","Guatemala"," Guinea","Guinea-Bissau","Guyana","Haiti","Holy See","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","international","Iran"," Iraq","Ireland","Israel","Italy","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati", "Kosovo","Korea","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia", "Madagascar","Malawi","Malaysia","Maldives","Mali", "Malta","Marshall Islands","Mauritania","Mauritius","Mexico","Micronesia","Moldova", "Monaco","Mongolia","Montenegro","Morocco","Mozambique","multinational", "Myanmar","Namibia","Nauru","Nepal","Netherlands","New Zealand","Nicaragua", "Niger","Nigeria","North America", "Norway","Oman","other countries","Pakistan","Palau","Palestinian Territories","Panama","Papua New Guinea","Paraguay","Peru", "Puerto Rico","Philippines","Poland","Portugal","Qatar","Rhodesia","Romania","Rwanda","Russia","Saint Kitts and Nevis","Saint Lucia","Saint Vincent and the Grenadines","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Sikkim","Singapore","Sint Maarten","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South America", "South Sudan","South Vietnam","Southwest Africa","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Tanganyika","Taiwan","Tajikistan","Tanzania"," Thailand"," Timor-Leste"," Togo"," Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","UK", "Ukraine","UAE","Union of Soviet Socialist Republics","United Arab Emirates","United Arab Republic","United Kingdom","United States","Uruguay","USA","USSR","Uzbekistan","Vanuatu", "Vatican","Venezuela","Vietnam", "Western Samoa","West Germany","Yemen","Yugoslavia","Zaire","Zambia","Zanzibar","Zimbabwe"};
		
		
		for (int i = 0; i < countryList.length; i++){//country info pops up in these strings, therefore they are checked for ocurrence of any country in the world, and for countries that existed after ww2
			
			if (locationProse.contains(countryList[i])){
				countries = countries + countryList[i] + ", ";//the country that was found is added to a String
				
				if (countryProse.contains(locationProse) == false) {//checks if the prose String is already contained in the big prose
					countryProse = addProse(locationProse, countryProse); //the String that contained the country is added to a big prose String
				}
				
				
			} else if  (countryProse.contains(countryList[i])) {
				countries = countries + countryList[i] + ", ";
				
				
			} else if (settingProse.contains(countryList[i])) {
				countries = countries + countryList[i] + ", ";
				
				if (countryProse.contains(settingProse) == false) {
					countryProse = addProse(settingProse, countryProse);
				}
			} else if (designProse.contains(countryList[i])) {
				countries = countries + countryList[i] + ", ";
				
				if (countryProse.contains(designProse) == false) {
					countryProse = addProse(designProse, countryProse);
				}
			}
			
		}
		
		if (countries.contains("United Kingdom")) {//Some countries in this list appear abbreviated and some are written out in reviews. Mostly, they are abbreviated so here, all the non-abbreviated versions are abbreviated for the sake of consistency
			countries = countries.replaceAll("United Kingdom", "UK");
		} 
		if (countries.contains("United States")) {
			countries = countries.replaceAll("United States", "USA");
		}
		if (countries.contains("Central African Republic")) {
			countries = countries.replaceAll("Central African Republic", "CAR");
		}
		if (countries.contains("multinational")) {
			countries = countries.replaceAll("multinational", "international");
		}
		/*Pattern pattern = Pattern.compile("(((\\d+)|(one)|(two)|(three)|(four)|(five)|(six)|(seven)|(eight)|(nine)|(ten))\\scountries)");//it can possibly alse say "x countries" instead of actual country names, so this is covered below. If the original says "4 countries", this will appear in cleaned version as well
		
		m = pattern.matcher(locationProse);
		if (m.find()) {
			countries = countries + m.group(1) + ", ";
			if (countryProse.contains(locationProse) == false) {
				countryProse = addProse(locationProse, countryProse);
			}
		} else {
			m = pattern.matcher(countryProse);
			if (m.find()) {
				countries = countries + m.group(1) + ", ";
				
			} else {
				m = pattern.matcher(settingProse);
				if (m.find()) {
					countries = countries + m.group(1) + ", ";
					if (countryProse.contains(settingProse) == false) {
						countryProse = addProse(settingProse, countryProse);
					}
				} else {
					m = pattern.matcher(designProse);
					if (m.find()) {
						countries = countries + m.group(1) + ", ";
						if (countryProse.contains(designProse) == false) {
							countryProse = addProse(designProse, countryProse);
						}
					}
				}
			}
		}*/
		
		
		
		
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
			
			m = crossoverDesign.matcher(str);//this is checked now because sometimes trials are described as both parallel and crossover or part/part. Here the checking has to be done by a human
				if (m.find()) {
					trialDesign = DESIGN.OTHER;
				}
			
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
					Pattern pragmaticDesign = Pattern.compile("(([Pp]ragmatic|[Pp]ractical)\\s(clinical\\s)?(study|trial))|PCT|pRCT");
					m = pragmaticDesign.matcher(str);
					if (m.find()){
						trialDesign = DESIGN.PRAGMATIC;
					} else {
						Pattern dosageDesign = Pattern.compile("([Dd]osage\\s(study|trial))|[Dd]ose-ranging\\s(clinical)?(study|trial)");
						m = dosageDesign.matcher(str);
						if (m.find()){
							trialDesign = DESIGN.DOSAGE;
						}  else {
							Pattern longitudinalDesign = Pattern.compile("[Ll]ongitudinal|[Pp]anel\\sstudy");
							m = longitudinalDesign.matcher(str);
							if (m.find()){
								trialDesign = DESIGN.LONGITUDINAL;
							} else {
								Pattern clusterDesign = Pattern.compile("([Cc]luster|[Gg]roup|[Pp]lace)[\\s-]randomi[zs]ed\\s(controlled\\s)?(trial|study)");
								m = clusterDesign.matcher(str);
								if (m.find()) {
									trialDesign = DESIGN.CLUSTER;
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
	
private void cleanBlindness(String str){//looks which kind of blinding methods were extracted for this trial
		
	
		
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
	public String getDiagnosisProse() {
		return diagnosisProse;
	}

	public void setDiagnosisProse(String diagnosisProse) {
		this.diagnosisProse = diagnosisProse;
	}

	public String getHistoryProse() {
		return historyProse;
	}

	public void setHistoryProse(String historyProse) {
		this.historyProse = historyProse;
	}

	public String getAgeProse() {
		return ageProse;
	}

	public void setAgeProse(String ageProse) {
		this.ageProse = ageProse;
	}

	public String getGenderProse() {
		return genderProse;
	}

	public void setGenderProse(String genderProse) {
		this.genderProse = genderProse;
	}

	public String getExcludedProse() {
		return excludedProse;
	}

	public void setExcludedProse(String excludedProse) {
		this.excludedProse = excludedProse;
	}

	public String getIncludedProse() {
		return includedProse;
	}

	public void setIncludedProse(String includedProse) {
		this.includedProse = includedProse;
	}

	public String getDurationIllProse() {
		return durationIllProse;
	}

	public void setDurationIllProse(String durationIllProse) {
		this.durationIllProse = durationIllProse;
	}

	public String getEthnicityProse() {
		return ethnicityProse;
	}

	public void setEthnicityProse(String ethnicityProse) {
		this.ethnicityProse = ethnicityProse;
	}

	public String getnProse() {
		return nProse;
	}

	public void setnProse(String nProse) {
		this.nProse = nProse;
	}
	public String getPhaseProse() {
		return phaseProse;
	}

	public void setPhaseProse(String phaseProse) {
		this.phaseProse = phaseProse;
	}
	public String getAssessmentPointProse() {
		return assessmentPointProse;
	}

	public void setAssessmentPointProse(String assessmentPointProse) {
		this.assessmentPointProse = assessmentPointProse;
	}
	public String getMulticentreProse() {
		return multicentreProse;
	}

	public void setMulticentreProse(String multicentreProse) {
		this.multicentreProse = multicentreProse;
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
	public String getRevManID() {
		return revManID;
	}

	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}
	
	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	public String getDesignCleaned() {
		return designCleaned;
	}

	public void setDesignCleaned(String designCleaned) {
		this.designCleaned = designCleaned;
	}
	
	
	
	public String getFollowUpProse() {
		return followUpOrAnalysisProse;
	}

	public void setFollowUpProse(String followUpProse) {
		this.followUpOrAnalysisProse = followUpProse;
	}
	
	public String getPowerCalculationProse() {
		return powerCalculationProse;
	}

	public void setPowerCalculationProse(String powerCalculationProse) {
		this.powerCalculationProse = powerCalculationProse;
	}
	public String getCountryProse() {
		return countryProse;
	}

	public void setCountryProse(String countryProse) {
		this.countryProse = countryProse;
	}

}

