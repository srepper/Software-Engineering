package archdearchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**Dearchival only requires the string of the file to dearchive. as of now it doesnt return anything */
public class Dearchival {

	
	public byte[] dearchive(String fileName) throws Exception {
		ArrayList<String> fileNames = new ArrayList<String>(0);
		ArrayList<byte[]> fileBytes = new ArrayList<byte[]>(0);
		
		Path filePath = Paths.get(fileName);
		byte[] fileByte = Files.readAllBytes(filePath);
		
		extract(fileNames,fileBytes,fileByte);
		System.out.println("fileNames.size(): " + fileNames.size() + "\nfileBytes.size(): " + fileBytes.size());
		
		if(fileNames.size() != fileBytes.size())
		{
			throw new Exception("Erorr while dearchiving");
		}
		
		for(int i = 0 ; i < fileNames.size(); i ++)
		{
			Files.write(Paths.get(fileNames.get(i)), fileBytes.get(i));
		}
		
/** charles' code
 		String[] conversion = dearchiveByteArrayToBinary(fileBytes);
		
		String header = extractHeader(conversion);
		String[] message = extractMessage(conversion);
		
		System.out.println("Header" + header);
		System.out.println("message:");
		
		ArrayList<Byte> arrayList = new ArrayList<Byte>();
		
		for(int i = 0; i < message.length; i++) {
			byte[] bytes = ArchivalUtil.binaryToByteArray(message[i]);
			
			for(int j = 0; j < bytes.length; j++) {
				arrayList.add(new Byte(bytes[i]));
			}
		}
		
		byte[] bytes = ArchivalUtil.arrayListToByteArray(arrayList);
		
		String[] s = new String[bytes.length];
		
		for(int i = 0; i < bytes.length; i++) {
			
			s[i] = "" + (char)bytes[i];
			System.out.print("s[" + i + "] " + s[i]);
		}
		
//		String messageText = ArchivalUtil.binaryToCharString(message);
//		System.out.println("dearchived message:" + messageText);
		
		return bytes;
*/		
		return null;
	}
	
	private void extract(ArrayList<String> fileNames,ArrayList<byte[]> fileBytes, byte[] totalFile) throws Exception
	{
		//while still reading the file
		//try to match the header delimiter
		//HD filename HD file HD. this pattern will be repeated
		byte[] headerDelim = "ArCh!vALt3&m".getBytes();
		int i = 0;
		while(i < totalFile.length)
		{
			//try to match first HD
			
			//HD
			if(HDfound(totalFile,headerDelim,i))
			{
				i = i + headerDelim.length;
			}
			else
				throw new Exception("error when reading file");
			//filename
			int startHeader = i;
			//hd
			while(!HDfound(totalFile, headerDelim, i))
			{
				i++;
			}
			int endHeader = i;
			i = i + headerDelim.length;
			//file
			int startFile = i;
			//hd
			while(!HDfound(totalFile, headerDelim, i))
			{
				i++;
			}
			int endFile = i;
			i = i + headerDelim.length;
			
			byte[] temp = new byte[endHeader - startHeader];
			for(int j = 0; j < temp.length; j ++)
			{
				temp[j] = totalFile[startHeader+j];
			}
			fileNames.add(new String(temp));
			temp = new byte[endFile - startFile];
			for(int j = 0; j < temp.length; j++)
			{
				temp[j] = totalFile[startFile+j];
			}
			fileBytes.add(temp);
			
		}
	}
	private boolean HDfound (byte[] totalFile, byte[] headerDelim, int index)
	{
		boolean hdFound = true;
		for(int i = 0; i < headerDelim.length; i ++)
		{
			if(totalFile[index+i] != headerDelim[i])
				hdFound = false;
		}
		return hdFound;
	}
	private String[] dearchiveByteArrayToBinary(byte[] fileBytes) {
		
		String[] s = ArchivalUtil.messageByteArrayToBinary(fileBytes);
		
		return s;
				
	}
	
	private String extractHeader(byte[] fileByte) {
		
		String fileString = new String(fileByte);
		
		String[] extract = fileString.split("ArCh!vALt3&m");
		
		if(extract.length != 3)
			throw new NullPointerException("Error with the length of extracted file for dearchival");
		return extract[1];
		
//		Charles' code
//		String header = combination[0];
//		
//		String compareTo = ArchivalUtil.binaryToCharString(header);
//		
//		String[] extract = header.split(compareTo);
//		
//		if(extract.length == 3) {
//			return extract[1];
//		}else {
//			throw new NullPointerException("Error with the length of extracted file for dearchival");
//		}		
	}
	
	private String[] extractMessage(String[] message) {
		ArrayList<String> extract = new ArrayList<String>();
		
		String temp;
		
		for(int i = 1; i < message.length; i++) {
			temp = ArchivalUtil.binaryToCharString(message[i]);
			extract.add(temp);
		}
		
		return extract.toArray(new String[extract.size()]);
	}
	
}
