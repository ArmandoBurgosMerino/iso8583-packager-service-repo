package com.posdemoproject.microservices.iso8583packagerservice.configuration;

import java.util.ArrayList;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.posdemoproject.microservices.iso8583packagerservice.service.DataField;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
@ConfigurationProperties("iso-packager-service")
public class Iso8583Configuration {
	
	private ArrayList<DataField> iso8583DataFields = new ArrayList<>();
	
	public Iso8583Configuration() {
		
	}
	
}
