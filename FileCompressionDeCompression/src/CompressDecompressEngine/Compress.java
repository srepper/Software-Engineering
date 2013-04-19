package CompressDecompressEngine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress {
	
		public Compress()
		{
			
		}
		void compressFile(String fileName)
		{
			byte[] buffer = new byte[1024];
			 
	    	try{
	 
	    		FileOutputStream fileOutputStream = new FileOutputStream(fileName +"_compressed");
	    		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
	    		ZipEntry zipEntry= new ZipEntry(fileName);
	    		zipOutputStream.putNextEntry(zipEntry);
	    		FileInputStream in = new FileInputStream(fileName);
	 
	    		int len;
	    		while ((len = in.read(buffer)) > 0) {
	    			zipOutputStream.write(buffer, 0, len);
	    		}
	 
	    		in.close();
	    		zipOutputStream.closeEntry();
	 
	    		//remember close it
	    		zipOutputStream.close();
	 
	    		System.out.println("Done");
	 
	    	}catch(IOException ex){
	    	   ex.printStackTrace();
	    	}
		}
}
