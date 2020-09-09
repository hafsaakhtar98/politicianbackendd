package com.cwiztech.politician.model;

import java.text.ParseException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.JSONException;
import org.json.JSONObject;

import com.cwiztech.login.model.LoginUser;
import com.cwiztech.services.PersonService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

@Entity
@Table(name="TBLPOLITICIAN")
public class Politician{
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long POLITICIAN_ID;
	
	@Column(name="PERSON_ID")
	private long PERSON_ID;
	
	@Transient
	private String PERSON_DETAIL;

	@Column(name="ISACTIVE")
	private String ISACTIVE;
	
	@JsonIgnore
	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "MODIFIED_BY")
	 private LoginUser MODIFIED_BY;

	@JsonIgnore
	@Column(name = "MODIFIED_WHEN")
	private String MODIFIED_WHEN;

	@JsonIgnore
	@Column(name = "MODIFIED_WORKSTATION")
	private String MODIFIED_WORKSTATION;

	public long getPOLITICIAN_ID() {
		return POLITICIAN_ID;
	}

	public void setPOLITICIAN_ID(long pOLITICIAN_ID) {
		POLITICIAN_ID = pOLITICIAN_ID;
	}

	public long getPERSON_ID() {
		return PERSON_ID;
	}

	public void setPERSON_ID(long pERSON_ID) {
		PERSON_ID = pERSON_ID;
	}

	public String getPERSON_DETAIL() throws JSONException, JsonProcessingException, ParseException {
		JSONObject personObject = new JSONObject(
				PersonService.GET("person/" + this.PERSON_ID, "nouser", "nouser"));
		PERSON_DETAIL = personObject.toString();
		return PERSON_DETAIL;
	}

	public String getISACTIVE() {
		return ISACTIVE;
	}

	public void setISACTIVE(String iSACTIVE) {
		ISACTIVE = iSACTIVE;
	}

	@JsonIgnore
	public LoginUser getMODIFIED_BY() {
		return MODIFIED_BY;
	}

	public void setMODIFIED_BY(LoginUser mODIFIED_BY) {
		MODIFIED_BY = mODIFIED_BY;
	}

	@JsonIgnore
	public String getMODIFIED_WHEN() {
		return MODIFIED_WHEN;
	}

	public void setMODIFIED_WHEN(String mODIFIED_WHEN) {
		MODIFIED_WHEN = mODIFIED_WHEN;
	}

	@JsonIgnore
	public String getMODIFIED_WORKSTATION() {
		return MODIFIED_WORKSTATION;
	}

	public void setMODIFIED_WORKSTATION(String mODIFIED_WORKSTATION) {
		MODIFIED_WORKSTATION = mODIFIED_WORKSTATION;
	}	

	public static long getDatabaseTableID() {
	   return(long) 6;
	   
   }
}
