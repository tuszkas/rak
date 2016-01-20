package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Menu {

	public static String getMenu() {
		BufferedReader br;
		String menu = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					"C:/Users/Marcin/kti/rak/src/resources/menu.txt"), "UTF8"));
			String line = br.readLine();
			while (line != null && !line.equals("")){
				menu = menu + line;
			    line = br.readLine();
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			menu = "UnsupportedEncodingException";
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			menu = "FileNotFoundException";
			e.printStackTrace();
		} catch (IOException e) {
			menu = "IOException";
			e.printStackTrace();
		}
		return menu;
	}
	
	public static void addItem(String itemText) {
		BufferedWriter br;
		try {
			br = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("C:/Users/Marcin/kti/rak/src/resources/menu.txt", true),
							"UTF8"));
			br.write(" \n" + itemText.substring(6));
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getMenuItem(int index) {
		//TODO: unimplemented method
		return null;
	}
	
	public static void deleteMenuItem(int index) {
		//TODO: unimplemented method
	}
	
	public static void editMenuItem(int index, String newItemText) {
		//TODO: unimplemented method
	}
}
