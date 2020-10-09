package com.dma.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public class Test22 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Path path = Paths.get("/home/dma/dma/cda4");
		
		File dir = new File(path + "/actionLogs");

		
		Path file = Paths.get(path + "/actionLogs.json");
		
		List<String> actionLogList = new ArrayList<String>();
		
		if(Files.exists(file)) {			
			@SuppressWarnings("unchecked")
			Map<String, Object> actionLogMap =  (Map<String, Object>) Tools.fromJSON(file.toFile(), new TypeReference<Map<String, Object>>(){});
			for(Entry<String, Object> al: actionLogMap.entrySet()) {
				Path alPath = Paths.get(dir + "/" + al.getKey());
				if(Files.exists(alPath)) {
					List<String> lines = Files.readAllLines(alPath);
					actionLogList.add(String.join("", lines));
				}
			}
		}		

		System.out.println(actionLogList);
		
		System.out.println(Tools.toJSON(actionLogList));
		
		ZipUtil.pack(dir, new File("/tmp/demo.zip"), new NameMapper() {
			  public String map(String name) {
			    return "foo/" + name;
			  }
		});		
		
		
		
	}

}
