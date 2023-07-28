package com.posdemoproject.microservices.iso8583packagerservice.dto;

import java.util.List;

import com.posdemoproject.microservices.iso8583packagerservice.mapper.Iso8583Mapper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Iso8583Unpacked {
	
	private Integer version;
	private String typeMessage;
	private String primaryBitmap;
	private String secondaryBitmap;
	private List<DataFieldDto> unpackedFields;
	
}
