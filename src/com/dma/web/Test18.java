package com.dma.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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
				
				Map<String, Filter> filters = qs.getFilters();
				
				String filter = qs.getFilter();
				if(filter.length() > 0) {
					System.out.println(filter);
					if(filter.startsWith("[FINAL]") && !filter.contains(";")) {
						Filter flt = new Filter();
						flt.setName("flt0");
						flt.setTarget(filter.substring(0, filter.lastIndexOf("]") + 1));
						flt.setOption("Mandatory");
						flt.setExpression(filter);
						filters.put(flt.getName(), flt);
						System.out.println(Tools.toJSON(filters));
					}
					if(filter.startsWith("[REF]") && !filter.contains(";")) {
						Filter flt = new Filter();
						flt.setName("flt0");
						flt.setTarget(filter.substring(0, filter.indexOf(":")));
						flt.setOption("Mandatory");
						flt.setExpression(filter.substring(filter.indexOf(":") + 1));
						filters.put(flt.getName(), flt);
						System.out.println(Tools.toJSON(filters));
					}
					if(filter.startsWith("[REF]") && filter.contains(";")) {
						
					}
					
				}
			
			}
			
			Files.write(output, Tools.toJSON(qss).getBytes());

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
