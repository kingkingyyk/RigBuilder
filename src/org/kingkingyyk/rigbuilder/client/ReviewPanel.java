package org.kingkingyyk.rigbuilder.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class ReviewPanel extends PopupPanel {
	private HTML txt;
	  
	public ReviewPanel() {
		txt=new HTML();
		txt.setStyleName("reviewPanelText");
		add(txt);
		setStyleName("reviewPanel");
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setGlassEnabled(true);
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
