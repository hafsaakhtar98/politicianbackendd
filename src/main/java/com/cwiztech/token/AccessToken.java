package com.cwiztech.token;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class AccessToken {
	private static final Logger log = LoggerFactory.getLogger(AccessToken.class);

	private static String applicationPath;
	private static String applicationPathUM;


	@Autowired
	public AccessToken(Environment env) {
		log.info("Trying to get Application Path..........");
		AccessToken.applicationPath = env.getRequiredProperty("file_path.applicationPath");
		AccessToken.applicationPathUM = env.getRequiredProperty("file_path.applicationPathUM");
		log.info("Application Path: "+AccessToken.applicationPath);
	}

	public static String findToken(String apllication_ID, String UserName, String Password)
			throws JsonProcessingException, JSONException, ParseException {

		JSONObject jsonObjmain = new JSONObject();
		jsonObjmain.put("user_NAME", UserName);
		jsonObjmain.put("password", Password);
		jsonObjmain.put("code", apllication_ID);

		log.info("Application Code for Token: " + apllication_ID);

		String rtnToken = null, appPath = null;

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> entityformudel = new HttpEntity<String>(jsonObjmain.toString());
		ResponseEntity<String> application = restTemplate.exchange(applicationPathUM + "application/bycode", HttpMethod.POST,
				entityformudel, String.class);
		JSONObject applicationSetting = new JSONObject(application.getBody());
		if (applicationSetting != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", applicationSetting.getString("content_TYPE"));
			headers.add("Authorization", applicationSetting.getString("authorization_CODE"));
			appPath = applicationSetting.getString("applicationservice_PATH");
			log.info(applicationSetting.toString());
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			rtnToken = restTemplate.postForObject(
					appPath + "oauth/token?grant_type=password&username=" + UserName + "&password=" + Password, entity,
					String.class);
			JSONObject jsonFinalRtnObject = new JSONObject(rtnToken);
			jsonFinalRtnObject.put("application_ID", applicationSetting.getLong("application_ID"));
			jsonFinalRtnObject.put("applicationservice_PATH", applicationSetting.getString("applicationservice_PATH"));
			if (!applicationSetting.isNull("applicationpath_FRONTEND"))
				jsonFinalRtnObject.put("applicationpath_FRONTEND", applicationSetting.getString("applicationpath_FRONTEND"));
			else
				jsonFinalRtnObject.put("applicationpath_FRONTEND", "");
			if (!applicationSetting.isNull("applicationlogo_PATH"))
				jsonFinalRtnObject.put("applicationlogo_PATH", applicationSetting.getString("applicationlogo_PATH"));
			else
				jsonFinalRtnObject.put("applicationlogo_PATH", "");
			if (!applicationSetting.isNull("oauthservice_PATH"))
				jsonFinalRtnObject.put("oauthservice_PATH", applicationSetting.getString("oauthservice_PATH"));
			else
				jsonFinalRtnObject.put("oauthservice_PATH", "");
			if (!applicationSetting.isNull("headername"))
				jsonFinalRtnObject.put("HeaderName", applicationSetting.getString("headername"));
			else
				jsonFinalRtnObject.put("HeaderName", "");
			if (!applicationSetting.isNull("projecttitle"))
				jsonFinalRtnObject.put("ProjectTitle", applicationSetting.getString("projecttitle"));
			else
				jsonFinalRtnObject.put("ProjectTitle", "");
			if (!applicationSetting.isNull("copyrights_YEAR"))
				jsonFinalRtnObject.put("CopyRights", applicationSetting.getString("copyrights_YEAR"));
			else
				jsonFinalRtnObject.put("CopyRights", "");
			if (!applicationSetting.isNull("companylink"))
				jsonFinalRtnObject.put("CompanyLink", applicationSetting.getString("companylink"));
			else
				jsonFinalRtnObject.put("CompanyLink", "");
			if (!applicationSetting.isNull("companyname"))
				jsonFinalRtnObject.put("CompanyName", applicationSetting.getString("companyname"));
			else
				jsonFinalRtnObject.put("CompanyName", "");

			rtnToken = jsonFinalRtnObject.toString();
		} else {
			jsonObjmain.put("message", "Application does't exist");
			rtnToken = jsonObjmain.toString();
		}

		log.info("Output: " + rtnToken);
		log.info("--------------------------------------------------------");

		return rtnToken;
	}

	public static String getTokenDetail(String accessToken) {
		log.info("----------------------------------------------------------------------------------");
		log.info("Get Toeken Detail By Token Service ");
		log.info("Application Path: " + applicationPath);
		log.info("accessToken: " + accessToken);
		log.info("----------------------------------------------------------------------------------");
		RestTemplate restTemplate = new RestTemplate();
		String UserDetail;
		
		String token = accessToken;
		String[] parts = token.split(" ");
		String OauthToken = parts[1];
		log.info(OauthToken);

		ResponseEntity<String> getToken = restTemplate.exchange(applicationPath + "oauth/check_token?token=" + OauthToken, HttpMethod.GET, null, String.class);
		JSONObject myobj = new JSONObject(getToken.getBody().toString());
		UserDetail = myobj.getString("user_name");
		log.info("----------------------------------------------------------------------------------");
		
		return UserDetail;
	}

	public static String findToken() throws JsonProcessingException, JSONException, ParseException {

		log.info("Process to get Token.......");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded");
		headers.add("Authorization", "Basic Y21pczpzZWNyZXQ=");

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		log.info("Application Path: "+applicationPath);
		String rtnToken = restTemplate.postForObject(
				applicationPath + "oauth/token?grant_type=password&username=development&password=development", entity,
				String.class);
		log.info("Output: " + rtnToken);
		log.info("--------------------------------------------------------");

		return rtnToken;
	}
}
