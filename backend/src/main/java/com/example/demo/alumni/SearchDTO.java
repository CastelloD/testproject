package com.example.demo.alumni;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SearchDTO {

    private int totalCount;
    private List<AlumnoDTO> data;
}
