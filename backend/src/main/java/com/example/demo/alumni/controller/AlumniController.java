package com.example.demo.alumni.controller;

import com.example.demo.alumni.dto.AddressDTO;
import com.example.demo.alumni.dto.EducationDTO;
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
import java.util.Map;
import java.util.Objects;
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

        checkData(alumnoDTO);

        alumniService.saveAlumno(alumnoDTO);

    }


    private void checkData(AlumnoDTO alumnoDTO) {

        if(alumnoDTO.getName() == null || !alumnoDTO.getName().chars().allMatch(Character::isLetter))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Alumni name not filled in correctly");

        List<AddressDTO> addresses = alumnoDTO.getAddresses();

        if(addresses == null)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Alumni address not filled in correctly");

        addresses.forEach(a -> {
            if (a.getCountry() == null || a.getStreet() == null || a.getNumber() == null || !a.getNumber().chars().allMatch(Character::isDigit))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Alumni address not filled in correctly");
        });

        Map<String, EducationDTO> education = alumnoDTO.getEducation();

        if(education == null)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Alumni education not filled in correctly");

        education.forEach((k,v) -> {
            if(k == null || v.getUniversity() == null || v.getYear() == 0)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Alumni education not filled in correctly");
        });
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
