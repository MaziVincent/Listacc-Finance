/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.net;

import android.util.Pair;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controllers.MaiinUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import services.net.view_model.LoginViewModel;
import services.net.view_model.SyncDownloadEntryViewModel;
import services.net.view_model.SyncInfo;

/**
 *
 * @author E-book
 */
public class Network {
    
    public final static String hostUrl = "http://192.168.0.101:5000/"; // 192.168.0.101:5000
    public static String authUrl = hostUrl + "api/auth";
    public static String baseUrl = hostUrl + "api/sync";
    public static boolean isConnected = false;
    public static boolean isSyncingUp = false;
    public static boolean isSyncingDown = false;
    
    public static boolean isConnected(){
        try {
            URL url = new URL(authUrl + "/pingserver"); //values
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                conn.disconnect();
                isConnected = true;
                return true;
            }
            conn.disconnect();
            isConnected = false;
            return false;

	  } catch (MalformedURLException e) {
           // e.printStackTrace();
	  } catch (IOException e) {
           // e.printStackTrace();
          }
        
        return false;
    }
    
    // RETRIVE TOKEN
    public static String getToken(LoginViewModel login){
        try {               
            Gson gson = new Gson();
            login = LoginViewModel.getFullDetails(login);
            if(login == null) return "";
            String json = gson.toJson(login);

            URL url = new URL(authUrl + "/desktoplogin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput( true );
            byte[] out = json .getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            conn.setFixedLengthStreamingMode(length);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.connect();
            try(OutputStream os = conn.getOutputStream()) {
                os.write(out);
            }
            if (conn.getResponseCode() != 200) {
                if(conn.getResponseCode() == 401){
                    return "";
                }
                System.out.println("Failed : HTTP error code : "
                    + conn.getResponseCode());
                return "";
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String body = "";
            while ((output = br.readLine()) != null) {
                    body += output;
            }

            br.close();
            conn.disconnect();
            return body;
            
         } catch (MalformedURLException e) {
               e.getMessage();
         } catch (IOException e) {
               e.getMessage();
         }
        return "";
    }
    
    public static String getTokenUsingHash(LoginViewModel login){
        try {               
            Gson gson = new Gson();
            // login.setPassword(login.getPasswordHash());
            String json = gson.toJson(login);

            URL url = new URL(baseUrl+ "/desktopapphashsignin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput( true );
            byte[] out = json .getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            conn.setFixedLengthStreamingMode(length);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.connect();
            try(OutputStream os = conn.getOutputStream()) {
                os.write(out);
            }
            if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                                    + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            // System.out.println("Output from Server .... \n");
            String body = "";
            while ((output = br.readLine()) != null) {
                    body += output;
            }
            JsonElement elem = new JsonParser().parse(body);
            JsonObject obj = elem.getAsJsonObject();
            body = obj.get("token").getAsString();

            conn.disconnect();
            return body;
         } catch (MalformedURLException e) {
               e.getMessage();
         } catch (IOException e) {
               e.getMessage();
         }
        return "";
    }
    
    
    // GET PC INFO
    public static String GetHostName(){
        try{
            return InetAddress.getLocalHost().getHostName();
        }
        catch(UnknownHostException e){
            System.out.println(e.getMessage());
        }
        return "UNKNOWN PC";
    }
    
    public static String GetAddress(String addressType) {
        String address = "";
        InetAddress lanIp = null;
        boolean correctInterfaceFound = false;
        
        try {
            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();

            while (net.hasMoreElements() && !correctInterfaceFound) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();

                while (addresses.hasMoreElements() 
                        && element.getHardwareAddress() != null && element.getHardwareAddress().length > 0 
                        && !isVMMac(element.getHardwareAddress()) && !correctInterfaceFound) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {

                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }

                    }

                }
            }

            if (lanIp == null)
                return null;

            if (addressType.equals("ip")) {

                address = lanIp.toString().replaceAll("^/+", "");

            } else if (addressType.equals("mac")) {

                address = getMacAddress(lanIp);

            } else {

                throw new Exception("Specify \"ip\" or \"mac\"");

            }

        } catch (UnknownHostException ex) {

            ex.printStackTrace();

        } catch (SocketException ex) {

            ex.printStackTrace();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return address;

    }
    
    private static String getMacAddress(InetAddress ip) {
        String address = null;
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            address = sb.toString();

        } catch (SocketException ex) {

            ex.printStackTrace();

        }

        return address;
    }

    private static boolean isVMMac(byte[] mac) {
        if(null == mac) return false;
        byte invalidMacs[][] = {
                {0x00, 0x05, 0x69},             //VMWare
                {0x00, 0x1C, 0x14},             //VMWare
                {0x00, 0x0C, 0x29},             //VMWare
                {0x00, 0x50, 0x56},             //VMWare
                {0x08, 0x00, 0x27},             //Virtualbox
                {0x0A, 0x00, 0x27},             //Virtualbox
                {0x00, 0x03, (byte)0xFF},       //Virtual-PC
                {0x00, 0x15, 0x5D}              //Hyper-V
        };

        for (byte[] invalid: invalidMacs){
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) return true;
        }

        return false;
    }
    
    public static String getMacAddress2(){

	InetAddress ip;
	try {			
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
            }
            return sb.toString();
			
	} 
        catch (SocketException | UnknownHostException | NullPointerException e) {		
            //e.printStackTrace();
            e.getMessage();
	}
	return null;
   }

    
    
    // UPLOAD
    public static Pair<Boolean, String> uploadData(SyncInfo syncInfo, List<SyncDownloadEntryViewModel> uploadList){
        try{
            boolean success = false;
            //ChangeUpload uploadModel = new UploadResponseViewModel(null, departmentList);
            Gson gson = new Gson();
            String json = gson.toJson(uploadList);
                
            URL url = new URL(baseUrl+ "/upload");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization","Bearer "+ syncInfo.getToken());
            conn.setRequestProperty("Content-Type", "application/json;");
            conn.setDoOutput( true );
            byte[] out = json.getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            conn.setFixedLengthStreamingMode(length);
            conn.connect();
            try(OutputStream os = conn.getOutputStream()) {
                os.write(out);
            }
            if (conn.getResponseCode() == 200) {
                success = true;
                //throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            else {
                if(conn.getResponseCode() == 401){
                    // get token from web server
                    String token = Network.getToken(new LoginViewModel(MaiinUI.loginUser.getEmail(), MaiinUI.loginUser.getPassword()));

                    // save token to file
                    SyncInfo.saveToken(token);
                }
                return new Pair(false, "");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            // System.out.println("Output from Server .... \n");
            String body = "";
            while ((output = br.readLine()) != null) {
                    body += output;
            }
            //JsonElement elem = new JsonParser().parse(body);
            //JsonObject obj = elem.getAsJsonObject();
            //body = obj.get("token").getAsString();

            conn.disconnect();
            return new Pair(success, body);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair(false, "");
    }
    
    
    // DOWNLOAD
    public static String downloadData(SyncInfo syncInfo, boolean firstLaunch) {
        try{
            URL url = new URL(baseUrl+ "/download/" + syncInfo.getLastChangeId() + "/" + firstLaunch);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization","Bearer "+ syncInfo.getToken());
            conn.setRequestProperty("Content-Type", "application/json;");
            conn.connect();
            
            if (conn.getResponseCode() != 200) {
                if(conn.getResponseCode() == 401){
                    // get token from web server
                    String token = Network.getToken(new LoginViewModel(MaiinUI.loginUser.getEmail(), MaiinUI.loginUser.getPassword()));

                    // save token to file
                    SyncInfo.saveToken(token);
                }
                else {
                    System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
                }
                return null;
                // throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            String body = "";
            while ((output = br.readLine()) != null) {
                    body += output;
            }
            //JsonElement elem = new JsonParser().parse(body);
            //JsonObject obj = elem.getAsJsonObject();
            //body = obj.get("token").getAsString();

            conn.disconnect();
            return body;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } 
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
