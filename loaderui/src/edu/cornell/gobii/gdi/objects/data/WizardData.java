package edu.cornell.gobii.gdi.objects.data;

import java.util.ArrayList;
import java.util.List;

public class WizardData {

	public List<String> header = new ArrayList<>();
	public boolean hasOrientation = false;
	public int orientation = 0;
	public List<String> getHeader() {
		return header;
	}
	public void setHeader(List<String> header) {
		this.header = header;
	}
	public boolean isHasOrientation() {
		return hasOrientation;
	}
	public void setHasOrientation(boolean hasOrientation) {
		this.hasOrientation = hasOrientation;
	}
	public int getOrientation() {
		return orientation;
	}
	public void setOriantation(int orientation) {
		this.orientation = orientation;
	}
	
	public WizardData(int orientation, boolean hasOrientation){
		this.orientation = orientation;
		this.hasOrientation = hasOrientation;
	}
	
	public WizardData(int orientation){
		this.orientation = orientation;
	}
}
