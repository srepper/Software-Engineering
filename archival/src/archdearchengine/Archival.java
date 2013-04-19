package archdearchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**Archival requires a string array of the file names to archive. can be switched to an array list easily.
 * returns a byte array that the caller must write to file using the output file name gotten from gui*/
public class Archival {
	public static final String headerConstant = "ArCh!vALt3&m";
	
	
	public byte[] archive(String[] files) throws IOException {
		ArrayList<byte[]> fileBytesWithHeader = new ArrayList<byte[]> (0);
		for(int i = 0; i < files.length; i ++)
		{
			Path filePath = Paths.get(files[i]);
			byte[] fileBinary = Files.readAllBytes(filePath);
			byte[] fileWithHeader = addHeader(filePath, fileBinary);
			fileBytesWithHeader.add(fileWithHeader);
			
		}
		int totalSize = 0;
		for(int i = 0; i < fileBytesWithHeader.size(); i ++ )
		{
			totalSize += fileBytesWithHeader.get(i).length;
		}
		byte[] totalFile = new byte[totalSize];
		int counter = 0;
		for(int i = 0; i < fileBytesWithHeader.size(); i ++)
		{
			for(int j = 0; j < fileBytesWithHeader.get(i).length; j++)
			{
				totalFile[counter + j] = fileBytesWithHeader.get(i)[j];
				
			}
			counter += fileBytesWithHeader.get(i).length;
		}
//		Path filePath = Paths.get(file);
//		byte[] fileBinary = Files.readAllBytes(filePath);
//		byte[] fileWithHeader = addHeader(filePath, fileBinary);
//		
		
		/**
		 * Chalrles' code
		 * String[] fileBinary = ArchivalUtil.getBinaryFromPath(file);
		String[] archiveString = addHeader(file, fileBinary);
		
		ArrayList<Byte> arrayList = createArrayList(archiveString);
		
		byte[] bytes = ArchivalUtil.arrayListToByteArray(arrayList);
		
		return bytes;*/
//		System.out.println("fileWIthHeader.toString() : " + fileWithHeader.toString());
		return totalFile;
	}
	public byte[] archive(ArrayList<String> files) throws IOException {
		ArrayList<byte[]> fileBytesWithHeader = new ArrayList<byte[]> (0);
		for(int i = 0; i < files.size(); i ++)
		{
			Path filePath = Paths.get(files.get(i));
			byte[] fileBinary = Files.readAllBytes(filePath);
			byte[] fileWithHeader = addHeader(filePath, fileBinary);
			fileBytesWithHeader.add(fileWithHeader);
			
		}
		int totalSize = 0;
		for(int i = 0; i < fileBytesWithHeader.size(); i ++ )
		{
			totalSize += fileBytesWithHeader.get(i).length;
		}
		byte[] totalFile = new byte[totalSize];
		int counter = 0;
		for(int i = 0; i < fileBytesWithHeader.size(); i ++)
		{
			for(int j = 0; j < fileBytesWithHeader.get(i).length; j++)
			{
				totalFile[counter + j] = fileBytesWithHeader.get(i)[j];
				
			}
			counter += fileBytesWithHeader.get(i).length;
		}
//	
		return totalFile;
	}
	
	private byte[] addHeader(Path file, byte[] fileToStuff) {
		byte[] byteHeader =  headerConstant.getBytes();
		byte[] byteFileName = file.toString().getBytes();
		
		byte[] header = new byte[byteHeader.length*2 + byteFileName.length];
		
		//copy the header, the file name, and then the header over to make the 'final' header
		for(int i = 0; i < byteHeader.length; i ++)
		{
			header[i] = byteHeader[i];
		}
		for(int i = 0; i < byteFileName.length; i ++ )
		{
			header[i + byteHeader.length] = byteFileName[i];
		}
		for(int i = 0; i < byteHeader.length; i ++ )
		{
			header[i+byteHeader.length+byteFileName.length] = byteHeader[i]; 
		}
		byte[] fileWithHeader = new byte[header.length + fileToStuff.length + byteHeader.length];
		for(int i = 0; i < header.length; i ++)
		{
			fileWithHeader[i] = header[i];
		}
		for(int i = 0; i < fileToStuff.length; i ++)
		{
			fileWithHeader[i + header.length] = fileToStuff[i];
		}
		for(int i = 0; i < byteHeader.length; i ++)
		{
			fileWithHeader[i + header.length + fileToStuff.length] = byteHeader[i];
		}
		return fileWithHeader;
/** charles' code
 * 		header = ArchivalUtil.charStringToBinary(header);
		String headerConstantBin = ArchivalUtil.charStringToBinary(headerConstant);
		
		header = headerConstantBin + header + headerConstantBin;
		
		String[] combination = new String[bitStuffing.length + 1];
		combination[0] = header;
		
		for(int i = 1; i < combination.length; i++) {
			combination[i] = bitStuffing[i - 1];
		}
		
		return combination;
*/		
	}
	
	private ArrayList<Byte> createArrayList(String[] archiveString){
		ArrayList<Byte> arrayList = new ArrayList<Byte>();
		String temp;
		byte[] bytes;
		
		for(int i = 0; i < archiveString.length; i++) {
			temp = ArchivalUtil.charStringToBinary(archiveString[i]);
			bytes = ArchivalUtil.binaryToByteArray(temp);
			
			for(int j = 0; j < bytes.length; j++) {
				arrayList.add(new Byte(bytes[j]));
			}
			
		}
		
		return arrayList;
	}
	

	

	

	
}