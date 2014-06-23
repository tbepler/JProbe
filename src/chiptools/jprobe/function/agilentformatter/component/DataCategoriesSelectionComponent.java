package chiptools.jprobe.function.agilentformatter.component;

import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;

import javax.swing.JTextField;

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
		this.updateCategory(m_Text, this.getSelectedData());
	}
	
	protected void updateCategory(JTextField text, D selected){
		if(selected != null){
			String name = this.getCore().getDataManager().getDataName(selected);
			text.setText(name);
		}else{
			text.setText("");
		}
	}
	
	@Override
	public void setEnabled(boolean enabled){
		m_Text.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	public DataCategory<D> getDataCategory(){
		return new DataCategory<D>(m_Text.getText(), this.getSelectedData());
	}
	
	protected JTextField createCategoryField(){
		return new JTextField();
	}
	
	protected GridBagConstraints createTextFieldConstraints(){
		GridBagConstraints gbc = super.createDataComboBoxConstraints();
		gbc.gridx = 0;
		return gbc;
	}
	
	@Override
	protected GridBagConstraints createDataComboBoxConstraints(){
		GridBagConstraints gbc = super.createDataComboBoxConstraints();
		gbc.gridx = 1;
		return gbc;
	}
	
	@Override
	protected GridBagConstraints createCloseButtonConstraints(){
		GridBagConstraints gbc = this.createCloseButtonConstraints();
		gbc.gridx = 2;
		return gbc;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e){
		this.updateCategory(m_Text, this.getSelectedData());
		super.itemStateChanged(e);
	}
	
	
	
}