package com.dma.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public class Test22 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Path path = Paths.get("/home/dma/dma/cda4");
		
		File dir = new File(path + "/actionLogs/.");
		
		if(dir.exists()){
			File[] fs = dir.listFiles();
			
			Arrays.sort(fs, new Comparator<File>() {
			    public int compare(File f1, File f2) {
			        return Long.compare(f1.lastModified(), f2.lastModified());
			    }
			});			
			
			FileOutputStream fout = new FileOutputStream("/tmp/test.zip");
			ZipOutputStream zout = new ZipOutputStream(fout);
			for(File f: fs){
				System.out.println(f.getName());
			    ZipEntry ze = new ZipEntry(f.getName());
			    zout.putNextEntry(ze);
			    zout.closeEntry();
			}
			zout.close();		
		}
		
		
		ZipUtil.pack(dir, new File("/tmp/demo.zip"), new NameMapper() {
			  public String map(String name) {
			    return "foo/" + name;
			  }
			});		
		
		
//		Path curdir = Paths.get(".");
//		System.out.println(curdir.toAbsolutePath());
//		
//		System.setProperty("user.dir", "/home/dma/dma/cda4");
//		
//		System.out.println(System.getProperty("user.dir"));
//		
//		
//		path = Paths.get(System.getProperty("user.dir") + "/actionLogs");
//		Path file = Paths.get(System.getProperty("user.dir") + "/actionLogs.json");
//		@SuppressWarnings("unchecked")
//		Map<String, Object> list = (Map<String, Object>) Tools.fromJSON(file.toFile(), new TypeReference<Map<String, Object>>(){});
//		
//		FileOutputStream fout = new FileOutputStream(System.getProperty("user.dir") + "/test.zip");
//		ZipOutputStream zout = new ZipOutputStream(fout);
//		for(Entry<String, Object> item: list.entrySet()){
//		    ZipEntry ze = new ZipEntry(path + "/" + item);
//		    zout.putNextEntry(ze);
//		    zout.closeEntry();
//		}
//		zout.close();		
		
	}

}
