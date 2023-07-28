package com.posdemoproject.microservices.iso8583packagerservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.posdemoproject.microservices.iso8583packagerservice.dto.DataFieldDto;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class Iso8583Service {
	
	private Integer version;
	private String typeMessage;
	private String primaryBitmap;
	private String secondaryBitmap;
	@Autowired
	private Iso8583DataFields requiredDataFields;
	
	private String packedIsoMessage;
	
	public Iso8583Service() {
		
	}
	
	public void unpackingIsoDataFields(String dataFieldsString) {
		requiredDataFields.setBinaryPrimaryBitmap(requiredDataFields.convertHexaBitmapsToBinary(this.primaryBitmap));
		requiredDataFields.setBinarySecondaryBitmap(requiredDataFields.convertHexaBitmapsToBinary(this.secondaryBitmap));
		requiredDataFields.retrieveRequiredIsoFields();
		requiredDataFields.unpackingIsoFieldsValues(dataFieldsString);
	
	}
	
	public void packingIsoMessage(List<DataFieldDto> unpackedFields) {
		requiredDataFields.setBinaryPrimaryBitmap(requiredDataFields.convertHexaBitmapsToBinary(this.primaryBitmap));
		requiredDataFields.setBinarySecondaryBitmap(requiredDataFields.convertHexaBitmapsToBinary(this.secondaryBitmap));
		requiredDataFields.retrieveRequiredIsoFields();
		String stringIsoDataFields = requiredDataFields.packingIsoFields(unpackedFields);
		packedIsoMessage = this.version + this.typeMessage + this.primaryBitmap + this.secondaryBitmap + stringIsoDataFields;
		
	}
}
