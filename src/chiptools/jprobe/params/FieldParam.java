package chiptools.jprobe.params;

import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;

public class FieldParam implements FieldParameter{
	
	private final String m_Name;
	private final String m_Description;
	private final boolean m_Optional;
	private final Field m_InitValue;
	
	public FieldParam(Field initValue, String name, String description, boolean optional){
		m_InitValue = initValue;
		m_Name = name;
		m_Description = description;
		m_Optional = optional;
	}
	
	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public String getDescription() {
		return m_Description;
	}

	@Override
	public boolean isOptional() {
		return m_Optional;
	}

	@Override
	public Field getType() {
		return m_InitValue;
	}

	@Override
	public boolean isValid(Field field) {
		return field.getClass().equals(m_InitValue.getClass());
	}

}
