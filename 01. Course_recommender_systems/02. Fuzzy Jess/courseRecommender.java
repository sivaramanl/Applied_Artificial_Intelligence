import nrc.fuzzy.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class courseInfo {
	String courseName;
	String courseDomain;
	int courseDuration;

	courseInfo(String name, String domainName, int duration) {
		courseName = name;
		courseDomain = domainName;
		courseDuration = duration;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getCourseDomain() {
		return courseDomain;
	}

	public int getCourseDuration() {
		return courseDuration;
	}
}

class courseInfoGlobal {
	static LinkedHashMap domainCourseMap;

	static {
		domainCourseMap = new LinkedHashMap<String, ArrayList<courseInfo>>();

		ArrayList mathList = new ArrayList();
		mathList.add(new courseInfo("Logic", "Math", 2));
		mathList.add(new courseInfo("Statistics", "Math", 6));
		domainCourseMap.put("Math", mathList);
		
		ArrayList dsList = new ArrayList();
		dsList.add(new courseInfo("Data Analysis", "Data Science", 4));
		dsList.add(new courseInfo("Machine Learning", "Data Science", 6));
		dsList.add(new courseInfo("Probability and Statistics", "Data Science", 3));
		domainCourseMap.put("Data Science", dsList);
		
		ArrayList csList = new ArrayList();
		csList.add(new courseInfo("Software Development", "Computer Science", 12));
		csList.add(new courseInfo("Mobile Development", "Computer Science", 8));
		csList.add(new courseInfo("Web Development", "Computer Science", 10));
		csList.add(new courseInfo("Algorithms", "Computer Science", 4));
		csList.add(new courseInfo("Design", "Computer Science", 6));
		domainCourseMap.put("Computer Science", csList);
	}

	public LinkedHashMap getDomainCourseMap() {
		return domainCourseMap;
	}
}

class userProfile {
	String name;
	int age;
	String currentDomain;
	int experienceInYears;
	boolean changeDomain;
	ArrayList skills;
	int availableDurationInMonths;
	courseInfoGlobal crsObj;
	static BufferedReader br;

	static {
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	public userProfile() {
		changeDomain = false;
		crsObj = new courseInfoGlobal();
		skills = new ArrayList();
	}

	public String getUserName() {
		return name;
	}

	private void setUserName() throws IOException {
		System.out.println("Enter the user name:");
		name = br.readLine();
	}

	public int getUserAge() {
		return age;
	}

	private void setUserAge() throws IOException {
		System.out.println("\nEnter the user age:");
		try {
			age = Integer.parseInt(br.readLine());
		} catch (NumberFormatException ne) {
			System.out.println("Incorrect value provided for age. Please enter correct value.");
			setUserAge();
		}
		if (age < 1 || age > 110) {
			System.out.println("Incorrect value provided for age. Please enter correct value.");
			setUserAge();
		}
	}

	public int getExperienceInYears() {
		return experienceInYears;
	}

	private void setExperienceInYears() throws IOException {
		System.out.println("\nEnter the experience (in years) in the selected domain:");
		try {
			experienceInYears = Integer.parseInt(br.readLine());
		} catch (NumberFormatException ne) {
			System.out.println("Incorrect value provided.. Please enter correct value.");
			setExperienceInYears();
		}
		if(experienceInYears == 0) {
			System.out.println("Please enter a value greater than 0.");
			setExperienceInYears();
		}
		else if (experienceInYears < 0 || experienceInYears > age) {
			System.out.println("Experience value doesn't correspond to the given age. Please enter correct value.");
			setExperienceInYears();
		}
	}

	public int getAvailableDurationInMonths() {
		return availableDurationInMonths;
	}

	private void setAvailableDurationInMonths() throws IOException {
		System.out.println("\nEnter the duration (in months) you are willing to commit for the courses:");
		try {
			availableDurationInMonths = Integer.parseInt(br.readLine());
		} catch (NumberFormatException ne) {
			System.out.println("Incorrect value provided. Please enter correct value.");
			setAvailableDurationInMonths();
		}
		if(availableDurationInMonths == 0) {
			System.out.println("Please enter a value greater than 0.");
			setAvailableDurationInMonths();
		}
		else if (availableDurationInMonths < 0) {
			System.out.println("Incorrect value provided. Please enter correct value.");
			setAvailableDurationInMonths();
		}
		else if(availableDurationInMonths > 24) {
			availableDurationInMonths = 24;
		}
	}

	public String getUserCurrentDomain() {
		return currentDomain;
	}

	public ArrayList getUserSkills() {
		return skills;
	}
	
	private void setUserCurrentDomainSkills() throws IOException{
		setUserCurrentDomainSkills(false);
	}

	private void setUserCurrentDomainSkills(boolean setSkills) throws IOException {
		LinkedHashMap domainCourseMap = crsObj.getDomainCourseMap();
		Iterator it = domainCourseMap.keySet().iterator();
		boolean toContinue = true;
		int userDomainChoice = -1, cntr;
		do {
			System.out.println("\nThe following domains are available currently.");
			cntr = 1;
			while (it.hasNext()) {
				System.out.println(cntr + ". " + it.next());
				cntr++;
			}
			System.out.println("Enter the number corresponding to your domain.");
			try {
				userDomainChoice = Integer.parseInt(br.readLine());
				if (userDomainChoice < 1 || userDomainChoice > domainCourseMap.size()) {
					System.out.println("Please enter valid value.");
				} else {
					toContinue = false;
				}
			} catch (NumberFormatException ne) {
				System.out.println("Please enter valid value.");
			}
		} while (toContinue == true);
		
		it = domainCourseMap.keySet().iterator();
		cntr = 0;
		while (it.hasNext()) {
			cntr++;
			String curDomain = (String) it.next();
			if (cntr == userDomainChoice) {
				currentDomain = curDomain;
				break;
			}
		}

		if(setSkills) {
			toContinue = true;
			ArrayList courseList = (ArrayList) domainCourseMap.get(currentDomain);
			do {
				System.out.println("The following skills are available currently in your selected domain.");
				cntr = 1;
				while (cntr <= courseList.size()) {
					System.out.println(cntr + ". " + ((courseInfo) courseList.get(cntr - 1)).getCourseName());
					cntr++;
				}
				System.out.println("Please enter the number corresponding to your current skill: ");
				try {
					int userSkillChoice = Integer.parseInt(br.readLine());
					if (userSkillChoice < 1 || userSkillChoice > courseList.size()) {
						System.out.println("Please enter valid value.");
					} else {
						skills.add(((courseInfo) courseList.get(userSkillChoice - 1)).getCourseName());
					}
					if (skills.size() == courseList.size()) {
						toContinue = false;
					} else {
						System.out.println("Do you wish to enter another skill (Y/N)?");
						if ("n".equals(br.readLine().toLowerCase())) {
							toContinue = false;
						}
					}
				} catch (NumberFormatException ne) {
					System.out.println("Please enter valid value.");
				}
			} while (toContinue == true);
		}
	}

	public void populateUserProfile() throws Exception {
		setUserName();
		setUserAge();
		setUserCurrentDomainSkills();
		setExperienceInYears();
		setAvailableDurationInMonths();
	}
	
	public void recommendationProvider(String recommendationType) {
		if(recommendationType == null) {
			System.out.println("Sorry " + name + ", we couldn't find any recommendations for you.");
			return;
		}
		ArrayList recommendedDomains = new ArrayList();
		if("negative".equals(recommendationType)) {
			recommendedDomains.add(currentDomain);
		}
		else if("neutral".equals(recommendationType) || "positive".equals(recommendationType)) {
			if("neutral".equals(recommendationType)) {
				recommendedDomains.add(currentDomain);
			}
			
			if("Math".equals(currentDomain)) {
				recommendedDomains.add("Data Science");
			}
			else if("Data Science".equals(currentDomain)) {
				recommendedDomains.add("Computer Science");
			}
			else if("Computer Science".equals(currentDomain)) {
				recommendedDomains.add("Data Science");
			}
		}
		
		System.out.println("\nCongratulations " + name + "! We recommend the following courses to be suitable for your career growth!");
		char c = 'a';
		LinkedHashMap domainCourseMap = crsObj.getDomainCourseMap();
		Iterator it = domainCourseMap.keySet().iterator();
		while(it.hasNext()) {
			String curDomain = (String) it.next();
			if(recommendedDomains.contains(curDomain)) {
				System.out.println("\n" + c++ + ". Domain: " + curDomain);
				ArrayList courseList = (ArrayList) domainCourseMap.get(curDomain);
				for(int i=0; i<courseList.size(); i++) {
					System.out.println((i+1) + ". " + ((courseInfo) courseList.get(i)).getCourseName());
				}
			}
		}
	}
	
	public void recommendationProvider() {
		recommendationProvider(null);
	}
}

public class courseRecommender {
	public static void main(String[] args) throws FuzzyException {
		userProfile person = new userProfile();
		try {
			person.populateUserProfile();
		} catch (Exception e) {
			System.err.println("Unexpected exception occurred! Please execute the application again.");
			e.printStackTrace();
			System.exit(1);
		}

		// Fuzzy definitions BEGIN

		FuzzyValue.setConfineFuzzySetsToUOD(true);

		FuzzyVariable ageMidLife = new FuzzyVariable("ageT", 0.0, 10.0, "0 - Teens, 10 - Old");
		ageMidLife.addTerm("twenties_and_less", new GaussianFuzzySet(1.0, 0.5, 2.0, 0.5));
		ageMidLife.addTerm("defining_decade", new GaussianFuzzySet(4.5, 0.25, 6.0, 0.15));
		ageMidLife.addTerm("forties", new GaussianFuzzySet(7.0, 0.25, 7.5, 0.45));
		ageMidLife.addTerm("older", new GaussianFuzzySet(8.0, 0.25, 9.0, 0.05));

		FuzzyVariable experienceMeasure = new FuzzyVariable("expY", 0.0, 10.0, "0 - Inexperienced, 10 - very experienced");
		experienceMeasure.addTerm("light", new GaussianFuzzySet(1.0, 0.5, 2.0, 0.5));
		experienceMeasure.addTerm("moderate", new GaussianFuzzySet(4.0, 0.15, 7.0, 0.2));
		experienceMeasure.addTerm("high", new GaussianFuzzySet(8.5, 0.35, 9.5, 0.99));
		
		FuzzyVariable availableDuration = new FuzzyVariable("durT", 0.0, 10.0, "0 - very less, 10 - more time");
		availableDuration.addTerm("less", new GaussianFuzzySet(1.0, 0.5, 2.0, 0.5));
		availableDuration.addTerm("good", new GaussianFuzzySet(4.5, 0.25, 6.0, 0.25));
		availableDuration.addTerm("more", new GaussianFuzzySet(8.0, 0.75, 9.0, 0.35));
		
		FuzzyVariable recommendation = new FuzzyVariable("change", 0.0, 10.0, "0 - no/negative change, 10 - positive change");
		recommendation.addTerm("negative", new TrapezoidFuzzySet(0.8, 1.2, 1.8, 2.5));
		recommendation.addTerm("neutral", new TrapezoidFuzzySet(4.0, 4.5, 5.5, 6.5));
		recommendation.addTerm("positive", new SFuzzySet(7.0, 9.5));
		
		FuzzyValue finalRecommendation = null;
		FuzzyValueVector recommendationVector = null;
		
		//Rule definitions BEGIN
		
		FuzzyRule[] ruleAccumulator = new FuzzyRule[36];
		int counter = 0;
		
		//Rule 1
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 2
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 3
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 4
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 5
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 6
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 7
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 8
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 9
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "twenties_and_less"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 10
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 11
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 12
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 13
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 14
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 15
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 16
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 17
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 18
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "defining_decade"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 19
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 20
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 21
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 22
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 23
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 24
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 25
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 26
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "forties"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 27
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 28
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 29
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 30
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "light"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 31
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 32
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "negative"));
		counter++;
		
		//Rule 33
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "moderate"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule 34
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "less"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 35
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "good"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "positive"));
		counter++;
		
		//Rule 36
		ruleAccumulator[counter] = new FuzzyRule();
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(ageMidLife, "older"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(experienceMeasure, "high"));
		ruleAccumulator[counter].addAntecedent(new FuzzyValue(availableDuration, "more"));
		ruleAccumulator[counter].addConclusion(new FuzzyValue(recommendation, "neutral"));
		counter++;
		
		//Rule definitions END
		 
		// Fuzzy definitions END
		
		int curAge = person.getUserAge();
		double leftCurvePoint = curAge / 10.0 < 1 ? curAge / 10.0 : curAge / 10.0 - 1;
		double rightCurvePoint = curAge > 100 ? 10.0 : curAge / 10.0 > 9 ? curAge / 10.0 : curAge / 10.0 + 1;
		FuzzyValue userAge = new FuzzyValue(ageMidLife, new GaussianFuzzySet(leftCurvePoint, 0.25, rightCurvePoint, 0.25));
		int curExperience = person.getExperienceInYears();
		leftCurvePoint = curExperience / 3.0 < 1 ? curExperience / 3.0 : curExperience / 3.0 - 1;
		rightCurvePoint = curExperience > 30 ? 10.0 : curExperience / 3.0 > 9 ? curExperience / 3.0 : curExperience / 3.0 + 1;
		FuzzyValue userExp = new FuzzyValue(experienceMeasure, new GaussianFuzzySet(leftCurvePoint, 0.5, rightCurvePoint, 0.25));
		int curAvailableDuration = person.getAvailableDurationInMonths();
		leftCurvePoint = curAvailableDuration / 2.4 < 1 ? curAvailableDuration / 2.4 : curAvailableDuration / 2.4 - 1;
		rightCurvePoint = curAvailableDuration > 24 ? 9.5 : curAvailableDuration / 2.4 > 9 ? curAvailableDuration / 2.4 : curAvailableDuration / 2.4 + 1;
		FuzzyValue userAvailableDuration = new FuzzyValue(availableDuration, new GaussianFuzzySet(leftCurvePoint, 0.25, rightCurvePoint, 0.5));
		
		for(int i=0; i<counter; i++) {
			ruleAccumulator[i].removeAllInputs();
		}
		
		for(int i=0; i<counter; i++) {
			ruleAccumulator[i].addInput(userAge);
			ruleAccumulator[i].addInput(userExp);
			ruleAccumulator[i].addInput(userAvailableDuration);
			
			if(ruleAccumulator[i].testRuleMatching()) {
				recommendationVector = ruleAccumulator[i].execute();
				if(finalRecommendation == null) {
					finalRecommendation = recommendationVector.fuzzyValueAt(0);
				}
				else {
					finalRecommendation = finalRecommendation.fuzzyUnion(recommendationVector.fuzzyValueAt(0));
				}
			}
		}
		
		if(finalRecommendation != null) {
			double recommendationFuzzyValue = finalRecommendation.momentDefuzzify();
			
			//Recommendation defuzzified values legend
			//0-3: Change in domain not recommended
			//3-7: Both current and new domains are to be recommended
			//7-10: New domain is actively recommended
			
			if(recommendationFuzzyValue > 0 && recommendationFuzzyValue < 3) {
				person.recommendationProvider("negative");
			}
			else if(recommendationFuzzyValue >= 3 && recommendationFuzzyValue <= 6.5) {
				person.recommendationProvider("neutral");
			}
			else if(recommendationFuzzyValue > 6.5 && recommendationFuzzyValue <= 10) {
				person.recommendationProvider("positive");
			}
			else {
				person.recommendationProvider();
			}
		}
		else {
			person.recommendationProvider();
		}
	}
}
