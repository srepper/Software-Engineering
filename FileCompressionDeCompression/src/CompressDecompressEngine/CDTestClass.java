package CompressDecompressEngine;

public class CDTestClass {

	public static void main(String[] args)
	{
		Compress comp = new Compress();
		Decompress decomp = new Decompress();
//		comp.compressFile("FileCompressor.txt");
		decomp.decompressFile("FileCompressor.txt_compressed");
	}
}
