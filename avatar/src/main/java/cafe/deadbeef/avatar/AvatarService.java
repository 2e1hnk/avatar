package cafe.deadbeef.avatar;

import java.awt.image.BufferedImage;

public interface AvatarService {
	public BufferedImage generate(int style, String hash, int scale);
}
