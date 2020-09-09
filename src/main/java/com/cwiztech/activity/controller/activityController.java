package com.cwiztech.activity.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cwiztech.datalogs.model.APIRequestDataLog;
import com.cwiztech.datalogs.model.DatabaseTables;
import com.cwiztech.datalogs.model.tableDataLogs;
import com.cwiztech.datalogs.repository.apiRequestDataLogRepository;
import com.cwiztech.datalogs.repository.databaseTablesRepository;
import com.cwiztech.datalogs.repository.tableDataLogRepository;
import com.cwiztech.login.model.LoginUser;
import com.cwiztech.login.repository.loginUserRepository;
import com.cwiztech.activity.model.Activity;
import com.cwiztech.activity.repository.activityRepository;
import com.cwiztech.systemsetting.repository.lookupRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/activity")
public class activityController {
	private static final Logger log = LoggerFactory.getLogger(activityController.class);

	@Autowired
	private activityRepository activityrepository;

	@Autowired
	private loginUserRepository loginuserrepository;

	@Autowired
	private apiRequestDataLogRepository apirequestdatalogRepository;

	@Autowired
	private tableDataLogRepository tbldatalogrepository;

	@Autowired
	private databaseTablesRepository databasetablesrepository;
	
	@Autowired
	private lookupRepository lookuprepository;

	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		log.info("GET: /activity");

		List<Activity> activity = activityrepository.findActive();
		String rtn, workstation = null;
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser, "/activity", null,
				workstation);

		rtn = mapper.writeValueAsString(activity);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String getAll(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		log.info("GET: /activity/all");

		List<Activity> activity = activityrepository.findAll();
		String rtn, workstation = null;
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser, "/activity/all", null,
				workstation);

		rtn = mapper.writeValueAsString(activity);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getOne(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		log.info("GET: /activity/" + id);

		Activity activity = activityrepository.findOne(id);
		String rtn, workstation = null;
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser, "/activity/" + id, null,
				workstation);

		rtn = mapper.writeValueAsString(activity);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String insert(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MMM/YYYY HH:mm:ss");
		Date date = new Date();
		ObjectMapper mapper = new ObjectMapper();

		log.info("POST: /activity");
		log.info("Input: " + data);

		JSONObject jsonObj = new JSONObject(data);
		Activity activity = new Activity();
		String rtn, workstation = null;
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());

		if (jsonObj.has("workstation"))
			workstation = jsonObj.getString("workstation");
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("POST", databaseTableID, requestUser, "/activity", data,
				workstation);
		
	    if (!jsonObj.has("activitytype_ID")) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "Activity", "Activity Type ID is missing");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
			return apiRequest.getREQUEST_OUTPUT();
		}

	    if (!jsonObj.has("activity_DESC")) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "Activity", "Activity Decription is missing");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
			return apiRequest.getREQUEST_OUTPUT();
		}

	    if (!jsonObj.has("activity_DATE")) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "Activity", "Activity Date is missing");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
			return apiRequest.getREQUEST_OUTPUT();
		}

	    if (!jsonObj.has("location_ID")) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "Activity", "Activity Location ID is missing");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
			return apiRequest.getREQUEST_OUTPUT();
		}

	    if (!jsonObj.has("accesslevel_ID")) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "Activity", "Activity Access Level ID is missing");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
			return apiRequest.getREQUEST_OUTPUT();
		}

	    if (!jsonObj.has("parentactivity_ID"))
	    	activity.setPARENTACTIVITY_ID(activityrepository.getOne(jsonObj.getLong("parentactivity_ID")));
	    activity.setACTIVITYTYPE_ID(lookuprepository.getOne(jsonObj.getLong("activitytype_ID")));
	    activity.setACTIVITY_DESC(jsonObj.getString("activity_DESC"));
	    activity.setACTIVITY_DATE(jsonObj.getString("activity_DATE"));
	    activity.setLOCATION_ID(jsonObj.getLong("location_ID"));
	    activity.setACCESSLEVEL_ID(lookuprepository.getOne(jsonObj.getLong("accesslevel_ID")));

		activity.setISACTIVE("Y");
		activity.setMODIFIED_WORKSTATION(workstation);
		activity.setMODIFIED_WHEN(dateFormat1.format(date));

		log.info(mapper.writeValueAsString(activity));

		activity = activityrepository.saveAndFlush(activity);
		rtn = mapper.writeValueAsString(activity);

		tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(activity.getACTIVITY_ID(), databaseTableID, requestUser, rtn));

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable Long id, @RequestBody String data,@RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MMM/YYYY HH:mm:ss");
		Date date = new Date();
		ObjectMapper mapper = new ObjectMapper();

		log.info("PUT: /activity/" + id);
		log.info("Input: " + data);

		JSONObject jsonObj = new JSONObject(data);
		Activity activity = activityrepository.findOne(id);
		String rtn, workstation = null;
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());

		if (jsonObj.has("workstation"))
			workstation = jsonObj.getString("workstation");
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("PUT", databaseTableID, requestUser, "/activity", data,
				workstation);

		if (activity == null) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "activity", "activity not exist!");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
			return apiRequest.getREQUEST_OUTPUT();
		}

	    if (jsonObj.has("parentactivity_ID"))
	    	activity.setPARENTACTIVITY_ID(activityrepository.getOne(jsonObj.getLong("parentactivity_ID")));

	    if (jsonObj.has("activitytype_ID"))
	    	activity.setACTIVITYTYPE_ID(lookuprepository.getOne(jsonObj.getLong("activitytype_ID")));

	    if (jsonObj.has("activity_DESC"))
	    	activity.setACTIVITY_DESC(jsonObj.getString("activity_DESC"));

	    if (jsonObj.has("activity_DATE"))
	    	activity.setACTIVITY_DATE(jsonObj.getString("activity_DATE"));

	    if (jsonObj.has("location_ID"))
	    	activity.setLOCATION_ID(jsonObj.getLong("location_ID"));

	    if (jsonObj.has("accesslevel_ID"))
		    activity.setACCESSLEVEL_ID(lookuprepository.getOne(jsonObj.getLong("accesslevel_ID")));

	    if (jsonObj.has("isactive"))
			 activity.setISACTIVE(jsonObj.getString("isactive"));

		 activity.setMODIFIED_WORKSTATION(workstation);
		activity.setMODIFIED_WHEN(dateFormat1.format(date));
		activity = activityrepository.saveAndFlush(activity);
		rtn = mapper.writeValueAsString(activity);

		tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(activity.getACTIVITY_ID(), databaseTableID, requestUser, rtn));

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		ObjectMapper mapper = new ObjectMapper();

		log.info("DELETE: /activity/" + id);

		Activity activity = activityrepository.findOne(id);
		String rtn, workstation = null;
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("DELETE", databaseTableID, requestUser, "/activity", null,
				workstation);

		activityrepository.delete(activity);
		rtn = mapper.writeValueAsString(activity);

		tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(activity.getACTIVITY_ID(), databaseTableID, requestUser, rtn));

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String remove(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MMM/YYYY HH:mm:ss");
		Date date = new Date();

		ObjectMapper mapper = new ObjectMapper();
		log.info("GET: /activity/remove/" + id);

		Activity activity = activityrepository.findOne(id);
		String rtn, workstation = null;
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser,
				"/activity/remove/" + id, "", workstation);
		activity.setISACTIVE("N");
		activity.setMODIFIED_WORKSTATION(workstation);
		activity.setMODIFIED_WHEN(dateFormat1.format(date));
		activity = activityrepository.saveAndFlush(activity);
		rtn = mapper.writeValueAsString(activity);
		tbldatalogrepository
				.saveAndFlush(tableDataLogs.TableSaveDataLog(activity.getACTIVITY_ID(), databaseTableID, requestUser, rtn));

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String getBySearch(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		return BySearch(data, true, headToken);

	}

	@RequestMapping(value = "/search/all", method = RequestMethod.POST)
	public String getAllBySearch(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		return BySearch(data, false, headToken);

	}

	public String BySearch(String data, boolean active, String headToken) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		log.info("POST: activity/search" + ((active == true) ? "" : "/all"));
		log.info("Input: " + data);

		JSONObject jsonObj = new JSONObject(data);
		String rtn = null, workstation = null;

		List<Activity> activity = ((active == true)
				? activityrepository.findBySearch("%" + jsonObj.getString("search") + "%")
				: activityrepository.findAllBySearch("%" + jsonObj.getString("search") + "%"));
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("POST", databaseTableID, requestUser,
				"/activity/search" + ((active == true) ? "" : "/all"), null, workstation);

		rtn = mapper.writeValueAsString(activity);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;

	}

	@RequestMapping(value = "/advancedsearch", method = RequestMethod.POST)
	public String getByAdvancedSearch(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		return ByAdvancedSearch(data, true, headToken);
	}

	@RequestMapping(value = "/advancedsearch/all", method = RequestMethod.POST)
	public String getAllByAdvancedSearch(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		return ByAdvancedSearch(data, false, headToken);
	}

	public String ByAdvancedSearch(String data, boolean active, String headToken) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		log.info("POST: activity/advancedsearch" + ((active == true) ? "" : "/all"));
		log.info("Input: " + data);

		JSONObject jsonObj = new JSONObject(data);
		long parentactivity_ID = 0, activitytype_ID = 0, location_ID = 0, accesslevel_ID = 0;

	    if (jsonObj.has("parentactivity_ID"))
	    	parentactivity_ID = jsonObj.getLong("parentactivity_ID");

	    if (jsonObj.has("activitytype_ID"))
	    	activitytype_ID = jsonObj.getLong("activitytype_ID");

	    if (jsonObj.has("location_ID"))
	    	location_ID = jsonObj.getLong("location_ID");

	    if (jsonObj.has("accesslevel_ID"))
	    	accesslevel_ID = jsonObj.getLong("accesslevel_ID");

		List<Activity> activity = ((active == true)
				? activityrepository.findByAdvancedSearch(parentactivity_ID, activitytype_ID, location_ID, accesslevel_ID)
				: activityrepository.findAllByAdvancedSearch(parentactivity_ID, activitytype_ID, location_ID, accesslevel_ID));
		String rtn, workstation = null;

		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);
		
		DatabaseTables databaseTableID = databasetablesrepository.findOne(Activity.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("POST", databaseTableID, requestUser,
				"/activity/advancedsearch" + ((active == true) ? "" : "/all"), data, workstation);

		rtn = mapper.writeValueAsString(activity);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

}
