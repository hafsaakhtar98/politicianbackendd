package com.cwiztech.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Path {

	private static final Logger log = LoggerFactory.getLogger(Document.class);

	public static final String Localpath = "http://192.168.10.10:8080/SIS/";
	public static final String Onlinpath = "http://52.42.93.207:8080/SIS/";

	public static Logger getLog() {
		return log;
	}

	public static String getLocalpath() {
		return Localpath;
	}

	public static String getOnlinpath() {
		return Onlinpath;
	}

	public String applicationPath = "http://192.168.10.10:8080/SIS/";

}
