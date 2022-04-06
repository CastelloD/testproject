package com.example.demo.alumni;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlumniService {


    void saveAlumno(AlumnoDTO alumni);

    List<AlumnoDTO> findAlumnoByName(String name, Pageable p);

}
