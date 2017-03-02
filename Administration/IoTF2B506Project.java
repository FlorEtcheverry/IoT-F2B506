package org.imt.atlantique.sss.kms.init;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.imt.atlantique.sss.kms.entities.LinkModel;
import org.imt.atlantique.sss.kms.entities.LinkQuery;
import org.imt.atlantique.sss.kms.entities.Request;
import org.imt.atlantique.sss.kms.entities.RequestParameter;
import org.imt.atlantique.sss.kms.entities.Sphere;
import org.imt.atlantique.sss.kms.entities.type.CachingStrategyType;

import org.imt.atlantique.sss.kms.entities.type.SharingType;

import org.imt.atlantique.sss.kms.exceptions.RequestExistException;
import org.imt.atlantique.sss.kms.factories.ResourceFactory;
import org.imt.atlantique.sss.kms.factories.type.ResourceCategory;
import org.imt.atlantique.sss.kms.manager.LinkQueryManager;
import org.imt.atlantique.sss.kms.manager.RequestManager;

import org.imt.atlantique.sss.kms.shared.entities.type.ParameterType;
import org.imt.atlantique.sss.kms.shared.entities.type.UseType;

/**
 *
 * @author admin
 */

@Stateless
public class IoTF2B506Project extends AbstractInitSphereDB {

	@EJB
	private RequestManager requestManager;
	@EJB
	private LinkQueryManager linkQueryManager;
	@EJB
	private ResourceFactory ressourceFactory;

	@Override
	protected void buildRequest() throws RequestExistException {
		try {
			System.out.println("********** Build ********");
			//Note: insert InitSmoopleDBWithDemo.java in  line 
			//	<<this.SmoopleFactory.createSmoople("IoTF2B506ProjectProject", "Smoople pour le  IoTF2B506Project Project", true, new Date(), Duration.ofDays(3*365), null);>>
			Sphere smoople = this.instantiateSphere("IoTF2B506ProjectProject");
			this.instantiateAuthor();
			// Referencing ontology
			URL url = new URL("file:///C:/Users/admin/Desktop/P506/Ontology/UV506Gr2.owl"); 
			System.out.println("URL=" + url);
			LinkModel linkModel = (LinkModel) ressourceFactory.createResource(ResourceCategory.MODEL, "xAPI_IoTF2B506Project", "IoTF2B506Project", author, "1.0.1", SharingType.READ, null, url, CachingStrategyType.OFF); //TODO change  


			//***********createDrug***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/createDrug.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery1 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "createDrug","[PLACEHOLDER]", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req1 = new Request("createDrug", "[PLACEHOLDER]", linkQuery1);

			RequestParameter createDrug_DrugParameter = new RequestParameter("Drug", ParameterType.String);
			createDrug_DrugParameter.setDescription("[PLACEHOLDER]");
			createDrug_DrugParameter.setUse(UseType.MANDATORY);
			req1.addParameter(createDrug_DrugParameter);

			requestManager.create(req1);


			//***********createPerson***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/createPerson.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery2 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "createPerson","[PLACEHOLDER]", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req2 = new Request("createPerson", "[PLACEHOLDER]", linkQuery2);

			RequestParameter createPerson_CircleParameter = new RequestParameter("Circle", ParameterType.String);
			createPerson_CircleParameter.setDescription("[PLACEHOLDER]");
			createPerson_CircleParameter.setUse(UseType.MANDATORY);
			req2.addParameter(createPerson_CircleParameter);

			RequestParameter createPerson_UserParameter = new RequestParameter("User", ParameterType.String);
			createPerson_UserParameter.setDescription("[PLACEHOLDER]");
			createPerson_UserParameter.setUse(UseType.MANDATORY);
			req2.addParameter(createPerson_UserParameter);

			requestManager.create(req1);


			//***********createPrescription***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/createPrescription.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery3 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "createPrescription","[PLACEHOLDER]", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req3 = new Request("createPrescription", "[PLACEHOLDER]", linkQuery3);

			RequestParameter createPrescription_HourParameter = new RequestParameter("Hour", ParameterType.String);
			createPrescription_HourParameter.setDescription("[PLACEHOLDER]");
			createPrescription_HourParameter.setUse(UseType.MANDATORY);
			req3.addParameter(createPrescription_HourParameter);

			RequestParameter createPrescription_PrescriptionParameter = new RequestParameter("Prescription", ParameterType.String);
			createPrescription_PrescriptionParameter.setDescription("[PLACEHOLDER]");
			createPrescription_PrescriptionParameter.setUse(UseType.MANDATORY);
			req3.addParameter(createPrescription_PrescriptionParameter);

			req3.addParameter(createPerson_UserParameter);

			req3.addParameter(createDrug_DrugParameter);

			RequestParameter createPrescription_DayParameter = new RequestParameter("Day", ParameterType.String);
			createPrescription_DayParameter.setDescription("[PLACEHOLDER]");
			createPrescription_DayParameter.setUse(UseType.MANDATORY);
			req3.addParameter(createPrescription_DayParameter);

			requestManager.create(req1);


			//***********createRound***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/createRound.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery4 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "createRound","[PLACEHOLDER]", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req4 = new Request("createRound", "[PLACEHOLDER]", linkQuery4);

			req4.addParameter(createPerson_UserParameter);

			req4.addParameter(createPrescription_DayParameter);

			requestManager.create(req1);


			//***********createTwitterAccount***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/createTwitterAccount.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery5 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "createTwitterAccount","creates the twitterData for the user", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req5 = new Request("createTwitterAccount", "creates the twitterData for the user", linkQuery5);

			RequestParameter createTwitterAccount_accessTokenParameter = new RequestParameter("accessToken", ParameterType.String);
			createTwitterAccount_accessTokenParameter.setDescription("the accessToken user data of the person ");
			createTwitterAccount_accessTokenParameter.setUse(UseType.MANDATORY);
			req5.addParameter(createTwitterAccount_accessTokenParameter);

			RequestParameter createTwitterAccount_consumerKeyParameter = new RequestParameter("consumerKey", ParameterType.String);
			createTwitterAccount_consumerKeyParameter.setDescription("the consumerKey user data of the person ");
			createTwitterAccount_consumerKeyParameter.setUse(UseType.MANDATORY);
			req5.addParameter(createTwitterAccount_consumerKeyParameter);

			RequestParameter createTwitterAccount_UserParameter = new RequestParameter("User", ParameterType.String);
			createTwitterAccount_UserParameter.setDescription("the consumerKey user data of the person ");
			createTwitterAccount_UserParameter.setUse(UseType.MANDATORY);
			req5.addParameter(createTwitterAccount_UserParameter);

			RequestParameter createTwitterAccount_consumerSecretParameter = new RequestParameter("consumerSecret", ParameterType.String);
			createTwitterAccount_consumerSecretParameter.setDescription("the consumerSecret user data of the person ");
			createTwitterAccount_consumerSecretParameter.setUse(UseType.MANDATORY);
			req5.addParameter(createTwitterAccount_consumerSecretParameter);

			RequestParameter createTwitterAccount_accesTokenSecretParameter = new RequestParameter("accesTokenSecret", ParameterType.String);
			createTwitterAccount_accesTokenSecretParameter.setDescription("the accesTokenSecret user data of the person ");
			createTwitterAccount_accesTokenSecretParameter.setUse(UseType.MANDATORY);
			req5.addParameter(createTwitterAccount_accesTokenSecretParameter);

			requestManager.create(req1);


			//***********endRound***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/endRound.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery6 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "endRound","[PLACEHOLDER]", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req6 = new Request("endRound", "[PLACEHOLDER]", linkQuery6);

			requestManager.create(req1);


			//***********getPoints***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/getPoints.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery7 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "getPoints","creates the twitterData for the user", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req7 = new Request("getPoints", "creates the twitterData for the user", linkQuery7);

			req7.addParameter(createTwitterAccount_UserParameter);

			requestManager.create(req1);


			//***********getPrescriptions***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/getPrescriptions.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery8 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "getPrescriptions","[PLACEHOLDER]", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req8 = new Request("getPrescriptions", "[PLACEHOLDER]", linkQuery8);

			req8.addParameter(createPerson_UserParameter);

			requestManager.create(req1);


			//***********getTwitterAccount***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/getTwitterAccount.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery9 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "getTwitterAccount","returns the twitterData for the user", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req9 = new Request("getTwitterAccount", "returns the twitterData for the user", linkQuery9);

			RequestParameter getTwitterAccount_UserParameter = new RequestParameter("User", ParameterType.String);
			getTwitterAccount_UserParameter.setDescription("the twtiter user data of the person ");
			getTwitterAccount_UserParameter.setUse(UseType.MANDATORY);
			req9.addParameter(getTwitterAccount_UserParameter);

			requestManager.create(req1);


			//***********setPoints***********
			url = new URL("file:///C:/Users/admin/Desktop/P506/Administration/SparqlQuerys/setPoints.sparql");
			System.out.println("URL=" + url);
			LinkQuery linkQuery10 = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, "setPoints","[placeHolder]", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
			Request req10 = new Request("setPoints", "[placeHolder]", linkQuery10);

			RequestParameter setPoints_ReasonParameter = new RequestParameter("Reason", ParameterType.String);
			setPoints_ReasonParameter.setDescription("[placeHolder] ");
			setPoints_ReasonParameter.setUse(UseType.MANDATORY);
			req10.addParameter(setPoints_ReasonParameter);

			RequestParameter setPoints_PointsParameter = new RequestParameter("Points", ParameterType.String);
			setPoints_PointsParameter.setDescription("[placeHolder] ");
			setPoints_PointsParameter.setUse(UseType.MANDATORY);
			req10.addParameter(setPoints_PointsParameter);

			req10.addParameter(createTwitterAccount_UserParameter);

			requestManager.create(req1);

			if (smoople != null) {
				smoople.getTemplate().getSemanticConfiguration().getResourceBundle();
				smoople.getTemplate().getRequestList().add(req1);
				smoople.getTemplate().getRequestList().add(req2);
				smoople.getTemplate().getRequestList().add(req3);
				smoople.getTemplate().getRequestList().add(req4);
				smoople.getTemplate().getRequestList().add(req5);
				smoople.getTemplate().getRequestList().add(req6);
				smoople.getTemplate().getRequestList().add(req7);
				smoople.getTemplate().getRequestList().add(req8);
				smoople.getTemplate().getRequestList().add(req9);
				smoople.getTemplate().getRequestList().add(req10);
				sphereManager.update(smoople);
			} else
				System.out.println("Smoople is null");

		} catch (MalformedURLException ex) {
			Logger.getLogger(IoTF2B506Project.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

}