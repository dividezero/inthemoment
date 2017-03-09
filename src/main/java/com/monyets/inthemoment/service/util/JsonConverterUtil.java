package com.monyets.inthemoment.service.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monyets.inthemoment.domain.Message;
import com.monyets.inthemoment.service.dto.RequestResult;

import java.io.IOException;

/**
 * Created by hazlan on 9/3/2017.
 */
public class JsonConverterUtil {

    public static Message getMessageFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Message.class);

        } catch (JsonGenerationException e) {
            System.out.println(e);
        } catch (JsonMappingException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        return null;
    }

    public static RequestResult getRequestResultFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, RequestResult.class);

        } catch (JsonGenerationException e) {
            System.out.println(e);
        } catch (JsonMappingException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        return null;
    }
}
