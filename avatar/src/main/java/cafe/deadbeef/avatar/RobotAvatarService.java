package cafe.deadbeef.avatar;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotAvatarService  {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired FileSystemStorageService fileSystemStorageService;
	@Autowired ColourService colourService;
	
	public final String[] styles = { "broken-arm", "broken" };

	public BufferedImage generate(int style, String hash, int scale) {
		
		
		
		BufferedImage robot = null;
		
		try {
			//Path robotPath = fileSystemStorageService.load("/src/main/resources/robot/broken.svg");
			File robotFile = new File(getClass().getClassLoader().getResource("robot/" + styles[style] + ".svg").getFile());
			robot = rasterize(robotFile, hash);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return robot;
	}
	
	public BufferedImage rasterize(File svgFile, String hash) throws IOException {

	    final BufferedImage[] imagePointer = new BufferedImage[1];
	    
	    int[] colours = colourService.getColours(hash);

	    // Rendering hints can't be set programatically, so
	    // we override defaults with a temporary stylesheet.
	    // These defaults emphasize quality and precision, and
	    // are more similar to the defaults of other SVG viewers.
	    // SVG documents can still override these defaults.
	    String css = "svg {" +
	            "shape-rendering: geometricPrecision;" +
	            "text-rendering:  geometricPrecision;" +
	            "color-rendering: optimizeQuality;" +
	            "image-rendering: optimizeQuality;" +
	            "}" +
	            ".background { fill: #" + Integer.toHexString(colours[colourService.BACKGROUND]).substring(2) + " }" +
	            ".skin-alt { fill: #" + Integer.toHexString(colours[colourService.SKIN_SHADOW]).substring(2) + " }" +
	            ".eye { fill: #" + Integer.toHexString(colours[colourService.EYE]).substring(2) + " }" +
	            ".motion { fill: #C1C4D7 }" +
	            ".clothing { fill: #" + Integer.toHexString(colours[colourService.CLOTHING]).substring(2) + " }" +
	            ".metal { fill: #ABAFC0 }" +
	            ".colour1 { fill: #" + Integer.toHexString(colours[colourService.CLOTHING_FEATURE]).substring(2) + " }" +
	            ".colour2 { fill: #46B978 }" +
	            ".colour3 { fill: #EC5C5B }";
	    logger.debug(css);
	    File cssFile = File.createTempFile("batik-default-override-"+hash, ".css");
	    FileUtils.writeStringToFile(cssFile, css);

	    TranscodingHints transcoderHints = new TranscodingHints();
	    transcoderHints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.FALSE);
	    transcoderHints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION,
	            SVGDOMImplementation.getDOMImplementation());
	    transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI,
	            SVGConstants.SVG_NAMESPACE_URI);
	    transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
	    transcoderHints.put(ImageTranscoder.KEY_USER_STYLESHEET_URI, cssFile.toURI().toString());

	    try {

	        TranscoderInput input = new TranscoderInput(new FileInputStream(svgFile));

	        ImageTranscoder t = new ImageTranscoder() {

	            @Override
	            public BufferedImage createImage(int w, int h) {
	                return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	            }

	            @Override
	            public void writeImage(BufferedImage image, TranscoderOutput out)
	                    throws TranscoderException {
	                imagePointer[0] = image;
	            }
	        };
	        t.setTranscodingHints(transcoderHints);
	        t.transcode(input, null);
	    }
	    catch (TranscoderException ex) {
	        // Requires Java 6
	        ex.printStackTrace();
	        throw new IOException("Couldn't convert " + svgFile);
	    }
	    finally {
	        cssFile.delete();
	    }

	    return imagePointer[0];
	}
	
}
