package com.dma.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

public class Test18 {

	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Path path = Paths.get("/home/dma/dma/cda0/models/mdl0.json");
		Path output = Paths.get("/home/dma/dma/cda0/models/mdl0-migrated.json");
		
		try {
			List<QuerySubject> qss = (List<QuerySubject>) Tools.fromJSON(path.toFile(), new TypeReference<List<QuerySubject>>(){});
			
			for(QuerySubject qs: qss) {
				
				String filter = qs.getFilter();
				if(filter.length() > 0) {
					System.out.println(filter);
				}
			
			}
			
			Files.write(output, Tools.toJSON(qss).getBytes());

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
