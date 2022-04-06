package com.example.demo.alumni.service;

import com.example.demo.alumni.dto.AlumnoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AlumniService {


    void saveAlumno(AlumnoDTO alumni);

    List<AlumnoDTO> findAlumni(String name, Optional<String> education, Pageable p);

}
