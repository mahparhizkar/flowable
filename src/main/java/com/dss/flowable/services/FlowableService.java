package com.dss.flowable.services;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service("flowableService")
public class FlowableService {

    public String getStudentByMeliCode(String result) {
        return execute(result, "http://localhost:8090/studentApi/studentByMeliCode/", "melicode");
    }

    public String getSchoolIdByStudentId(String result) {
        return execute(result, "http://localhost:8090/linkApi/link/", "id");
    }

    public String getSchoolById(String result) {

        return execute(result, "http://localhost:8090/schoolApi/school/", "id");
    }

    public Map<String, Object> convertInputObjectToHashMap (Object inputObject) {

        if (inputObject != null) {
            String object = inputObject.toString();
            object = object.replace("{","");
            object = object.replace("}","");
            object = object.replace("\"","");

            Map<String, Object> variables = new HashMap<>();
            String arr[] = object.split(",");
            for (int i = 0; i <arr.length ; i++) {
                if (arr[i].split(":").length > 1) {
                    String key = arr[i].split(":")[0];
                    String value = arr[i].split(":")[1];
                    variables.put(key, value);
                }
                else {
                    variables.put("isNotJson", arr[0].toString());
                }
            }
            return variables;
        }
        return null;
    }

    public  boolean isJson (Map<String, Object> variables) {
        if (variables.containsKey("isNotJson")) {
            return false;
        }
        else {
            return true;
        }
    }

    public String extractValueOfParam(Map<String, Object> variables, String name) {

        if (isJson(variables)) {
            return variables.get(name).toString();
        }
        else {
            return variables.get("isNotJson").toString();
        }
    }

    public String execute(String urlPath) {
        String body = null;
        try {
            URL url = new URL(urlPath);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            body = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public String execute(String resultObj, String urlPath, String variableName) {
        if (resultObj != null) {
            Map<String, Object> variables = convertInputObjectToHashMap(resultObj);
            String str = extractValueOfParam(variables, variableName);
            if (str != null) {
                return execute(urlPath + str);
            }
        }
        return null;
    }
}
