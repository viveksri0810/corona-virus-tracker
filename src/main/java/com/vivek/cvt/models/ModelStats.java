package com.vivek.cvt.models;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class ModelStats {

    private String country_Region;
    private String province_State;
    private String last_Update;
    private String confirmed;
    private String deaths;
    private String recovered;
    private String active;
}
