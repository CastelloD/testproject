package com.example.demo.alumni.controller;

import com.example.demo.alumni.service.AlumniService;
import com.example.demo.alumni.dto.AlumnoDTO;
import com.example.demo.alumni.dto.SearchDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/ex-1/alumni")
public class AlumniController {

    @Autowired
    AlumniService alumniService;

    @PostMapping
    public void saveAlumni(@RequestBody AlumnoDTO alumnoDTO){
        log.info("Entro nella POST saveAlumni");

        alumniService.saveAlumno(alumnoDTO);

    }

    @GetMapping
    public SearchDTO getAlumni(@RequestParam(name = "name") String name, @RequestParam(required = false, name="eduLevel") String eduLevel, Pageable p) {
        log.info("Entro nel Get getAlumni");

        List<AlumnoDTO> alumnoByName = alumniService.findAlumni(name, Optional.ofNullable(eduLevel), p);

        if(alumnoByName.size() == 0)
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Alumni Not Found");

        return SearchDTO.builder().totalCount(alumnoByName.size()).data(alumnoByName).build();
    }


}
