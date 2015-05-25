package com.yofungate.misc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class misc {
	public static boolean sendGET(String _url) throws Exception{
		URL url = new URL(_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.99");
		int responseCode = con.getResponseCode();
		if(responseCode==200)return true;
		return false;
	}
	public static BufferedReader getGET(String _url) throws Exception{
		URL url = new URL(_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.99");
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		if(responseCode==200)return in;
		return null;
	}
	public static boolean sendPOST(String _url,String _args) throws Exception{
		URL url = new URL(_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.99");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(_args);
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		if(responseCode==200)return true;
		return false;
	}
	public static BufferedReader getPOST(String _url,String _args) throws Exception{
		URL url = new URL(_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.99");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(_args);
		wr.flush();
		wr.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		int responseCode = con.getResponseCode();
		if(responseCode==200)return in;
		return null;
	}
	public static String escapeHTML(String _input){
		String output = _input;
		output.replaceAll("&", "&amp;");
		output.replaceAll("<", "&lt;");
		output.replaceAll(">", "&gt;");
		output.replaceAll("'", "&quot;");
		output.replaceAll("\"", "&#x27;");
		output.replaceAll("/", "&#x2F;");
		return output;
	}
	public static int randInt(int min, int max) {
	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();
	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	public static class RThread extends Thread{	
		public Thread rename(String s){
			setName(s);
			return this;
		}
	}
}
