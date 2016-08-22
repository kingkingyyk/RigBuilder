package org.kingkingyyk.rigbuilder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.kingkingyyk.rigbuilder.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private static String ListURL="https://docs.google.com/spreadsheets/u/0/d/1R3AtjtXBSlkhYKjbLb31_Iw2834ojviTNpwY3izZTwU/export?format=tsv";

	public ArrayList<String> greetServer(String input) throws IllegalArgumentException {
		try {
			URL u=new URL(ListURL);
			BufferedReader br=new BufferedReader(new InputStreamReader(u.openStream()));
			ArrayList<String> list=new ArrayList<>();
			String s;
			while ((s=br.readLine())!=null) {
				list.add(s);
			}
			br.close();
			return list;
		}catch (IOException e) {
			System.out.println("FAIL");
			return null;
		}
	}

}
