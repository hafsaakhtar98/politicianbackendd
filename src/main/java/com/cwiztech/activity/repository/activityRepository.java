package com.cwiztech.activity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cwiztech.activity.model.Activity;

public interface activityRepository extends JpaRepository<Activity, Long> {

	@Query(value = "select * from TBLACTIVITY where ISACTIVE='Y'", nativeQuery = true)
	public List<Activity> findActive();

	@Query(value = "select * from TBLACTIVITY "
			+ "where ACTIVITY_DESC like ?1 and ISACTIVE='Y'", nativeQuery = true)
	public List<Activity> findBySearch(String search);

	@Query(value = "select * from TBLACTIVITY "
			+ "where ACTIVITY_DESC like ?1", nativeQuery = true)
	public List<Activity> findAllBySearch(String search);

	@Query(value = "select * from TBLACTIVITY " 
			+ "where PARENTACTIVITY_ID LIKE CASE WHEN ?1 = 0 THEN PARENTACTIVITY_ID ELSE ?1 END "
			+ "and ACTIVITYTYPE_ID LIKE CASE WHEN ?2 = 0 THEN ACTIVITYTYPE_ID ELSE ?2 END "
			+ "and LOCATION_ID LIKE CASE WHEN ?3 = 0 THEN LOCATION_ID ELSE ?3 END "
			+ "and ACCESSLEVEL_ID LIKE CASE WHEN ?4 = 0 THEN ACCESSLEVEL_ID ELSE ?4 END "
			+ "and ISACTIVE='Y'", nativeQuery = true)
	List<Activity> findByAdvancedSearch(Long paid, Long atid, Long lid, Long alid);

	@Query(value = "select * from TBLACTIVITY " 
			+ "where PARENTACTIVITY_ID LIKE CASE WHEN ?1 = 0 THEN PARENTACTIVITY_ID ELSE ?1 END "
			+ "and ACTIVITYTYPE_ID LIKE CASE WHEN ?2 = 0 THEN ACTIVITYTYPE_ID ELSE ?2 END "
			+ "and LOCATION_ID LIKE CASE WHEN ?3 = 0 THEN LOCATION_ID ELSE ?3 END "
			+ "and ACCESSLEVEL_ID LIKE CASE WHEN ?4 = 0 THEN ACCESSLEVEL_ID ELSE ?4 END "
			+ "", nativeQuery = true)
	List<Activity> findAllByAdvancedSearch(Long paid, Long atid, Long lid, Long alid);

}