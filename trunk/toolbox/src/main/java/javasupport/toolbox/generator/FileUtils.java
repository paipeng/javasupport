package javasupport.toolbox.generator;

import java.io.File;

public class FileUtils {
	public static String getFilePathname(File file){
		if(file.isAbsolute()) 
			return file.getAbsolutePath();
	    else{
	      File f = file; //init file path name
	      StringBuilder sb = new StringBuilder(f.getName());
	      while(f.getParentFile() != null){
	        f = f.getParentFile();
	        sb.insert(0, f.getName()+File.separator);
	      }
	      return sb.toString();
	    }
	}
}
