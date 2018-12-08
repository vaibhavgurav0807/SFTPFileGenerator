package com.ge.des.mems.mfib.ob.filegenerator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

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
 * Class to accept and send GET request and upload response file to SFTP location
 */
public class Send_HTTP_Request {
public static void main(String[] args) {
	
	try {
		
        Send_HTTP_Request.call_me();
        Send_HTTP_Request.upload();
	} catch (Exception e) {
        e.printStackTrace();
	}
}


public static void call_me() throws Exception {
	String url = "http://18.219.23.28:8080/MemsLatestDeviceData/getAllDevices";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
        String inputLine;
        File file=new File("C:/Users/ISHWARI/Desktop/New folder/test.txt");   
        FileWriter fileWriter = new FileWriter(file); 
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
        	response.append(inputLine);
        	 fileWriter.write(inputLine); 
        }
        in.close();
        JSONArray object = new JSONArray(response.toString());
       
        for (int i = 0; i < object.length(); i++) {
            JSONObject json = object.getJSONObject(i);        
        }
        fileWriter.flush();  
        fileWriter.close();        
	}

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
		            sftpChannel.put("C:/Users/ISHWARI/Desktop/New folder/test.txt", "/home/ubuntu/Test/test.txt");  
		            sftpChannel.exit();
		            session.disconnect();
		        } catch (JSchException e) {
		            e.printStackTrace();  
		        } catch (SftpException e) {
		            e.printStackTrace();
		        }			
		}
}