package maptool.gui.mainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import maptool.Const;
import maptool.export.ExportDAO;
import maptool.gui.mainFrame.ObjectTree.ObjectTreeNode;
import maptool.gui.portal.CreatePortalView;
import maptool.gui.switchs.CreateLiftView;
import maptool.gui.switchs.CreateSwitchView;
import maptool.legacy.ImportDAO;
import maptool.object.ComplexContainerObject;
import maptool.object.ComplexGraphicalObject;
import maptool.util.ObjectChooserDiaglog;
import utilities.log.Log;
import utilities.log.LogFactory;
import utilities.mvc.Model;
import utilities.swing.FileFilter;
import utilities.swing.FilteredFileChooser;
import client.LocalClientModel;
import client.gui.GameFrame;
import framework.types.GraphicalObject;

public class MainMenu extends JMenuBar {
	
	/** Logging */
	public static Log log = LogFactory.getLog(MainMenu.class);
	
	Model model = null;
	
	public MainMenu(Model model) {
		this.model = model;
		
		//Datei*****************************************************************
		JMenu file = new JMenu("Datei");
		this.add(file);
				
		JMenuItem newMap = new JMenuItem("Neue Map");
		newMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((MainModel)getModel()).newMap();
			}				
		});
		file.add(newMap);
		
		JMenuItem loadMap = new JMenuItem("Map laden (.xml)");
		loadMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((MainModel)getModel()).loadMap();
			}				
		});
		file.add(loadMap);

		JMenuItem exportMap = new JMenuItem("Map exportieren (.xml)");
		exportMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new ExportDAO().exportToMap(getModel().getMap(), null);
				} catch (IOException ioe) {
					log.error("Fehler beim exportieren der Map", ioe);
				}
			}				
		});
		file.add(exportMap);
		
		JMenuItem exportMapGame = new JMenuItem("Map exportieren <Spiel> (.xml)");
		exportMapGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new ExportDAO().exportToMap(getModel().getMap(), null);
				} catch (IOException ioe) {
					log.error("Fehler beim exportieren der Map", ioe);
				}
			}				
		});
		file.add(exportMapGame);
		
		JMenuItem importContainer = new JMenuItem("Kontainer importieren (.xml)");
		importContainer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File importFile;
				ComplexContainerObject containerObject;
				try {
					FilteredFileChooser importFileChooser = new FilteredFileChooser(FilteredFileChooser.IMPORT);
					importFileChooser.setFileFilter(new FileFilter(".xml", "Kontainer (*.xml)"));
					importFileChooser.showOpenDialog(MainMenu.this);
					importFile = importFileChooser.getSelectedFile();
					containerObject = new ImportDAO().importToModel(getModel().getMap().getTextureHelper(), importFile);
				} catch (Exception ex) {
					log.error("Fehler beim importieren eines Kontainers", ex);
					return;
				}
				if (containerObject != null) {
					getModel().setModelObject(containerObject);
				}
			}				
		});
		file.add(importContainer);
		
		//Bearbeiten*****************************************************************
		JMenu bearbeiten = new JMenu("Bearbeiten");
		this.add(bearbeiten);
		
		JMenuItem gridAlignment = new JMenuItem("Am Raster ausrichten");
		gridAlignment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().getMap().autoGridAlignment();
				getModel().glRepaint();
			}				
		});
		bearbeiten.add(gridAlignment);

		
		JMenuItem similarTriangles = new JMenuItem("Ähnliche Flächen suchen");
		similarTriangles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<GraphicalObject> gos = getModel().getMap().getMainContainerObject().getAllSubGraphicalObjects();
				for (GraphicalObject graphicalObject : gos) {
					for (GraphicalObject refGraphicalObject : gos) {
						if (((ComplexGraphicalObject)graphicalObject).similar(refGraphicalObject) && !(graphicalObject == refGraphicalObject)) {
							((ComplexGraphicalObject)refGraphicalObject).setSelected(true);
						}
					}
				}
				getModel().glRepaint();
			}				
		});
		bearbeiten.add(similarTriangles);
		
		JMenuItem changeMarks = new JMenuItem("Markierung umkehren");
		changeMarks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<GraphicalObject> gos = getModel().getMap().getMainContainerObject().getAllSubGraphicalObjects();
				for (GraphicalObject graphicalObject : gos) {
					((ComplexGraphicalObject) graphicalObject).setSelected(!((ComplexGraphicalObject) graphicalObject).isSelected());
				}
				getModel().glRepaint();
			}				
		});
		bearbeiten.add(changeMarks);
		
		JMenuItem dropMarks = new JMenuItem("Markierung aufheben");
		dropMarks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<GraphicalObject> gos = getModel().getMap().getMainContainerObject().getAllSubGraphicalObjects();
				for (GraphicalObject graphicalObject : gos) {
					((ComplexGraphicalObject) graphicalObject).setSelected(false);
				}
				getModel().glRepaint();
			}				
		});
		bearbeiten.add(dropMarks);
		
		
		JMenuItem dropObject = new JMenuItem("Markierte Objekte löschen");
		dropObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<GraphicalObject> gos = getModel().getMap().getMainContainerObject().getAllSubGraphicalObjects();
				for (GraphicalObject graphicalObject : gos) {
					if(((ComplexGraphicalObject) graphicalObject).isSelected()) {
						graphicalObject.remove();
					}
				}
				getModel().notifyViews(MainModel.OBJECTTREE_RELOAD);
				getModel().glRepaint();
			}				
		});
		bearbeiten.add(dropObject);
		//Ansicht********************************************************************
		JMenu perspecvtive = new JMenu("Ansicht");
		ButtonGroup buttonGroupPerspective = new ButtonGroup();
		this.add(perspecvtive);

		JMenuItem textureFaceTrans = new JRadioButtonMenuItem("Flächen + Texturen + Transparent");
		textureFaceTrans.setSelected(((MainModel)model).getPerspective() == Const.TEXTURE_FACE_TRANS);
		textureFaceTrans.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().setPerspective(Const.TEXTURE_FACE_TRANS);
			}				
		});
		perspecvtive.add(textureFaceTrans);
		buttonGroupPerspective.add(textureFaceTrans);
		
		JMenuItem textureFace = new JRadioButtonMenuItem("Flächen + Texturen");
		textureFace.setSelected(((MainModel)model).getPerspective() == Const.TEXTURE_FACE);
		textureFace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().setPerspective(Const.TEXTURE_FACE);
			}				
		});
		perspecvtive.add(textureFace);
		buttonGroupPerspective.add(textureFace);
		
		JMenuItem colorFace = new JRadioButtonMenuItem("Flächen + Farben");
		colorFace.setSelected(((MainModel)model).getPerspective() == Const.COLOR_FACE);
		colorFace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().setPerspective(Const.COLOR_FACE);
			}				
		});
		perspecvtive.add(colorFace);
		buttonGroupPerspective.add(colorFace);
		
		JMenuItem gridFace = new JRadioButtonMenuItem("Gitternetz");
		gridFace.setSelected(((MainModel)model).getPerspective() == Const.GRID_FACE);
		gridFace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().setPerspective(Const.GRID_FACE);
			}				
		});
		perspecvtive.add(gridFace);
		buttonGroupPerspective.add(gridFace);
		
		perspecvtive.addSeparator();
		
		JCheckBoxMenuItem showInfoView = new JCheckBoxMenuItem("Info Anzeige");
		showInfoView.setSelected(true);
		showInfoView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().notifyViews(MainModel.INFOVIEW_SHOW, ((JCheckBoxMenuItem)e.getSource()).isSelected());
			}				
		});
		perspecvtive.add(showInfoView);
		

		//Map************************************************************************
		JMenu map = new JMenu("Map");
		this.add(map);
		
		JMenuItem exportAndTestMap = new JMenuItem("Exportieren & Testen");
		exportAndTestMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().exportMap(new File("tmpmap.xml"));
				GameFrame gameFrame = new GameFrame(new LocalClientModel(), new File("./maps/tmpmap.xml"));
				gameFrame.setVisible(true);
			}				
		});
		map.add(exportAndTestMap);
		
		JMenuItem testMap = new JMenuItem("Testen");
		testMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameFrame gameFrame = new GameFrame(new LocalClientModel(), new File("./maps/tmpmap.xml"));
				gameFrame.setVisible(true);
			}				
		});
		map.add(testMap);

		map.addSeparator();
		
		JMenuItem createPortal = new JMenuItem("Portal hizufügen");
		createPortal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CreatePortalView(getModel());
			}				
		});
		map.add(createPortal);
		
		JMenuItem createLift = new JMenuItem("Lift hizufügen");
		createLift.addActionListener(e -> new CreateLiftView(getModel()));
		map.add(createLift);
		
		//Models*********************************************************************
		JMenu models = new JMenu("Models");
		this.add(models);
		
		for (String name : getModel().modelObjects.keySet()) {
			JMenuItem modelObject = new JMenuItem(name);
			modelObject.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getModel().setModelObject(getModel().modelObjects.get(((JMenuItem)e.getSource()).getText()));
				}				
			});
			models.add(modelObject);
		}
		
		models.addSeparator();
		
		JMenuItem organizeFavorits = new JMenuItem("Favoriten bearbeiten");
		organizeFavorits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().modelObjects.clear();
				getModel().notifyViews(MainModel.MAINMENU_RELOAD);
			}
		});
		models.add(organizeFavorits);
		
		//Objekt*********************************************************************
		JMenu object = new JMenu("Objekt");
		this.add(object);
		
		JMenuItem createSwitch = new JMenuItem("Schalter hizufügen");
		createSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getModel().getSelectedObjects() != null && getModel().getSelectedObjects().size() == 1) {
					new CreateSwitchView(getModel(), (ComplexContainerObject)getModel().getSelectedObjects().get(0).getUserObject());
				} else {
					String infoMessage = "Es ist mehr als ein Objekt ausgewählt.\n" +
										 "Sie müssen ein Objekt auswählen unter das\n" +
										 "das neue Objekt gehängt werden soll.";
					
					ObjectChooserDiaglog<ObjectTreeNode> objektChooser = 
									new ObjectChooserDiaglog<ObjectTreeNode>(infoMessage);
					
					for (ObjectTreeNode node : getModel().getSelectedObjects()) {
						if (node.getUserObject() instanceof ComplexContainerObject) {
							objektChooser.addObject(node);
						}
					}
					
					ObjectTreeNode complexObject = objektChooser.getComplexObject();
					if(complexObject != null) {
						getModel().exportToModel((ComplexContainerObject)complexObject.getUserObject(), null);
					}
				}			
			}				
		});
		map.add(createSwitch);
		
		JMenuItem exportToModel = new JMenuItem("Export als Modell");
		exportToModel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getModel().getSelectedObjects() != null && getModel().getSelectedObjects().size() == 1 ) {
					getModel().exportToModel((ComplexContainerObject)getModel().getSelectedObjects().get(0).getUserObject(), null);
				} else {
					String infoMessage = "Es ist mehr als ein Objekt ausgewählt.\n" +
										 "Sie müssen ein Objekt auswählen unter das\n" +
										 "das neue Objekt gehängt werden soll.";
					
					ObjectChooserDiaglog<ObjectTreeNode> objektChooser = 
									new ObjectChooserDiaglog<ObjectTreeNode>(infoMessage);
					
					for (ObjectTreeNode node : getModel().getSelectedObjects()) {
						if (node.getUserObject() instanceof ComplexContainerObject) {
							objektChooser.addObject(node);
						}
					}
					
					ObjectTreeNode complexObject = objektChooser.getComplexObject();
					if(complexObject != null) {
						getModel().exportToModel((ComplexContainerObject)complexObject.getUserObject(), null);
					}
				}			
			}				
		});
		object.add(exportToModel);
	}
	
	public MainModel getModel() {
		return (MainModel)model;
	}
}
