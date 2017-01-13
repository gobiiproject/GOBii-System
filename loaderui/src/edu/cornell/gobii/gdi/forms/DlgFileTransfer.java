package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GlyphMetrics;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineStyleEvent;

public class DlgFileTransfer extends Dialog {

	private static Logger log = Logger.getLogger(DlgFileTransfer.class.getName());
	protected Object result;
	protected Shell shell;
	private String destination;
	private List<String> files = new ArrayList<String>();
	private String extention;
	private StyledText txtOutput;
	private Session session;
	private Channel channel;
	private boolean ptimestamp = true;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DlgFileTransfer(Shell parent, int style, String destination, List<String> files, String ext) {
		super(parent, style);
		setText("File Transfer");
		this.destination = destination;
		this.files = files;
		this.extention = ext;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				connect();
				
//				Thread t1 = new Thread(new Runnable() {
//
//					@Override
//					public void run() {
						process();
						disconnect();
//					}
//					
//				});
//				t1.start();
				
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(600, 600);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		txtOutput = new StyledText(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		txtOutput.addLineStyleListener(new LineStyleListener() {
			public void lineGetStyle(LineStyleEvent e) {
				//Set the line number
		        e.bulletIndex = txtOutput.getLineAtOffset(e.lineOffset);

		        //Set the style, 12 pixles wide for each digit
		        StyleRange style = new StyleRange();
		        style.metrics = new GlyphMetrics(0, 0, Integer.toString(txtOutput.getLineCount()+1).length()*12);

		        //Create and set the bullet
		        e.bullet = new Bullet(ST.BULLET_NUMBER,style);
			}
		});
		txtOutput.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtOutput.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
	}
	
	public void connect(){
		try {
			JSch jsch=new JSch();
			session = jsch.getSession(App.INSTANCE.getUser().getUserName(), App.INSTANCE.getServer(), 22);
			UserInfo ui=new UserInfoPrompter();
			session.setUserInfo(ui);
			
			session.connect();
		} catch (JSchException err) {
			Utils.log(shell, null, log, "Error opening session", err);
			shell.close();
		}
	}
	
	public void disconnect(){
		try{
			channel.disconnect();
			session.disconnect();
			shell.close();
		}catch (Exception err) {
			Utils.log(shell, null, log, "Error disconnecting session", err);
			shell.close();
		}
	}
	
	public void process(){
		try{
			for(String file : files){
				if(!file.endsWith(extention)) continue;
				try {
					processFile(destination, file);
				} catch (JSchException | IOException err) {
					Utils.log(shell, null, log, "Error tranfering file", err);
				}

			}
			print("done transfering files...", true);
		}catch (Exception err) {
			Utils.log(shell, null, log, "Error tranfering file", err);
			shell.close();
		}
	}
	
	private void processFile(String destination, String sourceFile) throws JSchException, IOException{
		// exec 'scp -t rfile' remotely
//		String command="scp " + (ptimestamp ? "-p" :"") +" -t "+destination;
		File sFile = new File(sourceFile);
		String dFilename = sFile.getName();
		dFilename = dFilename.replace(" ", "_");
		destination += (destination.endsWith("/") ? "" : "/") + dFilename;
		String command="scp " + "-p" +" -t "+destination;
		channel = session.openChannel("exec");
		((ChannelExec)channel).setCommand(command);
		print(command, true);

		// get I/O streams for remote scp
		OutputStream out=channel.getOutputStream();
		InputStream in=channel.getInputStream();

		channel.connect();

		if(checkAck(in)!=0){
			Utils.log(shell, null, log, "Error tranfering file", null);
			return;
		}

		File _lfile = new File(sourceFile);
		command = null;
		if(ptimestamp){
			command="T"+(_lfile.lastModified()/1000)+" 0";
			// The access time should be sent here,
			// but it is not accessible with JavaAPI ;-<
			command+=(" "+(_lfile.lastModified()/1000)+" 0\n"); 
			out.write(command.getBytes()); out.flush();
			if(checkAck(in)!=0){
				Utils.log(shell, null, log, "Error tranfering file", null);
				return;
			}
		}
		print(command, true);

		// send "C0644 filesize filename", where filename should not include '/'
		long filesize=_lfile.length();
		command="C0644 "+filesize+" ";
		if(sourceFile.lastIndexOf('/')>0){
			command+=sourceFile.substring(sourceFile.lastIndexOf('/')+1);
		}
		else{
			command+=sourceFile;
		}
		command+="\n";
		out.write(command.getBytes()); out.flush();
		if(checkAck(in)!=0){
			Utils.log(shell, null, log, "Error tranfering file", null);
			return;
		}
		print(command, true);

		// send a content of sourceFile
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(sourceFile);
			byte[] buf=new byte[1024];
			while(true){
				int len=fis.read(buf, 0, buf.length);
				if(len<=0) break;
				out.write(buf, 0, len); //out.flush();
			}
			fis.close();
			fis=null;
			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();
			if(checkAck(in)!=0){
				Utils.log(shell, null, log, "Error tranfering file", null);
				return;
			}
		}catch(Exception err){
			Utils.log(shell, null, log, "Error tranfering file", err);
		}finally{
			out.close();
		}
	}
	
	static int checkAck(InputStream in) throws IOException{
		int b=in.read();
		// b may be 0 for success,
		//          1 for error,
		//          2 for fatal error,
		//          -1
		if(b==0) return b;
		if(b==-1) return b;

		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do {
				c=in.read();
				sb.append((char)c);
			}
			while(c!='\n');
			if(b==1){ // error
				System.out.print(sb.toString());
			}
			if(b==2){ // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}
	
	private void print(String str, boolean addNewline){
		String text = txtOutput.getText();
		text += str + (addNewline ? "\n" : "");
		txtOutput.setText(text);
	}
}
