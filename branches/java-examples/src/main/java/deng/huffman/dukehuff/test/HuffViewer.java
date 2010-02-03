package deng.huffman.dukehuff.test;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class HuffViewer extends JFrame
{
    protected JList      myOutput;
    protected IHuffModel  myModel;
    protected String     myTitle;
    protected JTextField myMessage;
    protected File myFile;
    private boolean myForce;
    
    protected static JFileChooser ourChooser = new JFileChooser(".");
    
    
    public HuffViewer(String title)
    {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = (JPanel) getContentPane();
		panel.setLayout(new BorderLayout());
		setTitle(title);
		myTitle = title;
        myForce = false;
		
		panel.add(makeOutput(), BorderLayout.CENTER);
		panel.add(makeMessage(), BorderLayout.SOUTH);
		makeMenus();
		
		pack();
		setSize(400,400);
		setVisible(true);
    }

    public void setModel(IHuffModel model){
	    myModel = model;
        myModel.setViewer(this);
    }

    protected JPanel makeMessage(){
    	JPanel p = new JPanel(new BorderLayout());
    	myMessage = new JTextField(30);
    	p.setBorder(BorderFactory.createTitledBorder("message"));
    	p.add(myMessage, BorderLayout.CENTER);
    	return p;
    }
    
    protected JPanel makeOutput()
    {
		JPanel p = new JPanel(new BorderLayout());
		myOutput = new JList();
		myOutput.setVisibleRowCount(10);
		p.setBorder(
		    BorderFactory.createTitledBorder("output"));
		p.add(new JScrollPane(myOutput), BorderLayout.CENTER);
		return p;
	
    }

    protected void doRead(){
        
        int retval = ourChooser.showOpenDialog(null);
        if (retval != JFileChooser.APPROVE_OPTION){
            return;
        }   
        showMessage("reading/initializing");
        myFile = ourChooser.getSelectedFile();
        final ProgressMonitorInputStream pmis = 
            getMonitorableStream(myFile, "counting/reading bits ...");
        final ProgressMonitor progress = pmis.getProgressMonitor();
        Thread fileReaderThread = new Thread(){
            public void run(){        
                myModel.initialize(pmis);
                if (progress.isCanceled()) {
                    HuffViewer.this.showError("reading cancelled");
                }                 
            }
        };
        fileReaderThread.start();

    }
    protected JMenu makeOptionsMenu(){
        JMenu menu = new JMenu("Options");

        menu.add(new AbstractAction("Charcounts"){
            public void actionPerformed(ActionEvent ev){
                myModel.showCounts();
            }
        });
        
        menu.add(new AbstractAction("Charcodings"){
            public void actionPerformed(ActionEvent ev){
                myModel.showCodings();
            }
        });
        
        JCheckBoxMenuItem force = new JCheckBoxMenuItem(
                new AbstractAction("Force Compression"){
                    public void actionPerformed(ActionEvent ev){
                        myForce = !myForce;
                    }
                });
        menu.add(force);
        return menu;
        
    }
    protected JMenu makeFileMenu(){
		JMenu fileMenu = new JMenu("File");
		
			fileMenu.add(new AbstractAction("Open/count"){
				public void actionPerformed(ActionEvent ev){
				   doRead();    
				}
			    });
			
			fileMenu.add(new AbstractAction("Compress"){
			    public void actionPerformed(ActionEvent ev){
			        doSave();
			    }
			});
			
            fileMenu.add(new AbstractAction("Uncompress"){
                public void actionPerformed(ActionEvent ev){
                    doDecode();
                }
            });
            
			fileMenu.add(new AbstractAction("Quit"){
				public void actionPerformed(ActionEvent ev){
				    System.exit(0);
				}
			    });
		return fileMenu;
    }
    protected void makeMenus()
    {
		JMenuBar bar = new JMenuBar();
		bar.add(makeFileMenu());
        bar.add(makeOptionsMenu());
		setJMenuBar(bar);
    }

    private void doDecode(){
        File file = null;
        showMessage("uncompressing");
        try {
            int retval = ourChooser.showOpenDialog(null);
            if (retval != JFileChooser.APPROVE_OPTION){
                return;
            }       
            file = ourChooser.getSelectedFile();
            String name = file.getName();
            String newName = JOptionPane.showInputDialog(this,"Name of uncompressed file",
                                                         name+".unhf");
            if (newName == null){
                return;
            }
            String path = file.getCanonicalPath();
  
            int pos = path.lastIndexOf(name);
            newName = path.substring(0,pos) + newName;
            final File newFile = new File(newName);
            
            final ProgressMonitorInputStream stream = 
                    getMonitorableStream(file,"uncompressing bits...");
            final ProgressMonitor progress = stream.getProgressMonitor();
            final OutputStream out = new FileOutputStream(newFile);
            Thread fileReaderThread = new Thread(){
                public void run(){   
                    myModel.uncompress(stream,out);
                    if (progress.isCanceled()) {
                        HuffViewer.this.showError("reading cancelled");
                    }                                    
                }
            };
            fileReaderThread.start();
        } catch (FileNotFoundException e) {
            showError("could not open "+file.getName());
            e.printStackTrace();
        }
        catch (IOException e) {
            showError("IOException, uncompression halted from viewer");
            e.printStackTrace();
        }
    }
    private void doSave(){
        showMessage("compressing");
        String name = myFile.getName();
        String newName = JOptionPane.showInputDialog(this,"Name of compressed file",
                                                     name+".hf");
		if (newName == null){
            return;
        }
        String path=null;
        try {
            path = myFile.getCanonicalPath();
        } catch (IOException e) {
            showError("trouble with file canonicalizing");
            return;
        }
        int pos = path.lastIndexOf(name);
        newName = path.substring(0,pos) + newName;
        final File file = new File(newName);
        
        final ProgressMonitorInputStream pmis = 
            getMonitorableStream(myFile, "compressing bits ...");
        final ProgressMonitor progress = pmis.getProgressMonitor();
        Thread fileWriterThread = new Thread(){
            public void run(){        
                myModel.write(pmis,file,myForce);
                if (progress.isCanceled()) {
                    HuffViewer.this.showError("compression cancelled");
                    cleanUp(file);
                }                 
            }
        };
        fileWriterThread.start();
		 
    }
   
    private void cleanUp(File f){
        if (!f.delete()){
            showError("trouble deleting "+f.getName());
        }
        else {
            // do something here?
        }
    }
    
    private ProgressMonitorInputStream getMonitorableStream(File file, String message){
        try {
            InputStream stream = new FileInputStream(file);
            final ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(
                    this,
                    message,
                    stream);

            ProgressMonitor progress = pmis.getProgressMonitor();
            progress.setMillisToDecideToPopup(1);
            progress.setMillisToPopup(1);
            
            return pmis;
        }  catch (FileNotFoundException e) {
            showError("could not open "+file.getName());
            e.printStackTrace();
            return null;
        }
    }
    
    public void update(Collection elements){
        showMessage("");
		Object[] array = elements.toArray();
		myOutput.setListData(array);
    }
    

	public void showMessage(String s) {
		myMessage.setText(s);
	}
	
	public void showError(String s){
	    JOptionPane.showMessageDialog(this,s,"Huff info",
	                                  JOptionPane.INFORMATION_MESSAGE);
	}
    
}
