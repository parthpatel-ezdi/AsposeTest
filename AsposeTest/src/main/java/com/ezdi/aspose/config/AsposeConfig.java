/**
 * 
 */
package com.ezdi.aspose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspose.words.License;

/**
 * @author parth.m
 *
 */

@Configuration
public class AsposeConfig {
	
	@Bean
	public License license(){
		License license = new com.aspose.words.License();
		try {
			license.setLicense("/home/local/EZDI/parth.m/Downloads/Aspose.Words.lic");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return license;
	}
}
