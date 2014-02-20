package util.progress;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ProgressWindow extends JFrame implements ProgressListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_MILIS_BEFORE_VISIBLE = 500;
	public static final String DEFAULT_CANCEL_TEXT = "Cancel";
	
	private static final Insets CANCEL_INSETS = new Insets(10, 10, 10, 10);
	
	private int m_MiliSecsBeforeVisible;
	private ProgressPanel m_Panel;
	private JButton m_Cancel;
	
	protected ProgressWindow(){
		this("");
	}
	
	protected ProgressWindow(int miliSecsBeforeVisible){
		this("", miliSecsBeforeVisible);
	}
	
	protected ProgressWindow(String title){
		this(title, DEFAULT_MILIS_BEFORE_VISIBLE);
	}
	
	protected ProgressWindow(String title, int miliSecsBeforeVisible){
		super(title);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				onClose();
			}
		});
		m_MiliSecsBeforeVisible = miliSecsBeforeVisible;
		m_Panel = this.createProgressPanel();
		m_Cancel = new JButton(this.createCancelText());
		m_Cancel.addActionListener(this);
		this.layout(m_Panel, m_Cancel);
		this.pack();
	}
	
	protected ProgressPanel createProgressPanel(){
		return new ProgressPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(Object source, int progress,
					int maxProgress, String message, boolean indeterminant) {
				//do nothing
			}

			@Override
			protected void onCanceled(Object source) {
				ProgressWindow.this.onCancel();
			}

			@Override
			protected void onCompleted(Object source) {
				ProgressWindow.this.onComplete();
			}
			
		};
	}
	
	protected void onClose(){
		this.onCancel();
	}
	
	protected void onCancel(){
		this.setVisible(false);
		this.setShouldDisplay(false);
		this.dispose();
	}
	
	protected void onComplete(){
		this.setVisible(false);
		this.setShouldDisplay(false);
		this.dispose();
	}
	
	protected void layout(JPanel progressPanel, JButton cancel){
		this.getContentPane().setLayout(new GridBagLayout());
		this.setResizable(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(progressPanel, gbc);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(cancel);
		gbc.gridx = 0;
		gbc.insets = CANCEL_INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(buttonPanel, gbc);
	}
	
	protected String createCancelText(){
		return DEFAULT_CANCEL_TEXT;
	}
	
	protected boolean shouldDisplay(){
		return m_ShouldDisplay;
	}
	
	protected void setShouldDisplay(boolean shouldDisplay){
		m_ShouldDisplay = shouldDisplay;
	}
	
	private Timer m_Timer = null;
	private boolean m_ShouldDisplay = true;
	
	@Override
	public void update(final ProgressEvent event) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				if(event.getType() == ProgressEvent.Type.UPDATE){
					if(m_Timer == null){
						m_Timer = new Timer(m_MiliSecsBeforeVisible, ProgressWindow.this);
						m_Timer.setRepeats(false);
						m_Timer.start();
					}
				}
				m_Panel.update(event);
			}
			
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_Cancel){
			this.onCancel();
		}
		if(e.getSource() == m_Timer){
			if(this.shouldDisplay() && !this.isVisible()){
				this.setVisible(true);
			}
		}
	}
	
	
	
}
