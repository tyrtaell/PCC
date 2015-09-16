package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import model.FileDataManager;
import model.PCACalc;
import model.Variable;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ApplicationMenu extends JMenuBar{
	final static Logger logger = Logger.getLogger(ApplicationMenu.class);
	public ApplicationMenu(final JTabbedPane jtpPane){
		super();
		File workingDirectory = new File(System.getProperty("user.dir"));
		
		final JFileChooser jfcFile = new JFileChooser();
		jfcFile.setCurrentDirectory(workingDirectory);
		jfcFile.setMultiSelectionEnabled(true);
		JMenu menu1 = new JMenu("Plik");
		JMenu menu2 = new JMenu("About");
		add(menu1);
		add(menu2);		
		JMenuItem jmiItem1 = new JMenuItem("Za³aduj pliki");		
		jmiItem1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = jfcFile.showOpenDialog(ApplicationMenu.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File[] file = jfcFile.getSelectedFiles();		            
		            if(file.length<3)
		            {
		            	logger.error("User specified less than three files");
		            	JOptionPane.showMessageDialog(null,  "Nale¿y wskazaæ conajmniej trzy pliki","Alert",JOptionPane.ERROR_MESSAGE);
		            	return;
		            }
		            
		            // £adowanie plików
		            FileDataManager.clear();
		            for(int i = 0 ; i < file.length; i++){
		            	FileDataManager fdm = new FileDataManager(file[i].getName());
		            	try (BufferedReader br = new BufferedReader(new FileReader(file[i]))) {
			                String line = br.readLine();
			                if(line.startsWith("TITLE")){
			                	logger.debug("User selected Full info file");
			                	while(line != null && !line.startsWith("XYDATA"))
			                		line = br.readLine();              	
			                	line = br.readLine();
			                	try{
				                	while (line != null && !line.equals("")) {			                		
				                		String[] values = line.split("\t");
				                		Character c = new Character(',');
				                		Variable v = new Variable(values[0],values[1],c);
				                		fdm.addVariable(v);
				                		line = br.readLine();
				                	}
			                	}catch(Exception e){
			                		logger.fatal("Line reading error: "+ line,e);
			                	}			                	
			                }
			                else{
			                	logger.debug("User selected only data file");
			                	try{
				                	while (line != null && !line.equals("")) {			                		
				                		String[] values = line.split("\t");
				                		Character c = new Character(',');
				                		Variable v = new Variable(values[0],values[1],c);
				                		fdm.addVariable(v);
				                		line = br.readLine();
				                	}
			                	}catch(Exception e){
			                		logger.fatal("Line reading error: "+ line,e);
			                	}
			                }
			            } catch (FileNotFoundException e) {
							logger.error("File was moved or deleted", e);
							
						} catch (IOException e) {
							logger.error("IO exception", e);
						}
		            	FileDataManager.addFileDataManager(fdm);
		            }
		            //sprawdzanie wartoœci
		            try{
		            	FileDataManager.checkValues();
		            }catch (Exception e){
		            	logger.error("Data comparison exception ",e);
		            	JOptionPane.showMessageDialog(null,  "Wskazane pliki maj¹ ró¿ne spektrum lub ró¿ne odstêpy czasowe","Alert",JOptionPane.ERROR_MESSAGE);
		            }
		            jtpPane.removeAll();
		            
		            PCACalc pca = new PCACalc(FileDataManager.getData(), FileDataManager.przeliczSrednie());
		            
		            jtpPane.addTab("Data diagrams", null, new DataChartPanel());
		            jtpPane.addTab("PCA",null,new PCAChartPanel(pca.getCovMatrix()));
		            
		            
		        } 
			}
		});
		menu1.add(jmiItem1);
	}
	public ApplicationMenu() {
		// TODO Auto-generated constructor stub
	}

}
