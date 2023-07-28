package com.posdemoproject.microservices.iso8583packagerservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.posdemoproject.microservices.iso8583packagerservice.configuration.Iso8583Configuration;
import com.posdemoproject.microservices.iso8583packagerservice.dto.DataFieldDto;
import com.posdemoproject.microservices.iso8583packagerservice.dto.Iso8583Unpacked;
import com.posdemoproject.microservices.iso8583packagerservice.utils.IsoConstants;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class Iso8583DataFields {
	
	private  ArrayList<DataField> iso8583DataFields = new ArrayList<>();
	private String binaryPrimaryBitmap;
	private String binarySecondaryBitmap;
	
	@Autowired
	private Iso8583Configuration configuration;
	
	public Iso8583DataFields() {}
	

	public void convertHexaBitmapsToBinary(String hexaPrimaryBitmap, String hexaSecondaryBitmap) {
		int hexaNumberPrimary = Integer.valueOf(hexaPrimaryBitmap, 16);
		int hexaNumberSecondary = Integer.valueOf(hexaSecondaryBitmap, 16);
		String binPrimaryBitmap = Integer.toBinaryString(hexaNumberPrimary);
		String binSecondaryBitmap = Integer.toBinaryString(hexaNumberSecondary);
		
		this.binaryPrimaryBitmap = addLeadingZerosToBinaryNumber(binPrimaryBitmap, hexaPrimaryBitmap.length());
		this.binarySecondaryBitmap = addLeadingZerosToBinaryNumber(binSecondaryBitmap, hexaSecondaryBitmap.length());
	}
	
	public String convertHexaBitmapsToBinary(String hexaBitmap) {
		long half01HexaNumber = Long.valueOf(hexaBitmap.substring(0, 8), 16);
		long half02HexaNumber = Long.valueOf(hexaBitmap.substring(8, 16), 16);
		
		String half01BinaryNumber = Long.toBinaryString(half01HexaNumber);
		String half02BinaryNumber = Long.toBinaryString(half02HexaNumber);
		
		return addLeadingZerosToBinaryNumber((half01BinaryNumber + half02BinaryNumber), hexaBitmap.length());
	}
	
	public void retrieveRequiredIsoFields() {
		//according to bitmaps
		this.iso8583DataFields.clear();
		List<DataField> allIso8583DataFields = new ArrayList<>(); 
		allIso8583DataFields = configuration.getIso8583DataFields();
		char[] bitmapChar = new char[128];
		String completeBitmap = this.binaryPrimaryBitmap + this.binarySecondaryBitmap;
		
		bitmapChar = completeBitmap.toCharArray();
		
		for (int index = 0; index < bitmapChar.length; index++) {
			if ((bitmapChar[index] == '1') && (index + 1 == allIso8583DataFields.get(index).getId())){
				this.iso8583DataFields.add(allIso8583DataFields.get(index));
			}
		}
	}
		
	
	public void unpackingIsoFieldsValues(String iso8583Message) {
		
		int index = 0;
		int finalIndex = 0;
		
		for(DataField dataField:iso8583DataFields) {
				if (dataField.getFixedOrVariable() == IsoConstants.VARIABLE_SIZE) {
					finalIndex = index + dataField.getVariableDataSize();
					String sizeVariableField = iso8583Message.substring(index, finalIndex);
					int numberSizeVariableField = Integer.valueOf(sizeVariableField);
					index = finalIndex;
					finalIndex = index + numberSizeVariableField;
					
				}
				
				if (dataField.getFixedOrVariable() == IsoConstants.FIXED_SIZE) {
					finalIndex = index + dataField.getDataSize();
				}
				
				String fieldValue = iso8583Message.substring(index, finalIndex);
				dataField.setFieldValue(fieldValue);
				index = finalIndex;
		}
		
	}
	

	
	public boolean searchInIso8583DataFieldsFieldbyId(int id) {
		for(DataField dataField:iso8583DataFields) {
			if(dataField.getId() == id)
				return true;
		}
		return false;
	
	}
	
	public String packingIsoFields(final List<DataFieldDto> unpackedFields) {
		String stringIsoField = "";
		DataField dataField = null;
		
		for(DataFieldDto dataFieldDto:unpackedFields) {
			dataField = retrieveDataFieldById(dataFieldDto.getId());
			
			if(dataField.getFixedOrVariable() == IsoConstants.VARIABLE_SIZE) {
				stringIsoField += dataField.getDataSize() + dataFieldDto.getValue();
			}
		
			if(dataField.getFixedOrVariable() == IsoConstants.FIXED_SIZE) {
				stringIsoField += dataFieldDto.getValue();
			}
		}
		
		return stringIsoField;
	}
	
	public DataField retrieveDataFieldById(final int id) {
		
		for(DataField dataField:iso8583DataFields) {
			if(dataField.getId() == id) {
				return dataField;
			}
		}
		return null;
	}
	
	private String addLeadingZerosToBinaryNumber(String binaryNumberString, int hexaLength) {
		
		int until = hexaLength * 4;
		
		for(int index = binaryNumberString.length(); index < until; index++) {
			binaryNumberString = "0" + binaryNumberString; 
		}
		
		return binaryNumberString;
	}

}
