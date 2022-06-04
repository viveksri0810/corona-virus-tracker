package com.vivek.cvt.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SummaryStats {

    private Long totalConfirmed;
    private Long totalDeath;
    private Long totalRecovered;
    private Long totalActiveCases;

}
