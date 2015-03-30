package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class ChatView extends JFrame
{
	JPanel pan;
	JTextPane chat;
	JEditorPane message;
	Box box;

	multicast.Server server = null;
	
	SimpleAttributeSet keyWord;
	
	public ChatView(String ip, int port) throws IOException, InterruptedException
	{
	    this.setTitle("Multichat");
	    this.setSize(800, 600);
	    this.setLocationRelativeTo(null);

	    chat = new JTextPane();
	    chat.setEditable(false);
	    chat.setBackground(new Color(50, 50, 50));
	    
	    keyWord = new SimpleAttributeSet();
	    StyleConstants.setForeground(keyWord, Color.WHITE);
	    
	    message = new JEditorPane();
	    
	    box = Box.createVerticalBox();
	    box.add(message);
	    
	    int condition = JComponent.WHEN_FOCUSED;
	    InputMap iMap = message.getInputMap(condition);
	    ActionMap aMap = message.getActionMap();

	    String enter = "enter";
	    iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
	    
	    // action quand on appuie sur entrée
	    aMap.put(enter, new AbstractAction()
	    {
	       @Override
	       public void actionPerformed(ActionEvent arg0)
	       {
	    	   	try
	    	   	{
	    	   		server.sendMessage(message.getText());
	    	   	}
	    	   	catch(IOException e)
	    	   	{
	    	   		e.printStackTrace();
	    	   	}
	    	   	message.setText("");
	       }
	    });
	    
	    pan = new JPanel(new BorderLayout());
	    pan.setBackground(new Color(50, 150, 150));
	    pan.setLayout(new BorderLayout());
	    
	    pan.add(chat, BorderLayout.CENTER);
	    pan.add(box, BorderLayout.SOUTH);
	    
	    this.setContentPane(pan);
	    this.setVisible(true);
	    
		server = new multicast.Server(ip, port, this);
		server.start();
	}
	
	public void addMessage(String msg) throws BadLocationException
	{
		StyledDocument doc = chat.getStyledDocument();
	    doc.insertString(doc.getLength(), msg + "\n", keyWord);
	}
}