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

	private Menu() {
		
	}
	
	public static String getMenu() {
		BufferedReader br;
		String menu = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					"C:/Users/Marcin/kti/rak/resources/menu.txt"), "UTF8"));
			String line = br.readLine();
			while (!line.equals("")){
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
	
	public static String addItem(String itemText) {
		BufferedWriter br;
		String menu = "Error";
		try {
			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/Marcin/kti/rak/resources/menu.txt", true),  "UTF8"));
			br.write(" \n" + itemText.substring(6));
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return menu;
	}
}
