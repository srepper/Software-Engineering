package arch_dearchive_engine;

class ArchivalUtil{
	
	/**Copied from Stephen Repper, Converts bit string to array of bytes*/
	public static byte[] bitStringToBinary(String s)
	{
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
	
	/**Copied from Stephen Repper, Converts array of bytes into a string of binary*/
	public static String byteArrayToBinary( byte[] bytes )
	{
 		StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
		for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
			sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
		return sb.toString();
	}
	
	/**From Stack Overflow, Converts character string to bit string*/
	public static String charStringToBits(String charString) {
		
		byte[] bytes = charString.getBytes();
		
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
