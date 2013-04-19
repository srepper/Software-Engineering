package CompressDecompressEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress {

	void decompressFile(String inputFile) {
		try {
			byte[] buffer = new byte[1024];
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(inputFile));
			// get the zipped file list entry
			ZipEntry zipEntry = zipInputStream.getNextEntry();

			while (zipEntry != null) {

				String fileName = zipEntry.getName();
				File newFile = new File(fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zipInputStream.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				zipEntry = zipInputStream.getNextEntry();
			}

			zipInputStream.closeEntry();
			zipInputStream.close();

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
