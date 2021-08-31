package com.ibm.aiops.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import com.ibm.aiops.vo.CsvVo;
import com.ibm.aiops.vo.ResponseVo;
import com.ibm.aiops.vo.ResponseVo2;

@Component
public class RunMain2 {

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
		
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(localDirPath + "test_json2"));
 
			// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
			JSONObject jsonObject = (JSONObject) obj;
 
			// A JSON array. JSONObject supports java.util.List interface.
			JSONArray companyList = (JSONArray) jsonObject.get("users");
 
			// An iterator over a collection. Iterator takes the place of Enumeration in the Java Collections Framework.
			// Iterators differ from enumerations in two ways:
			// 1. Iterators allow the caller to remove elements from the underlying collection during the iteration with well-defined semantics.
			// 2. Method names have been improved.
			Iterator<JSONObject> iterator = companyList.iterator();
			
			Gson gson = new Gson();
			List<CsvVo> csvList = new ArrayList<>();
			
			while (iterator.hasNext()) {
				
				JSONObject jObj = iterator.next();
				
				String type = (String) jObj.getOrDefault("type", "");

				
				if(type.equals("A")) {
					ResponseVo imgVO = gson.fromJson(jObj.toJSONString(), ResponseVo.class);
//					System.out.println(imgVO.toString());
					
					CsvVo vo = new CsvVo();
					vo.setCol1(imgVO.getType());
					vo.setCol2(imgVO.getUserId());
					
					csvList.add(vo);
				} else if(type.equals("B")) {
					ResponseVo2 imgVO = gson.fromJson(jObj.toJSONString(), ResponseVo2.class);
//					System.out.println(imgVO.toString());
					
					CsvVo vo = new CsvVo();
					vo.setCol1(imgVO.getType());
					vo.setCol2(imgVO.getUserId());
					
					csvList.add(vo);
					
				} else {
					
				}
			
			}
			
			for(CsvVo v : csvList) {
				
				System.out.println(v.toString());
			}
			
					
			File csvFile = new File(localDirPath + "filename.csv"); 
			csvFileOut(CsvVo.class, csvFile, csvList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	      
		
		
	}

	public void csvFileOut(Class<?> clazz, File csvFile, List<?> dataList) {
		CsvMapper csvMapper = new CsvMapper();

		CsvSchema csvSchema = csvMapper.enable(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS) .schemaFor(clazz).withHeader().withColumnSeparator(',').withLineSeparator("\n"); ObjectWriter writer = csvMapper.writer(csvSchema); OutputStreamWriter outputStreamWriter;
		try {
			outputStreamWriter = new OutputStreamWriter(new FileOutputStream(csvFile), "MS949");
		
			writer.writeValues(outputStreamWriter).writeAll(dataList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
