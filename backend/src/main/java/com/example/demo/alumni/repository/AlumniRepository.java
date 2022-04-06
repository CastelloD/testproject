package com.example.demo.alumni.repository;

import com.example.demo.alumni.model.Alumni;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {

    List<Alumni> findByName(String name, Pageable pageable);

    @Query(value = "select a.* from alumni a where name = ?1 and (education_metadata::::json->>?2) is not null ", nativeQuery = true)
    List<Alumni> findByNameAndEducationMetadata(String name, String education, Pageable pageable);
}
