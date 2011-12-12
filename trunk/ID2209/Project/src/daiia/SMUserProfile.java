package daiia;

import java.util.Vector;

public class SMUserProfile {

	public enum AgeGroup { ADULTS, ELDERLY, YOUTH, BOYS, GIRLS,
		ADOLESCENTS }
	//aat:
	
	public enum LanguageSkill { EN, FR, DE, FI }
	//"language:fi , language:en , language:fr , language:de "
	
	AgeGroup age;
	Vector<LanguageSkill> langSkills = new Vector<LanguageSkill> ();
 
	 public void setAge(AgeGroup age_) {
		 this.age = age_;
	 }
	 public AgeGroup getAge() {
		 return this.age;
	 }
	 
	 public void addLanguage(LanguageSkill lang_) {
		 this.langSkills.add(lang_);
	 }
	 public Vector<LanguageSkill> getLanguage() {
		 return this.langSkills;
	 }
	 
	
	 
}
