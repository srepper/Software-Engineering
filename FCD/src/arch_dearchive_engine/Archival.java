package arch_dearchive_engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Archival {
	
	
	public String archive(String file) {
		
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
		String header = "This Archival Is Amazing";
		
		String bitStuffing = stuffBits(fileBinary);
		bitStuffing = addHeader(header, bitStuffing);
		
		return bitStuffing;
	}
	
	private String stuffBits(String fileBinary) {
		
		String bitStuffing = new String(fileBinary);
		
		bitStuffing = bitStuffing.replaceAll("11111", "111110");
		
		return bitStuffing;
	}
	
	private String addHeader(String header, String bitStuffing) {
		header = convertHeaderToBinary(header);
		
		header = "111111" + header + "111111";
		
		bitStuffing = header + bitStuffing;
		return bitStuffing;
	}
	
	private String convertHeaderToBinary(String header) {
		
		byte[] bytes = header.getBytes();
		
		StringBuilder binary = new StringBuilder();
		
		for (byte b : bytes){
			int val = b;
			
			for (int i = 0; i < 8; i++){
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
			
//			binary.append(' ');
		}
//		System.out.println("'" + header + "' to binary: " + binary);
		
		return binary.toString();
	}
	
}


class FileManagerDriver{
	private File sourceFile;
	private File targetFile;
	
	public FileManagerDriver(File sourceFile, File targetFile) {
		this.sourceFile = sourceFile;
		this.targetFile = targetFile;
	}

	public File getSourceFile(){
		return sourceFile;
	}

	public File getTargetFile(){
		return targetFile;
	}
	
	
}


