package chiptools.jprobe.function.agilentformatter.component;

import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import chiptools.jprobe.function.agilentformatter.DataCategory;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.components.DataSelectionPanel;

public class DataCategoriesSelectionComponent<D extends Data> extends DataSelectionPanel<D> {
	private static final long serialVersionUID = 1L;
	
	private final JTextField m_Text;

	public DataCategoriesSelectionComponent(JProbeCore core, boolean optional) {
		super(core, optional);
		m_Text = this.createCategoryField();
		this.add(m_Text, this.createTextFieldConstraints());
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				updateCategory(m_Text, getSelectedData());
			}
			
		});
	}
	
	protected void updateCategory(JTextField text, D selected){
		if(selected != null){
			String name = this.getCore().getDataManager().getDataName(selected);
			text.setText(name);
		}else{
			text.setText("");
		}
		text.setEnabled(selected != null);
		this.revalidate();
	}
	
	@Override
	public void setEnabled(boolean enabled){
		m_Text.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	public DataCategory<D> getDataCategory(){
		if(this.getSelectedData() == null){
			return null;
		}
		return new DataCategory<D>(m_Text.getText(), this.getSelectedData());
	}
	
	protected JTextField createCategoryField(){
		return new JTextField();
	}
	
	protected GridBagConstraints createTextFieldConstraints(){
		GridBagConstraints gbc = super.createDataComboBoxConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		return gbc;
	}
	
	@Override
	protected GridBagConstraints createDataComboBoxConstraints(){
		GridBagConstraints gbc = super.createDataComboBoxConstraints();
		gbc.weightx = 0;
		gbc.gridx = 1;
		return gbc;
	}
	
	@Override
	protected GridBagConstraints createCloseButtonConstraints(){
		GridBagConstraints gbc = super.createCloseButtonConstraints();
		gbc.gridx = 2;
		return gbc;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e){
		if(m_Text != null){
			this.updateCategory(m_Text, this.getSelectedData());
		}
		super.itemStateChanged(e);
	}
	
	
	
}
