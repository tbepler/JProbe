package chiptools.jprobe.function.params;

import java.util.List;

import chiptools.jprobe.data.PWM;

public interface PWMListParam {
	
	public void setPWMs(List<PWM> pwms);
	public List<PWM> getPWMs();
	
}
