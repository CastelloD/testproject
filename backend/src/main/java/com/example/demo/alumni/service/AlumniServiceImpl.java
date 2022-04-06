package com.example.demo.alumni.service;

import com.example.demo.alumni.dto.AddressDTO;
import com.example.demo.alumni.dto.AlumnoDTO;
import com.example.demo.alumni.dto.EducationDTO;
import com.example.demo.alumni.model.Alumni;
import com.example.demo.alumni.repository.AlumniRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlumniServiceImpl implements AlumniService{

    @Autowired
    AlumniRepository alumniRepository;

    @Override
    public void saveAlumno(AlumnoDTO alumnoDTO){

        Alumni alumni = getAlumno(alumnoDTO);

        alumniRepository.save(alumni);
    }

    @Override
    public List<AlumnoDTO> findAlumni(String name, Optional<String> education, Pageable p) {

        List<Alumni> alumniList;
        if(education.isPresent()){
            alumniList = alumniRepository.findByNameAndEducationMetadata(name, education.get(), p);
        } else {
            alumniList = alumniRepository.findByName(name, p);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        List<AlumnoDTO> alumnoDTOList = alumniList.stream().map(a -> {
            List<AddressDTO> addressDTOS = new ArrayList<>();
            Map<String, EducationDTO> stringEduInfoDTOMap = new HashMap<>();
            try {
                addressDTOS = mapper.readValue(a.getAddressMetadata(), new TypeReference<List<AddressDTO>>() {
                });

                stringEduInfoDTOMap = mapper.readValue(a.getEducationMetadata(), new TypeReference<Map<String, EducationDTO>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            AlumnoDTO alumnoDTO = AlumnoDTO.builder().name(a.getName())
                    .addresses(addressDTOS)
                    .education(stringEduInfoDTOMap).build();

            return alumnoDTO;

        }).collect(Collectors.toList());

        return alumnoDTOList;
    }


    private Alumni getAlumno(AlumnoDTO alumnoDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        Alumni alumno = new Alumni();
        try {
            alumno.setName(alumnoDTO.getName());
            alumno.setAddressMetadata(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(alumnoDTO.getAddresses()));
            alumno.setEducationMetadata(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(alumnoDTO.getEducation()));
        } catch(JsonProcessingException e){
            log.error("Errore nella conversione dei dati");
            e.printStackTrace();
        }
        return alumno;
    }

}
