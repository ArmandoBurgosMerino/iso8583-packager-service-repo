package com.posdemoproject.microservices.iso8583packagerservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.posdemoproject.microservices.iso8583packagerservice.dto.Iso8583Packed;
import com.posdemoproject.microservices.iso8583packagerservice.dto.Iso8583Unpacked;
import com.posdemoproject.microservices.iso8583packagerservice.mapper.Iso8583Mapper;

@RestController
public class Iso8583Controller{
	
	@Autowired
	private Iso8583Mapper mapper;
	
	
	@PostMapping("/iso8583/v1/unpacking")
	@ResponseBody
	public ResponseEntity<Iso8583Unpacked> unpackingIso8583(@RequestBody final Iso8583Packed iso8583Packed) {	
		mapper.mappingPackedMessageIntoIsoService(iso8583Packed);
		Iso8583Unpacked isoUnpacked = new Iso8583Unpacked();
		isoUnpacked = mapper.mappingIntoIso8583Unpacked();
		return new ResponseEntity(isoUnpacked,HttpStatus.OK);
	}
	
	
	@PostMapping("/iso8583/v1/packing")
	@ResponseBody
	public ResponseEntity<Iso8583Packed> packingIso8583(@RequestBody final Iso8583Unpacked iso8583Unpacked){
		mapper.mappingUnpackedMessageIntoIsoService(iso8583Unpacked);
		Iso8583Packed isoPacked = new Iso8583Packed();
		isoPacked = mapper.mappingIntoIso8583Packed();
		return new ResponseEntity(isoPacked,HttpStatus.OK);
	}
	
	//using hateoas
	@PostMapping("/iso8583/v2/unpacking")
	@ResponseBody
	public EntityModel<Iso8583Unpacked> unpackingIso8583Hateoas(@RequestBody final Iso8583Packed iso8583Packed) {	
		mapper.mappingPackedMessageIntoIsoService(iso8583Packed);
		Iso8583Unpacked isoUnpacked = new Iso8583Unpacked();
		isoUnpacked = mapper.mappingIntoIso8583Unpacked();
			
		EntityModel<Iso8583Unpacked> entityModel = EntityModel.of(isoUnpacked);
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).packingIso8583Hateoas(isoUnpacked));
		entityModel.add(link.withRel("iso8583_services"));
			
		return entityModel;
		
	}
	
	@PostMapping("/iso8583/v2/packing")
	@ResponseBody
	public EntityModel<Iso8583Packed> packingIso8583Hateoas(@RequestBody final Iso8583Unpacked iso8583Unpacked){
		mapper.mappingUnpackedMessageIntoIsoService(iso8583Unpacked);
		Iso8583Packed isoPacked = new Iso8583Packed();
		isoPacked = mapper.mappingIntoIso8583Packed();
		
		EntityModel<Iso8583Packed> entityModel = EntityModel.of(isoPacked);
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).unpackingIso8583Hateoas(isoPacked));
		entityModel.add(link.withRel("iso8583_services"));
		
		return entityModel;
	}

}
