package com.monyets.inthemoment.service;

import com.monyets.inthemoment.config.Constants;
import com.monyets.inthemoment.domain.Message;
import com.monyets.inthemoment.domain.Setting;
import com.monyets.inthemoment.repository.SettingRepository;
import com.monyets.inthemoment.service.dto.RequestResult;
import com.monyets.inthemoment.service.util.JsonConverterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hazlan on 9/3/2017.
 */
public class SlackApiService {

    @Autowired
    private SettingRepository settingRepository;


    public List<Message> deleteMessagesBeforeDateByCHannelId(Date date, String channelId){

        String token = Constants.DEFAULT_USER_TOKEN;
        List<Setting> tokens = settingRepository.findByName(Constants.SETTING_TOKEN);
        if(tokens!=null && tokens.size()>0){
                token = tokens. get(0).getValue();
        }

        List<Message> messages = getMessagesByChannelId(token, channelId);
        List<Message> deletedMsg = new ArrayList<>();

        long time = date.getTime();

        for(Message message: messages){
            String[] ts= StringUtils.split(message.getTs(),".");

            System.out.println("timestamp: "+ts.toString());

            if(time > Long.parseLong(ts[0])){
                //todo delete

                deleteMessagesByChannelIdAndTimestamp(token, channelId, message.getTs());
                deletedMsg.add(message);
            }
        }
        return deletedMsg;
    }

    public static List<Message> getMessagesByChannelId(String token, String channelId){
        List<Message> result = new ArrayList<>();

        try {

            String channel = channelId;
            URL url = new URL("https://slack.com/api/channels.history?token=" + token + "&channel=" + channel);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            String output, full = "";
//            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
//                System.out.println("NEWLINE"+output);
                full += output;
            }

            conn.disconnect();


            RequestResult requestResult = JsonConverterUtil.getRequestResultFromJson(full);

            if(requestResult != null){
                result = requestResult.getMessages();
            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return result;
    }


    public static List<Message> deleteMessagesByChannelIdAndTimestamp(String token, String channelId, String timestamp){
        List<Message> result = new ArrayList<>();

        try {

            String channel = channelId;
            URL url = new URL("https://slack.com/api/chat.delete?token="+token+"&channel="+channel+"&ts="+timestamp);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            String output, full = "";
//            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
//                System.out.println("NEWLINE"+output);
                full += output;
            }
            System.out.println("Deleted: "+full);

            conn.disconnect();



        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return result;
    }
}
