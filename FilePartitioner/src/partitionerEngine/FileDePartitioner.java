
import java.util.Vector;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import javax.swing.JOptionPane;


public class FileDePartitioner {
	
	static Vector<Path> fileArray;
	static Vector<byte[]> fileArrayByte = new Vector<byte[]>(0);
	static byte[] outputArray;
	static Path outputFile;
	public static void main(String[] args) throws Exception
	{
		
		Vector<Path>fileArray = new Vector<Path>(0);
		for(int i = 9; i >= 0; i --)
		{
			fileArray.add(Paths.get(args[0] + i +".part"));
		}
		FileDePartitioner.departitionFile(fileArray);
	}
	static boolean departitionFile(Vector<Path>fileArray) throws Exception
	{
		for(int i = 0; i < fileArray.size(); i++)
		{
			try {
				fileArrayByte.add(Files.readAllBytes(fileArray.get(i)));
			} catch (IOException e) {
				fileReadError();
			}
		}
		
		int numberPartitions = 0;
		for(int i = 0 ; i < fileArrayByte.size(); i ++)
		{

			if(fileArrayByte.get(i)[0] == Byte.valueOf("0"))
			{
				numberPartitions = Integer.parseInt("" +fileArrayByte.get(i)[1]);

				
			}
		}
		int counter = 0;
		for(int i = 0; i <fileArrayByte.size(); i ++)
		{
			counter += fileArrayByte.get(i).length;
		}
		//subtract 1 byte per array for the header, and then 1 extra for the double header on #1
		counter = counter - (fileArrayByte.size()) - 1;
		if(numberPartitions != fileArrayByte.size())
			throw new Exception ("hey, you didnt give me all the files");
		
		outputArray = new byte[counter];
		counter = 0;
		for(int i = 0; i < fileArrayByte.size(); i ++)
		{
			for(int j = 0; j < fileArrayByte.size(); j++)
			{
				if(fileArrayByte.get(j)[0] == Byte.valueOf("" + i))
				{
					for(int k = (fileArrayByte.get(i)[0] == Byte.valueOf("0")?2:1);k < fileArrayByte.get(j).length	; k ++)
					{
						outputArray[counter++] = fileArrayByte.get(j)[k];
					}
				}
			}
		}
		
		
		
		Pattern p = Pattern.compile("(.*?)(\\d+).(part)");
		Matcher m = p.matcher(fileArray.get(0).toString());
		if(m.matches())
		{
			outputFile = Paths.get(m.group(1));
			Files.write(outputFile, outputArray);
		}
		
		
		return false;
	}
	/***************************************************************************
	 * Error when file cannot be read to fileArray
	 **************************************************************************/
	private static void fileReadError()
	{
		JOptionPane.showMessageDialog(null, "Failed to read input file.",  
				"Results", JOptionPane.ERROR_MESSAGE);
	}

}
