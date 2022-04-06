package com.example.demo.alumni.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Builder
@Data
@AllArgsConstructor
public class AlumnoDTO {

    private String name;
    private List<AddressDTO> addresses;
    private Map<String, EducationDTO> education;



}



