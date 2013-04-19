package archdearchengine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

class ArchivalUtil{
	
	
	public static String[] messageByteArrayToBinary( byte[] bytes ) {
		
		ArrayList<String> arrayList= new ArrayList<String>();
		
		StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
		
		for( int i = 0; i < Byte.SIZE * bytes.length; i++ ) {
			sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
			arrayList.add(sb.toString());
			sb = new StringBuilder(bytes.length * Byte.SIZE);
//			if(i == Integer.MAX_VALUE / 2) {
//				arrayList.add(sb.toString());
//				sb = new StringBuilder(bytes.length * Byte.SIZE);
//			}
			
		}
		
		String[] s = arrayList.toArray(new String[arrayList.size()]);
		
		return s;
	}
	
	public static byte[] binaryToByteArray(String s){
		int sLen = s.length();
		byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
		char c;
		for( int i = 0; i < sLen; i++ )
			if( (c = s.charAt(i)) == '1' )
				toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
			else if ( c != '0' )
				throw new IllegalArgumentException();
		return toReturn;
	}
	
	public static String[] getBinaryFromPath(String file) {
	
		Path filePath = Paths.get(file);
		byte[] fileByteArray = null;
		
		try{
			fileByteArray = Files.readAllBytes(filePath);
		}
		catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		String[] fileBinary = messageByteArrayToBinary(fileByteArray);
		
		return fileBinary;
	}
	
	public static boolean writeToFile(String file, String data) {
		
		try {
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(data);
			//Close the output stream
			out.close();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
		
	public static String charStringToBinary(String charString) {
		
		byte[] bytes = charString.getBytes();
		
		StringBuilder binary = new StringBuilder();
		
		for (int z = 0; z < bytes.length; z++){
			byte val = bytes[z];
			
			for (int i = 0; i < 8; i++){
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
			
		}
		
		System.out.println("binary length" + binary.length());
		System.out.println("header to binaryCC: " + binary.toString());
		
		
		
		return binary.toString();
	}
	
	public static String binaryToCharString(String binary) {
		String builder = "";
		String returnString = "";
		int ascii;
		
		for(int i = 0; i < binary.length() - 8; i+=8) {
			builder = "";
			
			builder = binary.substring(i, i + 8);
			
			ascii = Integer.parseInt(builder, 2);
			
			System.out.println("ascii character" + ascii);
			
			returnString = returnString + new Character((char)ascii).toString();
			
		}
		
		return returnString;
	}
	
	
	public static byte[] arrayListToByteArray(ArrayList<Byte> arrayList) {
		int size = arrayList.size();
		byte[] bytes = new byte[size];
		Byte temp;
		
		
		
		
		for(int i = 0; i < size; i++) {
			temp = arrayList.get(i);
			
			bytes[i] = temp.byteValue();
			
		}
		
		return bytes;
	}
}




/**Copied from Stephen Repper, Converts bit string to array of bytes*/
/*public  byte[] bitStringToByteArray(String s){
	int sLen = s.length();
	byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
	char c;
	
	for( int i = 0; i < sLen; i++ ) {
	
		if( (c = s.charAt(i)) == '1' ) {
			toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
		}else if ( c != '0' ) {
			throw new IllegalArgumentException();
		}
			
	}
		
	return toReturn;
}

/**Copied from Stephen Repper, Converts array of bytes into a string of binary*/
//TODO fix this thing!
//public  String byteArrayToBinary( byte[] bytes ){
//	StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
//	
//	for( int i = 0; i < Byte.SIZE * bytes.length; i++ ) {
//		sb.append(
//				(bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
//	}
//	
//	String returnString = "";
//	for(int i = 0; i < bytes.length; i++) {
//		System.out.println("bytes" +  bytes[i]);
//	}
//	for(int i = 0; i < bytes.length; i++) {
//		int integerValue = 0;
//		integerValue = Integer.parseInt("" + bytes[i]);
//		returnString = returnString + Integer.toBinaryString(bytes[i]);
//	}
//		
//	return returnString;
//}

/**From Stack Overflow, Converts character string to bit string*/
/*public  String charStringToBits(String charString) {
	
//	for(int i = 0; i < charString.length(); i++) {
//		Integer.parseInt("" + charString.charAt(i));
//		Integer.toBinaryString(integer)
//	}
	
	
	byte[] bytes = charString.getBytes();
	
	StringBuilder binary = new StringBuilder();
	
	for (int z = 0; z < bytes.length; z++){
		int val = bytes[z];
		
		for (int i = 0; i < 8; i++){
			binary.append((val & 128) == 0 ? 0 : 1);
			val <<= 1;
		}
		
	}
//	System.out.println(" + header to binary: " + binary);
	
	return binary.toString();
}

/**Converts filename into binary string*/
/*public String getBinaryFromPath(String file) {
	
	Path filePath = Paths.get(file);
	byte[] fileByteArray = null;
	
	try{
		fileByteArray = Files.readAllBytes(filePath);
	}
	catch (IOException e){
		e.printStackTrace();
		System.exit(1);
	}
	
	String fileBinary = byteArrayToBinary(fileByteArray);
	
	return fileBinary;
}

public  String binaryToText(String binary) {
	
	String builder = "";
	String returnString = "";
	int ascii;
	
	for(int i = 0; i < binary.length() - 7; i+=8) {
		builder = "";
		
		for(int j = i; j < i + 8 ; j++) {
			builder = builder + binary.charAt(j);
			System.out.println("binary.length() " + binary.length() + " j" + j);
		}
		
		ascii = Integer.parseInt(builder, 2);
		
		System.out.println("ascii character" + ascii);
		
		returnString = returnString + new Character((char)ascii).toString();
		
	}
	
	return returnString;
}

*/