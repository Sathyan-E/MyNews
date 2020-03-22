package com.example.mynews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    public  JsonHelper()
    {
    }

    public static List<News> extractJsonDetails(String string)
    {
     String jsonResponse="";
     //creating URl form string
     URL url =createUrl(string);
     try {
        //calling makeHttpRequest to send request and get a respose form the server
         jsonResponse=makeHttpRequest(url);
     } catch (IOException e) {
         e.printStackTrace();
     }
     //calling a method to parse the Json response string got form th server
     List<News> newsList = getTitleFromJson(jsonResponse);
     return newsList;
    }
    //method for creating a url from string
    private static URL createUrl(String stringUrl)
    {
        URL url =null;
        try {
            url= new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    //method for making a request to the server and to get Json Response from server
    private static String  makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse="";
        if (url==null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try {
           //opening urlconnection to the proposed url and setting attributes to that cnnection
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode()==200)
            {
               //getting input stream from server
                inputStream=urlConnection.getInputStream();
               //conver that input stream to a string
                jsonResponse=readFromJson(inputStream);
            }
            else {
                Log.e("Error",""+urlConnection.getResponseCode());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
            {
               //closing connection
                urlConnection.disconnect();
            }
            if (inputStream!=null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromJson(InputStream inputStream) throws IOException
    {
        //stringBuilder instance to store the parsed response
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream!=null)
        {
            //to convert into byte
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            //read that byte code and parse to readable
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line =bufferedReader.readLine();
            while (line!=null)
            {
                stringBuilder.append(line);
                line=bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static List<News> getTitleFromJson(String jsonString)
    {
        if (TextUtils.isEmpty(jsonString)){
            return null;
        }
        List<News> list = new ArrayList<News>();
        try {
            //traversing through json response
            JSONObject baseJson = new JSONObject(jsonString);
            JSONObject responseObject =  baseJson.getJSONObject("response");
            JSONArray jsonArray = responseObject.getJSONArray("results");

            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject resultObject = jsonArray.getJSONObject(i);
                //getting the title from the current json object
                String webTitle = resultObject.getString("webTitle");
                //getting url of current json object
                String webLink = resultObject.getString("webUrl");
                //getting section name of current json object
                String sectionName = resultObject.getString("sectionName");
                Log.e("title"+webTitle,"url"+webLink);
                //creating a News object from above variables and adding to a list
                list.add(new News(webTitle,webLink,sectionName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
