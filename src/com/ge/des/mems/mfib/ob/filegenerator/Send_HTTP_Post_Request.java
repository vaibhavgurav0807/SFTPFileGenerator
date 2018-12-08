package com.ge.des.mems.mfib.ob.filegenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * @author Vaibhav Gurav
 * Class to accept and send POST request and upload response file to SFTP location
 */
public class Send_HTTP_Post_Request {
	public static void main(String[] args) {
		try {
			Send_HTTP_Post_Request.call_me();
			//Send_HTTP_Post_Request.upload();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method for Http Post Request and save response to local file.
	 * @throws Exception
	 */
	public static void call_me() throws Exception {
			
		 URL url = new URL("http://18.219.23.28:8080/MemsLatestDeviceData/getByDeviceId");
		 URLConnection con = url.openConnection();
		 HttpURLConnection http = (HttpURLConnection)con;
		 http.setDoInput(true);
		 http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		 http.setRequestProperty("Accept", "application/json");
		 http.setDoOutput(true);
		 
		 byte[] out = "{\"deviceId\":\"deviceTXT\"}".getBytes(StandardCharsets.UTF_8);
		 int length = out.length;
		 http.setFixedLengthStreamingMode(length);
		 http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		 http.connect();
		 try(OutputStream os = http.getOutputStream()) {
		     os.write(out);
		 }
		 int responseCode = http.getResponseCode();
	     System.out.println("\nSending 'GET' request to URL : " + url);
	     System.out.println("Response Code : " + responseCode);
	     BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream(),"ASCII"));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	       	response.append(inputLine);
	     }
	     in.close();
	     StringBuilder sb=new StringBuilder();
	     JSONArray object = new JSONArray(response.toString());
	     File file=new File("C:/Users/ISHWARI/Desktop/New folder/test2.txt");   
	     FileWriter fileWriter = new FileWriter(file); 
	     for (int i = 0; i < object.length(); i++) {
	         JSONObject json = object.getJSONObject(i);
	         String str=json.toString();
	         char [] letters=str.toCharArray();
	         for(char c:letters){
	        	 String hex = String.format("%04x", (int) c);
	        	 sb.append(hex);
	         }
	       fileWriter.write(sb.toString());  
	     }
	     fileWriter.flush();  
	     fileWriter.close();
	}
	
	/**
	 * Method to upload file to sftp location.
	 */
	public static void upload() {
		JSch jsch = new JSch();
	    Session session = null;
	    try {
	         session = jsch.getSession("ubuntu", "18.219.23.28", 22);
	         session.setConfig("StrictHostKeyChecking", "no");
	         session.setPassword("girishm32");
	         session.connect();
	         Channel channel = session.openChannel("sftp");
	         channel.connect();
	         ChannelSftp sftpChannel = (ChannelSftp) channel;
	         sftpChannel.put("C:/Users/ISHWARI/Desktop/New folder/test2.txt", "/home/ubuntu/Test/test2.txt");  
	         sftpChannel.exit();
	         session.disconnect();
	    } catch (JSchException e) {
	          e.printStackTrace();  
	    } catch (SftpException e) {
		   e.printStackTrace();
		}	
	}
}