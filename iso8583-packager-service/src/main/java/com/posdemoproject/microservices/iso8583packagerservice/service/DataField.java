package com.posdemoproject.microservices.iso8583packagerservice.service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataField {
	
	private int id;
	private String name;
	private String dataType;
	private char mandatory;
	private int dataSize;
	private int variableDataSize;
	private char fixedOrVariable;
	private String fieldValue;

}
