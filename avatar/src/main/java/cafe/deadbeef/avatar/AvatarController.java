package cafe.deadbeef.avatar;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "")
public class AvatarController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired EightBitAvatarService avatarService;
	@Autowired RobotAvatarService robotAvatarService;

	@RequestMapping(value = "/face/{username}", method = RequestMethod.GET, produces = "image/png")
	public @ResponseBody byte[] buildFace(@PathVariable String username, @RequestParam(defaultValue = "5") String size, @RequestParam(required = false) String style) throws IOException, NoSuchAlgorithmException {
		
		String hash = avatarService.md5(username.trim().toLowerCase());
		
		int scale = Integer.valueOf(size);
		
		int faceStyle = Integer.valueOf(hash.substring(31,32), 16)%avatarService.faces.length;
		
		if ( style != null ) {
			faceStyle = Integer.parseInt(style);
		}
		
		logger.info("Image request: " + hash);
		
        BufferedImage img = avatarService.generate(faceStyle, hash, scale);
        //savePNG( img, "C:/Java_Dev/" + hash + ".bmp" );
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bao);
        return bao.toByteArray();
	}

	@RequestMapping(value = "/robot/{username}", method = RequestMethod.GET, produces = "image/png")
	public @ResponseBody byte[] buildRobot(@PathVariable String username, @RequestParam(defaultValue = "5") String size, @RequestParam(required = false) String style) throws IOException, NoSuchAlgorithmException {
		
		String hash = avatarService.md5(username.trim().toLowerCase());
		
		int faceStyle = Integer.valueOf(hash.substring(31,32), 16)%robotAvatarService.styles.length;
		
		if ( style != null ) {
			faceStyle = Integer.parseInt(style);
		}
		
		int scale = Integer.valueOf(size);
		
		logger.info("Image request: " + hash);
		
        BufferedImage img = robotAvatarService.generate(faceStyle, hash, scale);
        //savePNG( img, "C:/Java_Dev/" + hash + ".bmp" );
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bao);
        return bao.toByteArray();
	}

}
