
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.junit.Test;
import static org.junit.Assert.*;



import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public class FindDup {
	public static void main(String[] args) throws InvalidFileFormatException, IOException {
        OptionSet options = null;

		options = checkOptions(args);

		File file = null;
		
		Properties propConfig = new Properties();
		try {
			propConfig.load(new FileInputStream("config.txt"));
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		File iniFile = new File((String) propConfig.get("forensic"));
		if(!iniFile.exists()) {
		    iniFile.createNewFile();
		} 

		Ini iniForsensic = new Ini(iniFile);

		ArrayList aDirTarget = getDir(propConfig.getProperty("dir"));

		Iterator<String> it = aDirTarget.iterator();
		while(it.hasNext())	{
		    String obj = it.next();
		    Dir directory = new Dir(obj);
		    
		    System.out.println(directory.getLoc() + " " + directory.getFileSize());
		    
		    IndFile f = null;
		    
		    for (int i = 0; i < directory.getFileSize(); i++) {
		    	f = directory.getFile(i);
		    	String fn = new String(directory.getLoc() + "\\" + f.getFilename());
		    	iniForsensic.put(directory.getLoc(),f.getFilename(),f.getMD5() + "," + f.getSize());   	
		    }
//		    directory.Print(true);
		}
		if (options.has("w")) {
			iniForsensic.store();
		}
		if (options.has("v")) {
			verifyForensic(propConfig.getProperty("forensic"));
		}
	}
	
	private static void verifyForensic(String fn) throws IOException{
		Ini iniFor = new Ini();
		iniFor.load(new FileReader(fn));

	}

	private static OptionSet checkOptions(String[] args) {
        OptionParser parser = new OptionParser( "c:wdv::" );
        OptionSet options = null;
        try {
        	options = parser.parse(args);
        } catch (Exception e){
        	
        }
   
        try {
        	if (options.has("c") && options.hasArgument("c")) {
        		String fn = (String) options.valueOf("c");
        		File f = new File(fn);
        		if (!f.exists()) {
        			System.out.println("File does not exist");
        			System.exit(1);
        		}
        	} else {
        		printArguments();	
        		System.exit(1);
        	}
        }	catch (NullPointerException e) {
    		printArguments();	
    		System.exit(1);
        }
        return options;
	}
	
	private static void printArguments() {
		System.out.println("xxx -c config.txt [-w] [-d] [-v [dir;dir2;dir3]]");
		System.out.println();
		System.out.println(" -c: name of config file");
		System.out.println(" -w: write forensic file");
		System.out.println(" -d: find duplicates");
		System.out.println(" -v: verify files with md5 in forensic file");
	}
	
	// return arraylist of string = "dir"
	private static ArrayList getDir(String dirSemiList) {
		ArrayList<String> ret = new ArrayList<String>();

   		String[] splits = dirSemiList.split(";");
   		for (int i=0; i<splits.length; i++) {
   			ret.add(splits[i]);
   		}
    		
    	return ret;
	}
	
	
	private static String findMatchFile(String needle, ArrayList aDirTarget) {
		String ret = null;
		
	String path = "\\temp";
		String files;
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		for (int i=0; i< listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
//				System.out.println(files);
			}
		}
		
		
		return ret;
	}

}



// run: FindDup 
//  -c config.ext
// -w create forensic file
// -d find dup
// -v <dir;dir;dir>   verify md5


// Read config file... 
//		has list of sub 
//		dir=\dir1;dir2
//		forensic=\dir\for.txt --- has filename,md5,size
//



