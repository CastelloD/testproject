package com.example.demo.alumni;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<AlumnoDTO> findAlumnoByName(String name, Pageable p) {
        List<AlumnoDTO> alumnoDTOList;

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        List<Alumni> alumniList = alumniRepository.findByName(name, p);
        alumnoDTOList = alumniList.stream().map(a -> {
            List<AddressDTO> addressDTOS = new ArrayList<>();
            Map<String, EduInfoDTO> stringEduInfoDTOMap = new HashMap<>();
            try {
            addressDTOS = mapper.readValue(a.getAddressMetadata(), new TypeReference<List<AddressDTO>>() {
            });

            stringEduInfoDTOMap = mapper.readValue(a.getEducationMetadata(), new TypeReference<Map<String, EduInfoDTO>>() {
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
