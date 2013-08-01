import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;


public class Dir {
	private String loc;
	private ArrayList<IndFile> fileList = new ArrayList<IndFile>();
	private HashMap hmMD5ToFileName = new HashMap();

	public Dir () {
		loc = ".";	
		
		getDirDetail();
	}
	
	public Dir (String inLoc) {
		loc = inLoc;
		getDirDetail();
	}
	
	public int getFileSize () {
		int s = fileList.size();
		return s;
	}
	
	public IndFile getFile(int index) {
		IndFile file = null;
		try {
			file = fileList.get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Index out of bound for directory "+loc+".  Returning null.");
		}
		return file;
	}
	
	public String getLoc() {
		return loc;
	}
	
	public void Print(Boolean fullPath) {
		
		Iterator<IndFile> it = fileList.iterator();
		while(it.hasNext())	{
		    IndFile obj = it.next();
		    if (fullPath) {
		    	System.out.println("Ret: " + obj.toString(loc));
		    } else {
			    System.out.println("Ret: " + obj.toString());
	    	
		    }
		}
		
	}
	private void getDirDetail() {
		String files;
		
		File folder = new File(loc);
		File[] listOfFiles = folder.listFiles();
		
		for (int i=0; i< listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				
				String md5Hex = "";
				long l = 0;
				try {
				  HashCode md5 = Files.hash(listOfFiles[i], Hashing.md5());

				  md5Hex = md5.toString();
				  
				  l = listOfFiles[i].length();
				  
				} catch (IOException e) {
					System.out.println("Cant find file.");
				}
				finally {
				}
				IndFile f = new IndFile(files,md5Hex,l);
				fileList.add(f);
				hmMD5ToFileName.put(md5Hex, f.getFilename());
			}
		}

	}
	public String findFileByMD5(String md5) {
		String filename = "";
		filename = (String)hmMD5ToFileName.get(md5);
		return filename;
	}
}
