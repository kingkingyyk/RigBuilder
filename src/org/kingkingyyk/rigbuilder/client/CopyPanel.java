package org.kingkingyyk.rigbuilder.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class CopyPanel extends PopupPanel {
	private HTML txt;
	  
	public CopyPanel() {
		txt=new HTML();
		txt.setStyleName("copyPanelText");
		add(txt);
		setStyleName("copyPanel");
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setGlassEnabled(true);
		setAnimationType(AnimationType.ROLL_DOWN);
		hide();
		
		getGlassElement().getStyle().setProperty("width","100%");
		getGlassElement().getStyle().setProperty("height","100%");
		getGlassElement().getStyle().setBackgroundColor("black");
		getGlassElement().getStyle().setOpacity(0.5);
	}
	
	public void setText(String s) {
		txt.setHTML(s);
	}
}
