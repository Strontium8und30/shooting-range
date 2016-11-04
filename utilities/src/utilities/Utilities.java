package utilities;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.*;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;

import utilities.log.*;
import utilities.resource.ResourceLoader;

public class Utilities {
	
	/** Logging */
	public static Log log = LogFactory.getLog(Utilities.class);
	
	/** Die bereits geladenen Icons */
	private static Map<String, Icon> loadedIcons = new HashMap<String, Icon>();
	
	/** Die bereits geladenen Fonts */
	private static Map<String, Font> loadedFonts = new HashMap<String, Font>();
	
	/** Die bereits geladenen Sounds */
	private static Map<String, Clip> loadedSounds = new HashMap<String, Clip>();
	
	
	public static String getSystemDate(String format) {
		return new SimpleDateFormat(format).format(System.currentTimeMillis());
	}
	
	public static Dimension getScreenCenter() {
		return new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
							 (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
	}
	
	public static boolean isNumber(String number) {
		try {
			Integer.parseInt(number);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static ImageIcon getIcon(String path) {
		ImageIcon icon = (ImageIcon)loadedIcons.get(path);
		if (icon == null) {
			log.warning("Icon musste nachgeladen werden: (" + path + ")");
			icon = new ImageIcon(ResourceLoader.getFile(path).toString());
			loadedIcons.put(path, icon);
		}
		return icon;
	}
	
	public static Font getFont(String path) {
		Font font = loadedFonts.get(path);
		if (font == null) {
			log.warning("Font musste nachgeladen werden: (" + path + ")");
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getFile(path));
			} catch (FontFormatException e) {
				log.error("FontFormat Fehler beim laden einer Schriftart", e);
			} catch (Exception e) {
				log.error("Datei Fehler beim laden einer Schriftart", e);
			}
			loadedFonts.put(path, font);
		}
		return font;
	}
	
	public static void playSound(String path) {
		streamSound(path);
	}
	
	private static void playClip(String path) {
		Clip sound = loadedSounds.get(path);
		if (sound == null) {
			try {
				log.warning("AudioClip musste nachgeladen werden: (" + path + ")");
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ResourceLoader.getFile(path));
				sound = AudioSystem.getClip();
				sound.open(audioInputStream);
				loadedSounds.put(path, sound);
			} catch (IOException e) {
				log.error("Audiofile wurde nicht gefunden: " + path);
			} catch (LineUnavailableException e) {
				log.error("Fehler beim laden eines Sounds", e);
			} catch (UnsupportedAudioFileException e) {
				log.error("Fehler beim laden eines Sounds", e);
			}
		}
//		sound.stop();
		sound.setFramePosition(0);
		sound.start();
	}
	
	private static void streamSound(final String strFilename) {
		if (strFilename == null) return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte[]	abData = new byte[128000];
				AudioInputStream audioInputStream = null;
				try	{
					audioInputStream = AudioSystem.getAudioInputStream(ResourceLoader.getFile(strFilename));
				} catch (Exception e) {
					log.error("Fehler beim laden des Sounds: " + strFilename, e);
				}
				audioInputStream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, audioInputStream);
				AudioFormat	audioFormat = audioInputStream.getFormat();

				SourceDataLine	line = null;
				try	{
					line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, audioFormat));
					line.open(audioFormat);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				line.start();
				
				int	nBytesRead = 0;				
				while (nBytesRead != -1)
				{
					try	{
						nBytesRead = audioInputStream.read(abData, 0, abData.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (nBytesRead >= 0) {
						line.write(abData, 0, nBytesRead);
					}
				}
				line.drain();
			    line.close();
			}
		}).start();
	}
}
