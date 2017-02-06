package org.kingkingyyk.rigbuilder.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RigBuilder implements EntryPoint {
	
	public final String [] titleColors={"#1abc9c","#2ecc71","#16a085","#27ae60","#3498db",
									   "#2980b9","#9b59b6","#8e44ad","#34495e","#2c3e50",
									   "#f1c40f","#e67e22","#e74c3c","#f39c12","#d35400",
									   "#7f8c8d"};
	public final String currTitleColor=titleColors[Random.nextInt(titleColors.length)];
	public final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	
	private static class StringTokenizer {
		private String [] sAry;
		private int currIndex;
		
		public StringTokenizer (String n) {
			sAry=n.split("\t");
		}
		
		public String nextToken() {
			return sAry[currIndex++];
		}
		
		public boolean hasMoreTokens() {
			return currIndex<sAry.length;
		}
		
		public int countTokens() {
			return sAry.length;
		}
	}
	
	public HorizontalPanel titlePanel;
	public Label lblPrice;
	public ListBox comboBoxRecommendedBuilds;
	public Button btnReview;
	
	public void drawTitle() {
		final ReviewPanel cpPanel=new ReviewPanel();
		
		HorizontalPanel hp=new HorizontalPanel();
		hp.setStyleName("title");
		hp.getElement().getStyle().setBackgroundColor(currTitleColor);
			Label lblTitle=new Label("kingkingyyk's Rig Recommendation & Builder");
				lblTitle.setStyleName("titleText");
			hp.add(lblTitle);
			
			comboBoxRecommendedBuilds=new ListBox();
				comboBoxRecommendedBuilds.setStyleName("cardComboBox");
				comboBoxRecommendedBuilds.getElement().getStyle().setMarginTop(8,Unit.PX);
				comboBoxRecommendedBuilds.getElement().getStyle().setWidth(300,Unit.PX);
				comboBoxRecommendedBuilds.setVisible(false);
			hp.add(comboBoxRecommendedBuilds);
			
			lblPrice=new Label("Total : RM 0");
				lblPrice.setStyleName("priceText");
				lblPrice.setVisible(false);
			hp.add(lblPrice);
		
			btnReview=new Button("Review");
				btnReview.getElement().setPropertyString("class", "reviewBtn");
				btnReview.getElement().setPropertyString("data-clipboard-target", "topkekekek");
				btnReview.setStyleName("reviewBtn");
				btnReview.setVisible(false);
				btnReview.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						int startIndex=indexOfStartsWith(0,"BUILD START");
						StringTokenizer st=new StringTokenizer(list.get(startIndex));
						st.nextToken(); st.nextToken(); //ignore.
						
						ArrayList<String> hardwareList=new ArrayList<>();
						while (st.hasMoreTokens()) hardwareList.add(st.nextToken());
						
						String toReview="<div><span>";
						for (String hardware : hardwareList) {
							ListBox comboBox=hardwareComboBox.get(hardware);
							if (!comboBox.getSelectedItemText().equals("N/A")) {
								toReview+=comboBox.getSelectedItemText();
								toReview+=" RM";
								toReview+=priceMap.get(comboBox.getSelectedItemText());
								toReview+="<br/><div class=\"line-separator\"></div>";
							}
						}
						toReview+=lblPrice.getText();
						toReview+="</span></div>";
						
						cpPanel.setText(toReview);
						cpPanel.center();
						cpPanel.show();
					}
				});
			hp.add(btnReview);
		titlePanel=hp;
		RootPanel.get().add(hp);
		
		hp=new HorizontalPanel(); // no idea. without this, the top panel will be at middle.
		RootPanel.get().add(hp);
	}
	
	public void showAllTitleElements() {
		lblPrice.setVisible(true);
		comboBoxRecommendedBuilds.setVisible(true);
		btnReview.setVisible(true);
	}
	
	public Label lblLoading;
	public long loadscreenInitTime;
	
	public void showLoadscreen () {
		lblLoading=new Label("Loading...");
		lblLoading.setStyleName("loadingTextLoop");
		RootPanel.get().add(lblLoading);
		loadscreenInitTime=System.currentTimeMillis();
	}
	
	public void setLoadscreenText(String s) {
		lblLoading.setText(s);
	}
	
	public int destroyLoadscreen() {
		lblLoading.addStyleName("loadingTextEnd");
		Timer destroyTimer=new Timer() {
			@Override
			public void run() {
				RootPanel.get().remove(lblLoading);
			}
		};
		int delay=3000-(int)((System.currentTimeMillis()-loadscreenInitTime)%3000);
		destroyTimer.schedule(delay);
		return delay;
	}
	
	private HashMap<String,Integer> priceMap=new HashMap<>();
	private HashMap<String,String> logoMap=new HashMap<>();
	private HashMap<String,String> productPageMap=new HashMap<>();
	private ArrayList<String> list=new ArrayList<>();
	
	public int indexOfStartsWith(int startIndex,String target) {
		for (int i=startIndex;i<list.size();i++) if (list.get(i).startsWith(target)) return i;
		return -1;
	}
	
	public void setupBackground() {
		final ArrayList<String> backgrounds=new ArrayList<>();
		int backgroundIndex=indexOfStartsWith(0,"BACKGROUND IMAGES");

		StringTokenizer st=new StringTokenizer(list.get(backgroundIndex));
		st.nextToken(); //remove the background image
		while (st.hasMoreTokens()) backgrounds.add(st.nextToken());
		RootPanel.getBodyElement().getStyle().setProperty("transition","background 1000ms ease-in 1000ms");
		RootPanel.getBodyElement().getStyle().setBackgroundImage("url("+backgrounds.get(Random.nextInt(backgrounds.size()))+")");

		Timer backgroundTimer=new Timer() {
			private int id=Random.nextInt(backgrounds.size());
			@Override
			public void run() {
				RootPanel.getBodyElement().getStyle().setBackgroundImage("url("+backgrounds.get(id)+")");
				id=(id+1)%backgrounds.size();
			}
		};
		backgroundTimer.scheduleRepeating(10000);
	}
	
	public static HashMap<String,String> hardwareInfoMap=new HashMap<>();
	public static HashMap<String,ListBox> hardwareComboBox=new HashMap<>();
	public static HashMap<ListBox,HashMap<String,Integer>> hardwareComboBoxItemPosition=new HashMap<>();
	public static HashMap<Image,String> imageHyperlink=new HashMap<>();
	public static String loadingImageUrl="https://mir-s3-cdn-cf.behance.net/project_modules/disp/585d0331234507.564a1d239ac5e.gif";
	
	public void addHardwareCard(String s, ArrayList<String> comboBoxValues) {
		final VerticalPanel vp=new VerticalPanel();
		vp.setStyleName("cardPanel");
		
			HorizontalPanel hp=new HorizontalPanel();
			vp.add(hp);
			
			final HorizontalPanel infoPanel=new HorizontalPanel();
				infoPanel.setStyleName("cardTextInfoPanel");
				final Label lblInfo=new Label();
				lblInfo.setStyleName("cardTextInfo");
				lblInfo.setVisible(false);
				infoPanel.add(lblInfo);
			vp.add(infoPanel);
				
				Label titleText=new Label(s);
					titleText.setStyleName("cardTextLeft");
				hp.add(titleText);
				
				final ListBox comboBox=new ListBox();
					comboBox.setStyleName("cardComboBox");
					hardwareComboBox.put(s,comboBox);
					hardwareComboBoxItemPosition.put(comboBox,new HashMap<String,Integer>());
				hp.add(comboBox);
				
				HorizontalPanel imageHP=new HorizontalPanel();
				imageHP.setStyleName("cardLogoImagePanel");
					final Image logoImage=new Image();
						logoImage.setStyleName("cardLogoImage");
					imageHP.add(logoImage);
				hp.add(imageHP);
				
				final Label priceText=new Label("0");
					priceText.setStyleName("cardTextRight");
				hp.add(priceText);
			
				for (String values : comboBoxValues) {
					comboBox.addItem(values);
					hardwareComboBoxItemPosition.get(comboBox).put(values,comboBox.getItemCount()-1);
				}
				
				logoImage.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (imageHyperlink.containsKey(logoImage)) Window.open(imageHyperlink.get(logoImage),"_blank","");
					}
				});
				comboBox.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						final String selected=comboBox.getSelectedItemText();
						priceText.setText(String.valueOf(priceMap.get(selected)));
						
						if (!logoImage.getUrl().equals(loadingImageUrl)) logoImage.setUrl(loadingImageUrl);
						if (hardwareInfoMap.containsKey(selected)) {
							infoPanel.setHeight("25px");
							lblInfo.setText(hardwareInfoMap.get(selected));
						} else {
							infoPanel.setHeight("0px");
							lblInfo.setText("");
						}
						
						Timer t=new Timer() {
							public void run () {
								String currSelected=comboBox.getSelectedItemText();
								if (currSelected.equals(selected)) {
									String brand=selected.split(" ")[0];
									if (logoMap.containsKey(brand)) logoImage.setUrl(logoMap.get(brand));
									else logoImage.setUrl(logoMap.get("N/A"));
									
									if (productPageMap.containsKey(selected)) {
										logoImage.getElement().getStyle().setProperty("cursor", "pointer");
										logoImage.setTitle("Click me to go product page!");
										imageHyperlink.put(logoImage,productPageMap.get(selected));
									} else {
										logoImage.getElement().getStyle().setProperty("cursor", "default");
										logoImage.setTitle("");
										imageHyperlink.remove(logoImage);
									}
									
									lblInfo.setVisible(hardwareInfoMap.containsKey(selected));
								}
							}
						};
						t.schedule(350);
						
						calcTotalPrice();
					}
				});
			
		RootPanel.get().add(vp);
	}
	
	public void calcTotalPrice() {
		int count=0;
		for (ListBox b : hardwareComboBox.values()) count+=priceMap.get(b.getSelectedItemText());
		
		lblPrice.setText("Total : RM "+count);
	}
	
	public void setupTable() {
		//=============== Add some padding below title!===============
		final HorizontalPanel titleBtmPadding=new HorizontalPanel();
		titleBtmPadding.setStyleName("titleBottomPadding");
		titleBtmPadding.getElement().getStyle().setHeight(titlePanel.getElement().getClientHeight(),Unit.PX);
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				titleBtmPadding.getElement().getStyle().setHeight(titlePanel.getElement().getClientHeight(),Unit.PX);
			}
		});
		RootPanel.get().add(titleBtmPadding);
		//===============Get hardware from build columns=================
		int startIndex=indexOfStartsWith(0,"BUILD START");
		StringTokenizer st=new StringTokenizer(list.get(startIndex));
		st.nextToken(); st.nextToken(); //ignore.
		
		ArrayList<String> hardwareList=new ArrayList<>();
		while (st.hasMoreTokens()) hardwareList.add(st.nextToken());
		
		//================Get logo link================
		int logoIndexStart=indexOfStartsWith(0,"LOGO STARTS");
		int logoIndexEnd=indexOfStartsWith(logoIndexStart,"LOGO ENDS");
		for (int i=logoIndexStart;i<logoIndexEnd;i++) {
			st=new StringTokenizer(list.get(i));
			logoMap.put(st.nextToken(),st.nextToken());
		}
		
		//================Get hardware and models beneath==========
		for (String hardware : hardwareList) {
			ArrayList<String> models=new ArrayList<>();
			for (int index=indexOfStartsWith(0,hardware)+1;true;index++) {
				st=new StringTokenizer(list.get(index));
				if (st.countTokens()>1) {
					String model=st.nextToken();
					int price=Integer.parseInt(st.nextToken());
					priceMap.put(model,price);
					if (st.hasMoreTokens()) productPageMap.put(model,st.nextToken());
					if (st.hasMoreTokens()) hardwareInfoMap.put(model,st.nextToken());
					
					models.add(model);
				} else break;
			}
			addHardwareCard(hardware,models);
		}
	}
	
	public static HashMap<String,RecommendedBuild> recomBuildMap=new HashMap<>();
	public void setupRecommendedBuild() {
		//Get Column.
		int startIndex=indexOfStartsWith(0,"BUILD START");
		int endIndex=indexOfStartsWith(0,"BUILD END");
		StringTokenizer st=new StringTokenizer(list.get(startIndex));
		st.nextToken(); st.nextToken(); //ignore.
		
		ArrayList<String> hardwareList=new ArrayList<>();
		while (st.hasMoreTokens()) hardwareList.add(st.nextToken());
		
		ArrayList<RecommendedBuild> buildList=new ArrayList<>();
		//Get builds.
		for (int i=startIndex+1;i<endIndex;i++) {
			st=new StringTokenizer(list.get(i));
			String name=st.nextToken();
			st.nextToken(); //not used ;)
			
			RecommendedBuild rb=new RecommendedBuild();
			int price=0;
			for (int i2=0;st.hasMoreTokens();i2++) {
				String comp=st.nextToken();
				rb.component.put(hardwareList.get(i2),comp);
				if (!priceMap.containsKey(comp)) Window.alert("Price not found for "+comp);
				else price+=priceMap.get(comp);
			}
			
			price=((price+99)/100)*100;
			rb.name=name+" - RM"+price;
			buildList.add(rb);
			recomBuildMap.put(rb.name, rb);
		}
		
		//add builds to combobox
		for (RecommendedBuild rb : buildList) comboBoxRecommendedBuilds.addItem(rb.name);
		
		//register combobox event
		comboBoxRecommendedBuilds.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				RecommendedBuild rb=recomBuildMap.get(comboBoxRecommendedBuilds.getSelectedItemText());
				for (String hardware : rb.component.keySet()) {
					ListBox comboBox=hardwareComboBox.get(hardware);
					comboBox.setSelectedIndex(hardwareComboBoxItemPosition.get(comboBox).get(rb.component.get(hardware)));
					DomEvent.fireNativeEvent(Document.get().createChangeEvent(), comboBox);
				}
			}
		});
	}
	
	public void setupFooter() {
		HorizontalPanel hp=new HorizontalPanel();
		hp.getElement().getStyle().setHeight(60,Unit.PX);
		RootPanel.get().add(hp);
		
		hp=new HorizontalPanel();
		hp.setStyleName("endPanel");
		hp.getElement().getStyle().setBackgroundColor(currTitleColor);
		
			Image lblGithub=new Image("/images/github-icon.png");
				lblGithub.setStyleName("gitHubIcon");
				lblGithub.setTitle("Repository @ GitHub");
				lblGithub.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent ev) {
						Window.open("https://github.com/kingkingyyk/RigBuilder","_blank","");
					}
				});
			hp.add(lblGithub);
	
			HTML endText=new HTML("Since there is no constraints in mix-and-match, it is important to check the compatibility before buying!<br />"
									+ "Price Update : "+list.get(0)+" | Site : Version 2.0.2 [6 Feb 2017] | Powered by Google Web Toolkit 2.7.0");
				endText.setStyleName("endPanelText");
			hp.add(endText);
			
		RootPanel.get().add(hp);
	}
	public void onModuleLoad() {
		RootPanel.getBodyElement().getStyle().setMargin(0,Unit.PX);
		RootPanel.getBodyElement().getStyle().setPadding(0,Unit.PX);
		drawTitle();
		showLoadscreen();

		greetingService.greetServer("", new AsyncCallback<ArrayList<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				lblLoading.getElement().getStyle().setProperty("animationIterationCount","0");
				setLoadscreenText(":( We have encountered problem when loading data.");
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				if (result==null) onFailure(null);
				else {
					setLoadscreenText("Done Loading !");
					int delay=destroyLoadscreen();
					showAllTitleElements();
					list=result;
					//setupBackground();
					Timer tableTimer=new Timer() {
						@Override
						public void run() {
							setupTable();
							setupRecommendedBuild();
							setupFooter();
						}
					};
					tableTimer.schedule(delay);
				}
			}
		});
	}
}
