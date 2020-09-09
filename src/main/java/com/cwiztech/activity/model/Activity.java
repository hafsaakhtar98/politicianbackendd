package com.cwiztech.activity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cwiztech.login.model.LoginUser;
import com.cwiztech.systemsetting.model.Lookup;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TBLACTIVITY")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ACTIVITY_ID;

	@ManyToOne
	@JoinColumn(name = "PARENTACTIVITY_ID")
	private Activity PARENTACTIVITY_ID;

	@ManyToOne
	@JoinColumn(name = "ACTIVITYTYPE_ID")
	private Lookup ACTIVITYTYPE_ID;

	@Column(name = "ACTIVITY_DESC")
	private String ACTIVITY_DESC;

	@Column(name = "ACTIVITY_DATE")
	private String ACTIVITY_DATE;

	@Column(name = "LOCATION_ID")
	private Long LOCATION_ID;

	@ManyToOne
	@JoinColumn(name = "ACCESSLEVEL_ID")
	private Lookup ACCESSLEVEL_ID;

	@Column(name = "ISACTIVE")
	private String ISACTIVE;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "MODIFIED_BY")
	private LoginUser MODIFIED_BY;

	@JsonIgnore
	@Column(name = "MODIFIED_WHEN")
	private String MODIFIED_WHEN;

	@JsonIgnore
	@Column(name = "MODIFIED_WORKSTATION")
	private String MODIFIED_WORKSTATION;

	public long getACTIVITY_ID() {
		return ACTIVITY_ID;
	}

	public void setACTIVITY_ID(long aCTIVITY_ID) {
		ACTIVITY_ID = aCTIVITY_ID;
	}

	public Activity getPARENTACTIVITY_ID() {
		return PARENTACTIVITY_ID;
	}

	public void setPARENTACTIVITY_ID(Activity pARENTACTIVITY_ID) {
		PARENTACTIVITY_ID = pARENTACTIVITY_ID;
	}

	public Lookup getACTIVITYTYPE_ID() {
		return ACTIVITYTYPE_ID;
	}

	public void setACTIVITYTYPE_ID(Lookup aCTIVITYTYPE_ID) {
		ACTIVITYTYPE_ID = aCTIVITYTYPE_ID;
	}

	public String getACTIVITY_DESC() {
		return ACTIVITY_DESC;
	}

	public void setACTIVITY_DESC(String aCTIVITY_DESC) {
		ACTIVITY_DESC = aCTIVITY_DESC;
	}

	public String getACTIVITY_DATE() {
		return ACTIVITY_DATE;
	}

	public void setACTIVITY_DATE(String aCTIVITY_DATE) {
		ACTIVITY_DATE = aCTIVITY_DATE;
	}

	public Long getLOCATION_ID() {
		return LOCATION_ID;
	}

	public void setLOCATION_ID(Long lOCATION_ID) {
		LOCATION_ID = lOCATION_ID;
	}

	public Lookup getACCESSLEVEL_ID() {
		return ACCESSLEVEL_ID;
	}

	public void setACCESSLEVEL_ID(Lookup aCCESSLEVEL_ID) {
		ACCESSLEVEL_ID = aCCESSLEVEL_ID;
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
		return (long) 2;
	}
}
