package cafe.deadbeef.avatar;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ColourService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final int BACKGROUND = 0;
	public static final int SKIN = 1;
	public static final int EYE = 2;
	public static final int HAIR = 3;
	public static final int CLOTHING = 4;
	public static final int CLOTHING_FEATURE = 5;
	public static final int EYE_ALT = 6;	// Typically white
	public static final int SKIN_ALT = 7;	// Typically black
	public static final int SKIN_SHADOW = 8;	// Typically skin but a bit darker
	
	public int[] getColours(String hash) {
		// hash is a 32-character MD5 hash of the standardised user identifier
		// where:
		// Chars Use
		// 1-6 background colour (1-2 for red, 3-4 for green, 5-6 for blue)
		// 7-12 skin colour
		// 13-18 eye colour
		// 19-24 hair colour
		// 25-27 clothing colour (1 for red, 2 for green, 3 for blue)
		// 28-30 clothing feature colour
		
		int[] colours = new int[9];
		colours[BACKGROUND] = new Color(
				Integer.valueOf(hash.substring(0,2), 16),
				Integer.valueOf(hash.substring(3,5), 16),
				Integer.valueOf(hash.substring(5,7), 16)).getRGB();
		
		colours[SKIN] = new Color(
				Integer.valueOf(hash.substring(7,9), 16),
				Integer.valueOf(hash.substring(9,11), 16),
				Integer.valueOf(hash.substring(11,13), 16)).getRGB();
		
		colours[SKIN_ALT] = new Color(0,0,0).getRGB();
		
		colours[SKIN_SHADOW] = new Color(
				(int) (Integer.valueOf(hash.substring(7,9), 16) * 0.75),
				(int) (Integer.valueOf(hash.substring(9,11), 16) * 0.75),
				(int) (Integer.valueOf(hash.substring(11,13), 16) * 0.75)).getRGB();
		
		colours[EYE] = new Color(
				Integer.valueOf(hash.substring(13,15), 16),
				Integer.valueOf(hash.substring(15,17), 16),
				Integer.valueOf(hash.substring(17,19), 16)).getRGB();
		
		colours[HAIR] = new Color(
				Integer.valueOf(hash.substring(19,21), 16),
				Integer.valueOf(hash.substring(21,23), 16),
				Integer.valueOf(hash.substring(23,25), 16)).getRGB();
		
		colours[CLOTHING] = new Color(
				Integer.valueOf(hash.substring(25,27), 16),
				Integer.valueOf(hash.substring(27,29), 16),
				Integer.valueOf(hash.substring(29,31), 16)).getRGB();
		
		colours[CLOTHING_FEATURE] = new Color(
				Integer.valueOf(hash.substring(28,29), 16),
				Integer.valueOf(hash.substring(29,30), 16),
				Integer.valueOf(hash.substring(30,31), 16)).getRGB();
		
		colours[EYE_ALT] = new Color(
				Integer.valueOf(hash.substring(13,15), 16),
				Integer.valueOf(hash.substring(15,17), 16),
				Integer.valueOf(hash.substring(17,19), 16)).getRGB();
		
		return colours;
	}

	public int[] getRealisticColours(String hash) {
		// hash is a 32-character MD5 hash of the standardised user identifier
		// where:
		// Chars Use
		// 1-6 background colour (1-2 for red, 3-4 for green, 5-6 for blue)
		// 7-12 skin colour
		// 13-18 eye colour
		// 19-24 hair colour
		// 25-27 clothing colour (1 for red, 2 for green, 3 for blue)
		// 28-30 clothing feature colour
		
		int[] colours = new int[9];
		colours[BACKGROUND] = new Color(0,0,0,128).getRGB();
		
		Color[] skinTones = {
				new Color(240,240,216),
				new Color(240,216,192),
				new Color(216,168,144),
				new Color(168,120,72),
				new Color(120,72,48),
				new Color(45,34,30),
				new Color(60,46,40),
				new Color(75,57,50),
				new Color(90,69,60),
				new Color(105,80,70),
				new Color(120,92,80),
				new Color(135,103,90),
				new Color(150,114,100),
				new Color(165,126,110),
				new Color(180,138,120),
				new Color(195,149,130),
				new Color(210,161,140),
				new Color(225,172,150),
				new Color(240,184,160),
				new Color(255,195,170),
				new Color(255,206,180)
		};
		
		Color[] eyeColours = {
				new Color(99,78,52),
				new Color(46,83,111),
				new Color(61,103,29),
				new Color(28,120,71),
				new Color(73,118,101)
		};
		
		Color[] hairColours = {
				new Color(9, 8, 6),
				new Color(44,34,43),
				new Color(59,48,36),
				new Color(78,87,63),
				new Color(80,69,69),
				new Color(106,78,66),
				new Color(95,72,56),
				new Color(167,133,106),
				new Color(184,151,120),
				new Color(220,208,186),
				new Color(222,188,153),
				new Color(151,121,97),
				new Color(230,206,188),
				new Color(229,200,168),
				new Color(165,137,70),
				new Color(145,85,61),
				new Color(83,61,53),
				new Color(113,99,90),
				new Color(183,166,158),
				new Color(214,196,194),
				new Color(255,24,225),
				new Color(202,191,177),
				new Color(141,74,67),
				new Color(181,82,57)
		};
		
		Color[] clothingColours = {
				new Color(255,0,0),
				new Color(0,255,0),
				new Color(0,0,255),
				new Color(255,255,0),
				new Color(0,255,255),
				new Color(255,0,255)
		};
		
		colours[SKIN] = skinTones[Integer.valueOf(hash.substring(0,2), 16)%skinTones.length].getRGB();
		
		colours[SKIN_ALT] = new Color(0,0,0).getRGB();
		
		colours[SKIN_SHADOW] = skinTones[(int) ((Integer.valueOf(hash.substring(0,2), 16)%skinTones.length) * 0.75)].getRGB();
		
		colours[EYE] = eyeColours[Integer.valueOf(hash.substring(2,4), 16)%eyeColours.length].getRGB();

		colours[EYE_ALT] = new Color(255,255,255).getRGB();
		
		colours[HAIR] = hairColours[Integer.valueOf(hash.substring(4,6), 16)%hairColours.length].getRGB();
		
		colours[CLOTHING] = clothingColours[Integer.valueOf(hash.substring(6,8), 16)%clothingColours.length].getRGB();
		
		colours[CLOTHING_FEATURE] = clothingColours[Integer.valueOf(hash.substring(8,10), 16)%clothingColours.length].getRGB();
		
		return colours;
	}

}
