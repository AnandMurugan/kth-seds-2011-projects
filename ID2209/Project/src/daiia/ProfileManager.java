package daiia;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.myprofile.profile.MuseumItem;
import com.myprofile.profile.ObjectFactory;
import com.myprofile.profile.ProfileType;


public class ProfileManager {
	
	public ProfileType  createProfile() {
		JAXBContext jc;
		Marshaller marshaller;
		
		/*  */
		try {
			ObjectFactory factory=new ObjectFactory();
			//create profile
			ProfileType profile = factory.createProfileType();
			/**
			 * The following sample code shows how you can set the profile content, using the provided java Code
			 */
			/*--------------------------------------------*/
			//initialize basic information
			profile.setAge(33);
			/** only one of : "higher education","postsecondary education","elementary education","secondary education" */
			profile.setEducation("higher education");
			/** only one of: "all",	"adventure","art of culture", "educational", "welfare and relaxing" */
			profile.setMotivationOfVisit("educational");
			/** only use of these values:{ greedy, normal, conservative} */
			profile.setAttitude("greedy");
			profile.setPrivacy(0.5);
			/*--------------------------------------------*/
			//add interests into profile 
			ProfileType.Interests interests = factory.createProfileTypeInterests();
			
			/** choose any value of Subject, Object Type, Material from provided list */
			interests.getInterest().add("astronomy");
			interests.getInterest().add("art");
			// add collection of interests into profile
			profile.setInterests(interests);
			/*--------------------------------------------*/
			
			// create one museum item and initialize it
			MuseumItem  item1 = factory.createMuseumItem();
			item1.setId("urn:imss:instrument:401037");
			item1.setName("Sundial@en");
			item1.setSubject("astronomy");
			item1.setObjectType("sundials");
			item1.setMaterial("brass");
			// use rating 1-5 where 5 is more interesting (relevant) and 1 is the least interested one
			item1.setRating(5);
			
			// create another museum item and initialize it
			MuseumItem  item2 = factory.createMuseumItem();
			item2.setId("urn:imss:instrument:414141");
			item2.setName("Coulomb magnetic declination compass@en");
			item2.setSubject("electrical engineering");
			item2.setObjectType("declinometers");
			item2.setMaterial("marble");
			item1.setRating(4);
			
			// add the initialized museum items into a collection of visited itmes
			ProfileType.VisitedItems visitedItems =  factory.createProfileTypeVisitedItems();
			visitedItems.getVisitedItem().add(item1);
			visitedItems.getVisitedItem().add(item2);
			// add collection of visited items into profile
			profile.setVisitedItems(visitedItems);
			/*--------------------------------------------*/			
			//dump profile into an XML file
			jc = JAXBContext.newInstance("com.myprofile.profile");
			marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			String outputFileName = "Profile.xml";
			marshaller.marshal( profile , new FileOutputStream(outputFileName));
			
			return profile;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public ProfileType loadProfile(String inputFileName ) {
		JAXBContext jc;
		Unmarshaller u;
		
		/* Read data */
		try {
			jc = JAXBContext.newInstance("com.myprofile.profile");
			u = jc.createUnmarshaller();
			//ProfileType profile = (ProfileType)(((JAXBElement)u.unmarshal(new FileInputStream(inputFileName))).getValue());
			JAXBElement<ProfileType> root = u.unmarshal(new StreamSource(new File(inputFileName)),ProfileType.class);
			ProfileType profile = root.getValue();
			
			System.out.println(" Profile content is: ");
			System.out.println(" Age: " + profile.getAge());
			System.out.println(" Education:  " + profile.getEducation());
			System.out.println(" MotivationOfVisit:  " + profile.getMotivationOfVisit());
			
			for (int i = 0; i < profile.getVisitedItems().getVisitedItem().size(); i++) {
				MuseumItem item = profile.getVisitedItems().getVisitedItem().get(i);
				System.out.println("\t Visited Item<" + i + ">: " + item.getId() + ", " + item.getName() );
			}
			for (int i = 0; i < profile.getInterests().getInterest().size(); i++) {
				System.out.println("\t Profile Interest<" + i + ">: " + profile.getInterests().getInterest().get(i) );
			}
			
			return profile;
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
	
        
	public void dumpProfile (ProfileType profile, String outputFilename) {
		JAXBContext jc;
		Marshaller marshaller;
		
		try {
			//dump profile into an XML file
			jc = JAXBContext.newInstance("com.myprofile.profile");
			marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			String outputFileName = outputFilename;
			marshaller.marshal( profile , new FileOutputStream(outputFileName));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}
	
	public static void main (String[]  args) {
		ProfileManager profiler = new ProfileManager();
		profiler.createProfile();
		profiler.loadProfile("Profile.xml");
	}
}
