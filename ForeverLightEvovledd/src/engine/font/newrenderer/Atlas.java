package engine.font.newrenderer;

import java.util.ArrayList;
import java.util.Arrays;

import engine.utils.FileUtils;

public class Atlas {

	private ArrayList<Glyph> glyphs = new ArrayList<>();

	public static ArrayList<Atlas> Atlases = new ArrayList<>();

	public final String FontName;

	public class Glyph {
		public int id;
		public int graphicx;
		public int graphicy;
		public int width;
		public int height;
		public int xoffset;
		public int yoffset;
		public int xadvance;
	}

	public static Atlas getAtlas(String FontName) throws Exception {
		for (Atlas a : Atlases) {
			if (a.FontName == FontName) {
				return a;
			}
		}
		throw new Exception("Atlas " + FontName + " is not initiated, please make sure the spelling is correct");
	}

	public static void createAtlas(String FontName) {
		new Atlas(FontName);
	}

	private Atlas(String FontName) {
		this.FontName = FontName;
		loadAtlasFile();
		Atlases.add(this);
	}

	public Glyph getGlyph(int id) {
		for (Glyph glyph : glyphs) {
			if (glyph.id == id) {
				return glyph;
			}
		}
		return getGlyph(0);
	}

	public Glyph getGlyph(char c) {
		for (Glyph glyph : glyphs) {
			if (glyph.id == c) {
				return glyph;
			}
		}
		return getGlyph(0);
	}

	private void loadAtlasFile() {
		System.out.println("loading font with fontname: " + FontName);
		ArrayList<String> results = FileUtils.loadAsStringArray("res/" + FontName + ".fnt");

		for (int i = 0; i < results.size(); i++) {
			// char id=0 x=0 y=0 width=73 height=84 xoffset=8 yoffset=4 xadvance=93 page=0
			// chnl=0
			ArrayList<String> splittedString = new ArrayList<String>(Arrays.asList(results.get(i).split(" ")));
			// clean up all the empty ""
			splittedString.removeAll(Arrays.asList("", null));
			// check if the line start with string "char", if yes, it is a atlas
			if (!splittedString.get(0).contentEquals("char")) {
				continue;
			}
			Glyph a = new Glyph();
			a.id = Integer.parseInt(splittedString.get(1).split("=")[1]);
			a.graphicx = Integer.parseInt(splittedString.get(2).split("=")[1]);
			a.graphicy = Integer.parseInt(splittedString.get(3).split("=")[1]);
			a.width = Integer.parseInt(splittedString.get(4).split("=")[1]);
			a.height = Integer.parseInt(splittedString.get(5).split("=")[1]);
			a.xoffset = Integer.parseInt(splittedString.get(6).split("=")[1]);
			a.yoffset = Integer.parseInt(splittedString.get(7).split("=")[1]);
			a.xadvance = Integer.parseInt(splittedString.get(8).split("=")[1]);
			glyphs.add(a);
		}
	}
}