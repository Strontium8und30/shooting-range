package client.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.miginfocom.swing.*;

public class TransFrame extends JComponent {
	
    private Image background;

	public TransFrame(JDialog frame) {
	    updateBackground( );
	    setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
	    add(new TransPanel(), "grow");
	    frame.setContentPane(this);
	    frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				TransFrame.this.repaint();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				TransFrame.this.repaint();
			}	    	
	    });
	}

	public void updateBackground( ) {
	    try {
	        Robot rbt = new Robot( );
	        Toolkit tk = Toolkit.getDefaultToolkit( );
	        Dimension dim = tk.getScreenSize( );
	        background = rbt.createScreenCapture(
	        new Rectangle(0,0,(int)dim.getWidth( ),
	                          (int)dim.getHeight( )));
	    } catch (Exception ex) {
	        ex.printStackTrace( );
	    }
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    Point pos = this.getLocationOnScreen( );
	    Point offset = new Point(-pos.x,-pos.y);
	    g.drawImage(background,offset.x,offset.y,this);
	}
}
