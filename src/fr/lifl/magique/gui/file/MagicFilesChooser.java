
package fr.lifl.magique.gui.file;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.File;



/**
 
 * A class that permits to choose the magic files of a directory
 *
 * @version 1.0 04/05/99
 * @author Nadir Doghmane
 * @author Fabien Niquet
 */

public class MagicFilesChooser extends JFileChooser {

  /**
   * Constructs a new MagicFilesChooser with the specified mode
   *
   * @param mode the mode of this chooser 0 to open and 1 to save
   */
	public MagicFilesChooser(int mode,String path) {
		super();
		FilesFilter filter = new FilesFilter(new String("magic"), "Configuration Files");
		addChoosableFileFilter(filter);
		setFileFilter(filter);
 
	  FilesView fileView = new FilesView();

	  ImageIcon magicIcon = new ImageIcon(this.getClass().getResource("magicIcon.gif"));
			fileView.putIcon("magic", magicIcon);
    	setFileView(fileView);
    

   	if (new File(path).exists())
   		setCurrentDirectory(new File(path));    
   	else
   		setCurrentDirectory(new File("."));
    setFileSelectionMode(JFileChooser.FILES_ONLY);
		setMultiSelectionEnabled(false);
		
		if (mode == 0)
			showOpenDialog(null);
		else if (mode == 1) {
			setDialogTitle("Save As");
			showSaveDialog(null);
		}
	}
	
}
