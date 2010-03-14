package javasupport.toolbox.generator;

import java.io.File;
import java.io.FileFilter;

public class FileUtils {
	
	public static FileFilter DIR_FILTER = new FileFilter(){
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};
	
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
	
	public static interface FileProcess{
		public void process(File file) throws Exception;
	}
	
	public static void walk(File dir, FileFilter filter, FileProcess process) throws Exception {
		for(File file: dir.listFiles(filter)){
			if(file.isDirectory())
				walk(file, filter, process);
			else
				process.process(file);
		}
	}
	public static void eachFile(File dir, FileFilter filter, FileProcess process) throws Exception {
		for(File file: dir.listFiles(filter)){
			if(file.isDirectory())
				eachFile(file, filter, process);
			else
				process.process(file);
		}
	}

	public static void eachDir(File dir, FileFilter filter, FileProcess process) throws Exception {
		for(File file: dir.listFiles(filter)){
			if(file.isDirectory()){
				process.process(file);
				eachDir(file, filter, process);
			}
		}
	}
}
