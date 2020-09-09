package com.cwiztech.politician.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cwiztech.politician.model.Politician;


public interface politicianRepository extends JpaRepository<Politician, Long> {

	@Query(value = "select * from TBLPOLITICIAN where ISACTIVE='Y'", nativeQuery = true)
	public List<Politician> findActive();

	@Query(value = "select * from TBLPOLITICIAN "
			+ "where ISACTIVE='Y'", nativeQuery = true)
	public List<Politician> findBySearch(String search);

	@Query(value = "select * from TBLPOLITICIAN "
			+ "", nativeQuery = true)
	public List<Politician> findAllBySearch(String search);

	@Query(value = "select * from TBLPOLITICIAN " 
			+ "where PERSON_ID LIKE CASE WHEN ?1 = 0 THEN PERSON_ID ELSE ?1 END "
			+ "and ISACTIVE='Y'", nativeQuery = true)
	List<Politician> findByAdvancedSearch(Long pid);

	@Query(value = "select * from TBLPOLITICIAN " 
			+ "where PERSON_ID LIKE CASE WHEN ?1 = 0 THEN PERSON_ID ELSE ?1 END ", nativeQuery = true)
	List<Politician> findAllByAdvancedSearch(Long pid);

}
