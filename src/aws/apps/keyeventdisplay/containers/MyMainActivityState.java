package aws.apps.keyeventdisplay.containers;

public class MyMainActivityState {
	private CharSequence logText;
	private boolean chkKeyEvents;
	private boolean chkLogcat;
	private boolean chkKenel;

	public CharSequence getLogText() {
		return logText;
	}
	public boolean isChkKenel() {
		return chkKenel;
	}
	public boolean isChkKeyEvents() {
		return chkKeyEvents;
	}
	public boolean isChkLogcat() {
		return chkLogcat;
	}
	public void setChkKenel(boolean chkKenel) {
		this.chkKenel = chkKenel;
	}
	public void setChkKeyEvents(boolean chkKeyEvents) {
		this.chkKeyEvents = chkKeyEvents;
	}
	public void setChkLogcat(boolean chkLogcat) {
		this.chkLogcat = chkLogcat;
	}
	public void setLogText(CharSequence logText) {
		this.logText = logText;
	}


}
