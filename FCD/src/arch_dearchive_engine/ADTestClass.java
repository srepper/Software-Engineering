package arch_dearchive_engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ADTestClass
{
	public static void main(String[] args) {
		Archival archival = new Archival();
		Dearchival dearchival = new Dearchival();
		
		File testFile = new File(args[0]);
		System.out.println("test file name:" + testFile.getName());
		System.out.println("absolute path:" + testFile.getAbsolutePath());
		
		Path path = Paths.get("archivalT1.txt");

		System.out.println("path to string: " + path.getFileName());
		byte[] byteArray = null;
		
		try{
			path = Paths.get(testFile.getAbsolutePath());
			byteArray = Files.readAllBytes(path.toAbsolutePath());
		}
		catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		String file = ArchivalUtil.byteArrayToBinary(byteArray);
		
		
		String archive = archival.archive(file);
		dearchival.dearchive(file);
		
	}
}
