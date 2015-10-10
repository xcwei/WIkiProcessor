package util;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class IO {
	public static ArrayList FileRead(String path) throws IOException{
		ArrayList content = new ArrayList<String>();
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		while((line = br.readLine())!=null){
			content.add(line);
		}
		br.close();
		isr.close();
		fis.close();
		return content;
	}
}
