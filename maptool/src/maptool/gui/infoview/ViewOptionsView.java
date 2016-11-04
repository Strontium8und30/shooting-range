package maptool.gui.infoview;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import maptool.gui.mainFrame.*;
import net.miginfocom.swing.*;
import utilities.mvc.*;
import framework.types.*;

public class ViewOptionsView extends DefaultView {

	/** Die Rastergenauigkeit */
	private Vector3D gridPrecision;
	
	/** Das Hauptpanel der ViewOptionsView */
	private JPanel mainPanel = null;
	
	/** Schieber um die Raster genauigkeit einzustellen */
	private JScrollBar gridScrollBarX;
	private JScrollBar gridScrollBarY;
	private JScrollBar gridScrollBarZ;
	
	/** Anzeige der aktuellen Rastergenauigkeit */
	private JLabel gridPrecisionLableX;
	private JLabel gridPrecisionLableY;
	private JLabel gridPrecisionLableZ;
	
	public ViewOptionsView(Model model) {
		super(model);
		gridPrecision = ((MainModel)getModel()).getGridPrecision();
		
		initScrollBars();
		
		gridPrecisionLableX = new JLabel(new Float(gridPrecision.getX()).toString());
		gridPrecisionLableY = new JLabel(new Float(gridPrecision.getY()).toString());
		gridPrecisionLableZ = new JLabel(new Float(gridPrecision.getZ()).toString());
		
		mainPanel = new JPanel(new MigLayout("","[align center, 30][align center, 30][align center, 30]","[][grow, fill][]"));
		mainPanel.add(new JLabel("X:"));
		mainPanel.add(new JLabel("Y:"));
		mainPanel.add(new JLabel("Z:"), "wrap");
		mainPanel.add(gridScrollBarX, "growy");
		mainPanel.add(gridScrollBarY, "growy");
		mainPanel.add(gridScrollBarZ, "growy, wrap");
		mainPanel.add(gridPrecisionLableX);
		mainPanel.add(gridPrecisionLableY);
		mainPanel.add(gridPrecisionLableZ);
	}
	
	private void initScrollBars() {
		gridScrollBarX = new JScrollBar(JScrollBar.VERTICAL);
		gridScrollBarX.setMaximum(210);
		gridScrollBarX.setMinimum(1);
		gridScrollBarX.setValue((int)(gridPrecision.getX() * 100));
		gridScrollBarX.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				gridPrecision.setX((float)gridScrollBarX.getValue() / 100);
				gridPrecisionLableX.setText(new Float(gridPrecision.getX()).toString());
				gridPrecisionLableX.validate();
				((MainModel)getModel()).glRepaint();
			}
		});
		
		gridScrollBarY = new JScrollBar(JScrollBar.VERTICAL);
		gridScrollBarY.setMaximum(210);
		gridScrollBarY.setMinimum(1);
		gridScrollBarY.setValue((int)(gridPrecision.getY() * 100));
		gridScrollBarY.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				gridPrecision.setY((float)gridScrollBarY.getValue() / 100);
				gridPrecisionLableY.setText(new Float(gridPrecision.getY()).toString());
				gridPrecisionLableY.validate();
				((MainModel)getModel()).glRepaint();
			}
		});
		
		gridScrollBarZ = new JScrollBar(JScrollBar.VERTICAL);
		gridScrollBarZ.setMaximum(210);
		gridScrollBarZ.setMinimum(1);
		gridScrollBarZ.setValue((int)(gridPrecision.getZ() * 100));
		gridScrollBarZ.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				gridPrecision.setZ((float)gridScrollBarZ.getValue() / 100);
				gridPrecisionLableZ.setText(new Float(gridPrecision.getZ()).toString());
				gridPrecisionLableZ.validate();
				((MainModel)getModel()).glRepaint();
			}
		});
	}

	@Override
	public Component getComponent() {
		return mainPanel;
	}
}
