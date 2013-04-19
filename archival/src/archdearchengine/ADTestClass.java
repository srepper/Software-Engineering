package archdearchengine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ADTestClass
{
	public static void main(String[] args) throws Exception {
		Archival archival = new Archival();
		Dearchival dearchival = new Dearchival();
		
		File testFile = new File(args[0]);
		System.out.println("test file name:" + testFile.getName());
		System.out.println("absolute path:" + testFile.getAbsolutePath());
		
//		String fileName = testFile.getName();
		String[] fileName = {"test1.txt", "test2.txt"};
//		byte[] archive = archival.archive(fileName);
		
//		String archiveString = ArchivalUtil.byteArrayToBinary(archive);
//		ArchivalUtil.writeToFile(fileName + "_archived", archiveString);
//		System.out.println("archive length: " + archive.length);
//		Files.write(Paths.get(fileName[0] + "_archived"), archive);
		
		byte[] dearchive = dearchival.dearchive(fileName[0] + "_archived");
		
//		byte header = dearchive[0];
		
//		System.out.println("header " + Byte.toString((byte)header));
		
		
		
//		String dearchiveString = ArchivalUtil.byteArrayToBinary(dearchive);
//		ArchivalUtil.writeToFile(fileName, dearchiveString);
		
	}
}
