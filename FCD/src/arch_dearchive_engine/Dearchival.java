package arch_dearchive_engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Dearchival
{
	public void dearchive(String file) {
		
		Path filePath = Paths.get(file);
		byte[] fileByteArray = null;
		
		try{
			fileByteArray = Files.readAllBytes(filePath);
		}
		catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		String fileBinary = ArchivalUtil.byteArrayToBinary(fileByteArray);
		
		fileBinary = new String(fileBinary);
		
		String[] extract = fileBinary.split("111111");
		
		String header, message;
		
		if(extract.length == 2) {
			header = extract[0];
			message = extractMessage(extract[1]);
		}else {
			throw new NullPointerException("Error with the length of extracted file for dearchival");
		}
		
		System.out.println("Header:" + header);
		System.out.println("Message:" + message);
		
		
	}
	
//	private String extractHeader(String bitStuffing) {
//		String temp; 
//		
//		header = "111111" + header + "111111";
//		
//		bitStuffing = header + bitStuffing;
//		return bitStuffing;
//	}
	
	private String extractMessage(String fileBinary) {
		
		String bitStuffing = new String(fileBinary);
		
		bitStuffing = bitStuffing.replaceAll("111110", "11111");
		
		return bitStuffing;
	}
	


}
