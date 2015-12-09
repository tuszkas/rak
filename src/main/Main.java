//Main.java
package main;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.AbstractAction;

public class Main {
	private ConfigurationLoader sample;

	public static void main(String[] args) throws Exception {
		new Main();
	}

	public Main() throws IOException {
		GUI gui = new GUI();
		gui.addButton(new AbstractAction("Start") {
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		sample = new ConfigurationLoader(gui);
		gui.addButton(new AbstractAction("Stop") {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				terminate();
			}
		});
		gui.showCentered();
	}

	public void start() {
		try {
			sample.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			sample.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Terminates the application.
	 */
	public void terminate() {
		System.exit(0);
	}
}