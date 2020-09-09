package com.cwiztech.politician.controller;

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
import com.cwiztech.politician.model.Politician;
import com.cwiztech.politician.repository.politicianRepository;
import com.cwiztech.login.model.LoginUser;
import com.cwiztech.login.repository.loginUserRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/politician")
public class politicianController {
	private static final Logger log = LoggerFactory.getLogger(politicianController.class);

	@Autowired
	private politicianRepository politicianrepository;

	@Autowired
	private loginUserRepository loginuserrepository;

	@Autowired
	private apiRequestDataLogRepository apirequestdatalogRepository;

	@Autowired
	private tableDataLogRepository tbldatalogrepository;

	@Autowired
	private databaseTablesRepository databasetablesrepository;

	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		LoginUser requestUser;

		log.info("GET: /politician");

		List<Politician> politician = politicianrepository.findActive();
		String rtn, workstation = null;

        String user_NAME = AccessToken.getTokenDetail(headToken);
        requestUser = loginuserrepository.getUser(user_NAME);

        DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser, "/politician",
				null, workstation);


		rtn = mapper.writeValueAsString(politician);

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
		LoginUser requestUser;

		log.info("GET: /politician/all");

		List<Politician> politician = politicianrepository.findAll();
		String rtn, workstation = null;
		
		
        String user_NAME = AccessToken.getTokenDetail(headToken);
        requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser, "/politician/all",
				null, workstation);

		rtn = mapper.writeValueAsString(politician);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getOne(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		LoginUser requestUser;

		log.info("GET: /politician/" + id);

		Politician politician = politicianrepository.findOne(id);
		String rtn, workstation = null;

		String user_NAME = AccessToken.getTokenDetail(headToken);
        requestUser = loginuserrepository.getUser(user_NAME);
		
		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser,
				"/politician/" + id, null, workstation);

		rtn = mapper.writeValueAsString(politician);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String insert(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {

		log.info("POST: /politician");
		log.info("Input: " + data);

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MMM/YYYY HH:mm:ss");
		Date date = new Date();
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jsonObj = new JSONObject(data);

		LoginUser requestUser;
		String rtn, workstation = null;
		
		String user_NAME = AccessToken.getTokenDetail(headToken);
        requestUser = loginuserrepository.getUser(user_NAME);

		Politician politician = new Politician();

		if (jsonObj.has("workstation"))
			workstation = jsonObj.getString("workstation");

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("POST", databaseTableID, requestUser, "/politician", data, workstation);
		
	    if (!jsonObj.has("person_ID")) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "Politician", "Person ID is missing");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
			return apiRequest.getREQUEST_OUTPUT();
		}
		
		politician.setPERSON_ID(jsonObj.getLong("person_ID"));

		politician.setISACTIVE("Y");
		politician.setMODIFIED_BY(requestUser);
		politician.setMODIFIED_WORKSTATION(workstation);
		politician.setMODIFIED_WHEN(dateFormat1.format(date));
		politician = politicianrepository.saveAndFlush(politician);
		rtn = mapper.writeValueAsString(politician);

		tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(politician.getPOLITICIAN_ID(),
				databaseTableID, requestUser, rtn));

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
		LoginUser requestUser;

		log.info("PUT: /politician/" + id);
		log.info("Input: " + data);

		JSONObject jsonObj = new JSONObject(data);
		Politician politician = politicianrepository.findOne(id);
		String rtn, workstation = null;
		String user_NAME = AccessToken.getTokenDetail(headToken);
        requestUser = loginuserrepository.getUser(user_NAME);
		
		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		
		if (jsonObj.has("workstation"))
			workstation = jsonObj.getString("workstation");
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("PUT", databaseTableID, requestUser, "/politician", data, workstation);

		if (jsonObj.has("person_ID")) 
		    politician.setPERSON_ID(jsonObj.getLong("person_ID"));
				 
		 if (jsonObj.has("isactive"))
			politician.setISACTIVE(jsonObj.getString("isactive"));
				 
		politician.setMODIFIED_BY(requestUser);
		politician.setMODIFIED_WORKSTATION(workstation);
		politician.setMODIFIED_WHEN(dateFormat1.format(date));
		politician = politicianrepository.saveAndFlush(politician);
		rtn = mapper.writeValueAsString(politician);

		tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(politician.getPOLITICIAN_ID(),
				databaseTableID, requestUser, rtn));

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		LoginUser requestUser;

		log.info("DELETE: /politician/" + id);

		Politician politician = politicianrepository.findOne(id);
		String rtn, workstation = null;

		String user_NAME = AccessToken.getTokenDetail(headToken);
        requestUser = loginuserrepository.getUser(user_NAME);
		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		requestUser = loginuserrepository.findOne((long) 0);
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("DELETE", databaseTableID, requestUser, "/politician",
				null, workstation);

		politicianrepository.delete(politician);
		rtn = mapper.writeValueAsString(politician);

		tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(politician.getPOLITICIAN_ID(),
				databaseTableID, requestUser, rtn));

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
		
		log.info("GET: /politician/" + id + "/remove");

		Politician politician = politicianrepository.findOne(id);
		String rtn, workstation = null;

		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);
		
		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		requestUser = loginuserrepository.findOne((long) 0);
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("GET", databaseTableID, requestUser,
				"/politician" + id + "/remove", "", workstation);
		politician.setISACTIVE("N");
		politician.setMODIFIED_BY(requestUser);
		politician.setMODIFIED_WORKSTATION(workstation);
		politician.setMODIFIED_WHEN(dateFormat1.format(date));
		politician = politicianrepository.saveAndFlush(politician);
		rtn = mapper.writeValueAsString(politician);
		tbldatalogrepository
				.saveAndFlush(tableDataLogs.TableSaveDataLog(politician.getPOLITICIAN_ID(), databaseTableID, requestUser, rtn));

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
		
		log.info("POST: politician/search" + ((active == true) ? "" : "/all"));
		log.info("Input: " + data);

		JSONObject jsonObj = new JSONObject(data);
		String rtn = null, workstation = null;

		List<Politician> politician = ((active == true)
				? politicianrepository.findBySearch("%" + jsonObj.getString("search") + "%")
				: politicianrepository.findAllBySearch("%" + jsonObj.getString("search") + "%"));
		
		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);

		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		requestUser = loginuserrepository.findOne((long) 0);
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("POST", databaseTableID, requestUser,
				"/politician/search" + ((active == true) ? "" : "/all"), null, workstation);

		rtn = mapper.writeValueAsString(politician);

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
		
		log.info("POST: politician/search" + ((active == true) ? "" : "/all"));
		log.info("Input: " + data);

		JSONObject jsonObj = new JSONObject(data);
		long person_ID = 0;

		if (jsonObj.has("person_ID"))
			person_ID = jsonObj.getLong("person_ID");

		List<Politician> politician = ((active == true)
				? politicianrepository.findByAdvancedSearch(person_ID)
				: politicianrepository.findAllByAdvancedSearch(person_ID));
		String rtn, workstation = null;

		LoginUser requestUser;
		String user_NAME = AccessToken.getTokenDetail(headToken);
		requestUser = loginuserrepository.getUser(user_NAME);
		
		DatabaseTables databaseTableID = databasetablesrepository.findOne(Politician.getDatabaseTableID());
		requestUser = loginuserrepository.findOne((long) 0);
		APIRequestDataLog apiRequest = tableDataLogs.apiRequestDataLog("POST", databaseTableID, requestUser,
				"/politician/advancedsearch" + ((active == true) ? "" : "/all"), data, workstation);

		rtn = mapper.writeValueAsString(politician);

		apiRequest.setREQUEST_OUTPUT(rtn);
		apiRequest.setREQUEST_STATUS("Success");
		apirequestdatalogRepository.saveAndFlush(apiRequest);

		log.info("Output: " + rtn);
		log.info("--------------------------------------------------------");

		return rtn;
	}

};
