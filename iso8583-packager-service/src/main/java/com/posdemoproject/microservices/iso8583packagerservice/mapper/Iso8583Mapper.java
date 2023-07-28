package com.posdemoproject.microservices.iso8583packagerservice.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.posdemoproject.microservices.iso8583packagerservice.dto.DataFieldDto;
import com.posdemoproject.microservices.iso8583packagerservice.dto.Iso8583Packed;
import com.posdemoproject.microservices.iso8583packagerservice.dto.Iso8583Unpacked;
import com.posdemoproject.microservices.iso8583packagerservice.service.DataField;
import com.posdemoproject.microservices.iso8583packagerservice.service.Iso8583Service;

@Component
public class Iso8583Mapper {
	
	@Autowired
	private Iso8583Service iso8583Service;

	public void mappingPackedMessageIntoIsoService(Iso8583Packed iso8583Packed) {
		
		String isoMessage = iso8583Packed.getIso8583Message();
		
		iso8583Service.setVersion(Integer.valueOf(isoMessage.substring(0, 1)));
		iso8583Service.setTypeMessage(isoMessage.substring(1, 4));
		iso8583Service.setPrimaryBitmap(isoMessage.substring(4, 20));
		iso8583Service.setSecondaryBitmap(isoMessage.substring(20 ,36));
		iso8583Service.unpackingIsoDataFields(isoMessage.substring(20));
	}
	
	public void mappingUnpackedMessageIntoIsoService(Iso8583Unpacked iso8583Unpacked) {
		iso8583Service.setVersion(iso8583Unpacked.getVersion());
		iso8583Service.setTypeMessage(iso8583Unpacked.getTypeMessage());
		iso8583Service.setPrimaryBitmap(iso8583Unpacked.getPrimaryBitmap());
		iso8583Service.setSecondaryBitmap(iso8583Unpacked.getSecondaryBitmap());
		iso8583Service.packingIsoMessage(iso8583Unpacked.getUnpackedFields());
	}
	

	
	public Iso8583Unpacked mappingIntoIso8583Unpacked() {
		Iso8583Unpacked iso8583Unpacked = new Iso8583Unpacked();
		List<DataField> isoDataFields = new ArrayList<>();
		List<DataFieldDto> isoDataFieldsDto = new ArrayList<>();
		
		iso8583Unpacked.setVersion(iso8583Service.getVersion());
		iso8583Unpacked.setTypeMessage(iso8583Service.getTypeMessage());
		iso8583Unpacked.setPrimaryBitmap(iso8583Service.getPrimaryBitmap());
		iso8583Unpacked.setSecondaryBitmap(iso8583Service.getSecondaryBitmap());
		
		isoDataFields = iso8583Service.getRequiredDataFields().getIso8583DataFields();
		for(DataField isoDataField:isoDataFields) {
			
			DataFieldDto dataFieldDto = new DataFieldDto();
			dataFieldDto.setId(isoDataField.getId());
			dataFieldDto.setName(isoDataField.getName());
			dataFieldDto.setValue(isoDataField.getFieldValue());
			
			isoDataFieldsDto.add(dataFieldDto);
		}
		
		iso8583Unpacked.setUnpackedFields(isoDataFieldsDto);
		
		return iso8583Unpacked;
	}
	
	public Iso8583Packed mappingIntoIso8583Packed() {
		Iso8583Packed isoPacked = new Iso8583Packed();
		isoPacked.setIso8583Message(iso8583Service.getPackedIsoMessage());
		return isoPacked;
	}

}
