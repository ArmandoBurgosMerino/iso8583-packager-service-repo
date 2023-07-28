package com.posdemoproject.microservices.iso8583packagerservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataFieldDto {
	
	private Integer id;
	private String name;
	private String value;

}
