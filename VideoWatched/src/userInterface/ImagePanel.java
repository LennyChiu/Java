package userInterface;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ImagePanel extends JPanel{
	
	private JScrollPane imageScrollPane = null;
	
	public ImagePanel(File pFile){
		this.setBackground(Color.BLACK);
		try {
			BufferedImage myPicture = ImageIO.read(pFile);
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			picLabel.setSize(ImagePanel.WIDTH, this.HEIGHT);
			add(picLabel);
			imageScrollPane = new JScrollPane(picLabel);
			imageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			imageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			add(imageScrollPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}
}
