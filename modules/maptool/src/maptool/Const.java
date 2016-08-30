package maptool;

public class Const {

	/** Das Hauptprojekt verzeichniss */
	public final static String MAIN_DIR = "../../";
	/** Rastergenaugikeit */
	public static float GRID_PRECISION = 0.5f;
	
	/** Winkel(Grad) * DEGREE_TO_RADIAN = Winkel(Bogenmaﬂ) */
	public final static float DEGREE_TO_RADIAN = (float)(Math.PI/180);
	/** Farbe des Markierten objectes */
	public final static float[] COLOR_SELECTED = {0.5f, 0.5f, 1.0f};
	/** Farbe des Markierten zu bewegenden objectes */
	public final static float[] COLOR_SELECTED_MOVE = {1.5f, 0.5f, 0.5f};
	/** Farbe der Texture */
	public final static float[] COLOR_TEXTURED_VIEW = {1.0f, 1.0f, 1.0f};
	/** Farbe einer nicht Textuierten fl‰che */
	public final static float[] COLOR_NOT_TEXTURED_VIEW = {0.0f, 1.0f, 0.0f};
	/** Farbe des orientierungs Rasters */
	public final static float[] COLOR_GRID = {0.0f, 0.3f, 0.0f};
	/** Farbe des orientierungs Rasters an der Null linie */
	public final static float[] COLOR_NULL_GRID = {1.0f, 0.0f, 0.0f};
	
	/** Ansicht mit transparenten Texuren */ 
	public final static int TEXTURE_FACE_TRANS = 0;
	/** Ansicht mit Texuren */ 
	public final static int TEXTURE_FACE = 1;
	/** Ansicht mit Farben */
	public final static int COLOR_FACE = 2;	
	/** Ansicht mit Gitternetz */
	public final static int GRID_FACE = 3;
}
