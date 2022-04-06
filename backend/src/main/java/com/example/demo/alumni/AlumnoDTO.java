package com.example.demo.alumni;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private Map<String, EduInfoDTO> education;



}

@Data
class AddressDTO {

    private String street;
    private String number;
    private String country;

}

@Data
class EduInfoDTO {
    private String university;
    private int year;
}


