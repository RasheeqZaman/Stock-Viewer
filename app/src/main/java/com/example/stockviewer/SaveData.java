package com.example.stockviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveData {
    private Context context;
    private Map<String, String> fileNames;
    private int maxSize;

    public SaveData(Context context){
        this.context = context;
        fileNames = new HashMap<>();
        fileNames.put("Bookmark", "Bookmark data");
        fileNames.put("Recent", "Recent data");
        maxSize = 10;
    }

    public void removeData(String fileName, String companyName){
        List<String> data = getData(fileName);
        if(!data.contains(companyName)) return;

        data.remove(companyName);

        if(data.size()<=0){
            saveInSharedPreferences(fileName, "");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(data.get(0));
        for(int i=1, n=data.size(); i<n; i++){
            sb.append(",");
            sb.append(data.get(i));
        }

        saveInSharedPreferences(fileName, sb.toString());
    }

    private void saveInSharedPreferences(String fileName, String str){
        SharedPreferences settings = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(fileNames.get(fileName), str);
        editor.apply();
    }

    public void setData(String fileName, String companyName){
        List<String> data = getData(fileName);
        //Log.d("alright", "setData6: "+data.toString());
        if(data.contains(companyName)) return;
        //Log.d("alright", "setData1: "+data.toString());

        data.add(companyName);

        //Log.d("alright", "setData5: "+data.toString());
        if(fileName.equals("Bookmark")){
            if(data.size()>maxSize){
                Log.d("alright", "setData: "+"Can't save more than "+maxSize+" bookmarks.");
                return;
            }
            Collections.sort(data);
        }else{
            if(data.size()>maxSize) {
                data.remove(0);
            }
        }

        //Log.d("alright", "setData2: "+data.toString());
        StringBuilder sb = new StringBuilder();
        sb.append(data.get(0));
        for(int i=1, n=data.size(); i<n; i++){
            sb.append(",");
            sb.append(data.get(i));
        }

        //Log.d("alright", "setData3: "+sb.toString());
        saveInSharedPreferences(fileName, sb.toString());
        //Log.d("alright", "setData4: "+getData(fileName).toString());
    }

    public List<String> getData(String fileName){
        SharedPreferences settings = context.getSharedPreferences(fileName, 0);
        String value = settings.getString(fileNames.get(fileName), "");

        if(value.isEmpty()) return new ArrayList<>();

        String[] valueArr = value.split(",");
        List<String> valueList = new ArrayList<>();
        for(String s : valueArr) {
            valueList.add(s);
        }
        return valueList;
    }
}
