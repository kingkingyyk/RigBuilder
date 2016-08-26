package org.kingkingyyk.rigbuilder.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

public class RigBuilder implements EntryPoint {

	public String [] hardware;
	public final HashMap<String,Item> strToItem=new HashMap<>();
	public final HashMap<String,Component> strToComp=new HashMap<>();
	public final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	public final ArrayList<Build> recomBuilds=new ArrayList<>();
	
	private static class Build {
		String category;
		String name;
		HashMap<String,Item> map;
	}
	
	private static class Component {
		String name;
		ArrayList<Item> itemList;
	}
	private static class Item {
		String name;
		int price;
	}
	
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
	}
	
	private ArrayList<String> list=new ArrayList<>();
	
	public void onModuleLoad() {
		RootPanel.getBodyElement().getStyle().setBackgroundColor("black");
		
		final HorizontalPanel recomHP=new HorizontalPanel();
		final Label recomText=new Label("Recommended Builds :  ");
		final ListBox recomlb=new ListBox();
		recomText.getElement().getStyle().setProperty("color", "white");
		recomlb.getElement().getStyle().setBackgroundColor("rgba(0,0,0,0.6)");
		recomlb.getElement().getStyle().setColor("white");
		recomHP.add(recomText);
		recomHP.add(recomlb);
		RootPanel.get("recommended").add(recomHP);
		
		final Label sep=new Label("--------------------------------------------");
		sep.getElement().getStyle().setProperty("color", "white");
		RootPanel.get("seperator").add(sep);
				
		final HorizontalPanel hp=new HorizontalPanel();
		final Label totalTxt=new Label("Total : RM ");
		final Label totalValue=new Label("0");
		hp.add(totalTxt);
		hp.add(totalValue);
		totalTxt.getElement().getStyle().setProperty("fontSize", "150%");
		totalTxt.getElement().getStyle().setProperty("color", "white");
		totalValue.getElement().getStyle().setProperty("color", "red");
		totalValue.getElement().getStyle().setProperty("fontWeight", "bold");
		totalValue.getElement().getStyle().setProperty("fontSize", "150%");
		RootPanel.get("total").add(hp);
		
		final Label disclaimer=new Label("Since there is no constraints in mix-and-match, it is important to check the compatiblity before buying!");
		disclaimer.getElement().getStyle().setProperty("color", "white");
		RootPanel.get("disclaimer").add(disclaimer);		
		
		final HTML lastUpdate=new HTML();
		lastUpdate.setHTML("Loading");
		lastUpdate.getElement().getStyle().setProperty("color", "white");
		RootPanel.get("lastUpdate").add(lastUpdate);
		
		greetingService.greetServer("", new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
				lastUpdate.setHTML("Fail");
			}

			public void onSuccess(ArrayList<String> result) {
				list=result;
				lastUpdate.setHTML("Price Update : "+list.get(0)+" | Site Update : 26 Aug 2016<br/>Source Code available on <a href=\"https://github.com/kingkingyyk/RigBuilder\">GitHub</a> | Powered by Google Web Toolkit 2.7.0");
				
				//for background
				final ArrayList<String> backgrounds=new ArrayList<>();
				int backgroundIndex=0;
				for (int i=0;i<list.size();i++) {
					if (list.get(i).startsWith("BACKGROUND IMAGES")) {
						backgroundIndex=i;
						break;
					}
				}
				StringTokenizer st=new StringTokenizer(list.get(backgroundIndex));
				st.nextToken(); //remove the background image
				while (st.hasMoreTokens()) {
					backgrounds.add(st.nextToken());
				}
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

				
				//for builds.
				int startIndex=0;
				for (int i=0;i<list.size();i++) {
					if (list.get(i).startsWith("BUILD START")) {
						startIndex=i;
						break;
					}
				}
				int endIndex=0;
				for (int i=startIndex;i<list.size();i++) {
					if (list.get(i).startsWith("BUILD END")) {
						endIndex=i;
						break;
					}
				}
				
				ArrayList<String> componentList=new ArrayList<>();
				st=new StringTokenizer(list.get(startIndex));
				st.nextToken(); st.nextToken();
				while (st.hasMoreTokens()) {
					componentList.add(st.nextToken());
				}
				
				hardware=componentList.toArray(new String [componentList.size()]);
				for (int i=0;i<hardware.length;i++) {
					Component c=new Component();
					c.name=hardware[i];
					c.itemList=new ArrayList<>();
					strToComp.put(hardware[i],c);
				}
				for (int i=startIndex+1;i<endIndex;i++) {
					st=new StringTokenizer(list.get(i));
					Build b=new Build();
					b.category=st.nextToken();
					b.name=st.nextToken();
					b.map=new HashMap<>();
					for (int i2=0;i2<hardware.length;i2++) {
						String n=st.nextToken();
						Item it=strToItem.get(hardware[i2]+n);
						if (it==null) {
							it=new Item();
							it.name=n;
							strToItem.put(hardware[i2]+n,it);
						}
						b.map.put(hardware[i2],it);
					}
					recomBuilds.add(b);
				}
				//price...
				Component currComp=null;
				for (int i=endIndex+1;i<list.size();i++) {
					st=new StringTokenizer(list.get(i));
					String s=st.nextToken();
					if (strToComp.containsKey(s)) {
						currComp=strToComp.get(s);
					} else {
						Item it=strToItem.get(currComp.name+s);
						if (it==null) {
							it=new Item();
							it.name=s;
							strToItem.put(currComp.name+s,it);
						}
						it.price=Integer.parseInt(st.nextToken());
						currComp.itemList.add(it);
					}
				}
				//calculate build price.
				for (Build b : recomBuilds) {
					int sum=0;
					for (int i=0;i<hardware.length;i++) {
						Item it = b.map.get(hardware[i]);
						sum+=it.price;
					}
					sum=((sum+99)/100)*100;
					b.name=" RM"+sum;
					recomlb.addItem(b.category+" - "+b.name);
				}
				
				final HashMap<ListBox,Integer> boxPosition=new HashMap<>();
				final Grid g=new Grid(hardware.length,3);
				final ListBox [] lb=new ListBox [hardware.length];
				for (int i=0;i<hardware.length;i++) {
					Label componentName=new Label(hardware[i]);
					componentName.getElement().getStyle().setProperty("color", "white");
					g.setWidget(i,0,componentName);
					currComp=strToComp.get(hardware[i]);
					lb[i]=new ListBox();
					lb[i].getElement().getStyle().setWidth(350, Unit.PX);
					lb[i].getElement().getStyle().setBackgroundColor("rgba(0,0,0,0.6)");
					lb[i].getElement().getStyle().setColor("white");
					for (Item it : currComp.itemList) {
						lb[i].addItem(it.name);
					}
					boxPosition.put(lb[i],i);
					lb[i].addChangeHandler(new ChangeHandler() {
						@Override
						public void onChange(ChangeEvent event) {
							ListBox fired=(ListBox)event.getSource();
							((Label)g.getWidget(boxPosition.get(fired),2)).setText(String.valueOf(strToItem.get(g.getText(boxPosition.get(fired),0)+fired.getSelectedItemText()).price));
							int sum=0;
							for (int i=0;i<hardware.length;i++) {
								sum+=Integer.parseInt(g.getText(i,2));
							}
							totalValue.setText(String.valueOf(sum));
						}
						
					});
					g.setWidget(i,1, lb[i]);
					Label price=new Label("0");
					price.getElement().getStyle().setProperty("color", "white");
					g.setWidget(i,2,price);
				}

				RootPanel.get("spec").add(g);
				
				recomlb.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						for (int i=0;i<hardware.length;i++) {
							String comp=recomBuilds.get(recomlb.getSelectedIndex()).map.get(hardware[i]).name;
							for (int i2=0;i2<lb[i].getItemCount();i2++) {
								if (lb[i].getItemText(i2).equals(comp)) {
									lb[i].setSelectedIndex(i2);
									break;
								}
							}
							
							((Label)g.getWidget(i,2)).setText(String.valueOf(strToItem.get(g.getText(i,0)+lb[i].getSelectedItemText()).price));
							
						}
						int sum=0;
						for (int i=0;i<hardware.length;i++) {
							sum+=Integer.parseInt(g.getText(i,2));
						}
						totalValue.setText(String.valueOf(sum));
					}
				});
			}
		});
	}
}
