package com.ibm.aiops.core;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.ibm.aiops.utils.JSchWrapper;

//@Component
public class RunMain {

	@Value("${conn.host}")
	private String host;
	@Value("${conn.port}")
	private int port;
	@Value("${conn.user.name}")
	private String userName;
	@Value("${conn.password}")
	private String password;
	@Value("${conn.dir.path}")
	private String remoteFilePath;
	@Value("${conn.file.name}")
	private String remoteFileName;
	@Value("${local.dir.path}")
	private String localDirPath;
	
	@Value("${conn.file.download}")
	private boolean isDownload;
	
	@PostConstruct
	private void execute() {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		JSchWrapper jsch = new JSchWrapper();
		
		boolean isFtp = false;
		
		if(isDownload) {
			
			try {
				
				jsch.connectSFTP(host, port, userName, password);

				jsch.downloadFile(remoteFilePath + remoteFileName, localDirPath, true);
				
				isFtp = true;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				
				try {
					jsch.disconnectSFTP();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				stopWatch.stop();
				System.out.println("수행시간: " + stopWatch.getTime() + " ms");
				
			}
			
		} else {
			isFtp = true;
		}
		
		
		if(isFtp) {
			
			try {
			
				JsonNode jsonTree = new ObjectMapper().readTree(new File(localDirPath + remoteFileName));
			
//				System.out.println(jsonTree.get("users"));
				
				Builder csvSchemaBuilder = CsvSchema.builder();
				JsonNode firstObject = jsonTree.elements().next();
				
				System.out.println(firstObject);
				
//				firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
//				CsvSchema csvSchema = csvSchemaBuilder
//						.addColumn("userId", CsvSchema.ColumnType.NUMBER)
//						.addColumn("firstName", CsvSchema.ColumnType.STRING)
//						.addColumn("lastName", CsvSchema.ColumnType.STRING)
//						.addColumn("phoneNumber", CsvSchema.ColumnType.STRING)
//						
//						.build().withHeader();
//				
//				
//				CsvMapper csvMapper = new CsvMapper();
//				csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN,true);
//				
//		        csvMapper.writerFor(JsonNode.class)
//		            .with(csvSchema)
//		            .writeValue(new File(localDirPath + "orderLines.csv"), jsonTree);
		        
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
