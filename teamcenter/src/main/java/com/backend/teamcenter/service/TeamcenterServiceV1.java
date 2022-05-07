package com.backend.teamcenter.service;

import com.backend.teamcenter.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TeamcenterServiceV1 implements TeamcenterService{

    private OkHttpClient httpClient;

    @Override
    public ResponseModel login(String username, String password) {
        var xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ses=\"http://teamcenter.com/Schemas/Core/2006-03/Session\">\"" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<ses:LoginInput username=\"" + username + "\" password=\"" + password + "\"/>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
        RequestBody formBody
                = RequestBody.create(xml, MediaType.parse("text/xml;charset=UTF-8"));

        Request request = new Request.Builder()
                .url("http://portal.kitp.vorstu.ru:8082/tc/services/Core-2006-03-Session")
                .addHeader("SOAPAction", "login")
                .post(formBody)
                .build();

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            List<String> Cookielist = response.headers().values("Set-Cookie");
            String jsessionid = (Cookielist.get(0).split(";"))[0];
            HttpHeaders headers = new HttpHeaders();
            headers.add("Session", jsessionid);
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
            headers.add("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
            headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
            headers.add("Content-Type", "text/plain;charset=cp1251");
            return new ResponseModel(headers,
                    new String(response.body().string().getBytes(StandardCharsets.UTF_8), "cp1251"));
        } catch (IOException e) {
            return new ResponseModel(null, "Что-то пошло не так!");
        }
    }

    @Override
    public ResponseModel getSavedQueries(String session) {
        var xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sav=\"http://teamcenter.com/Schemas/Query/2006-03/SavedQuery\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <sav:GetSavedQueriesInput/>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        RequestBody formBody
                = RequestBody.create(xml, MediaType.parse("text/xml;charset=UTF-8"));

        Request request = new Request.Builder()
                .url("http://portal.kitp.vorstu.ru:8082/tc/services/Query-2006-03-SavedQuery")
                .addHeader("SOAPAction", "getSavedQueries")
                .addHeader("Cookie", session)
                .post(formBody)
                .build();

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            List<String> Cookielist = response.headers().values("Set-Cookie");
            String jsessionid = (Cookielist.get(0).split(";"))[0];
            HttpHeaders headers = new HttpHeaders();
            headers.add("Session", jsessionid);
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
            headers.add("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
            headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
            headers.add("Content-Type", "text/plain;charset=cp1251");
            return new ResponseModel(headers,
                    new String(response.body().string().getBytes(StandardCharsets.UTF_8), "cp1251"));
        } catch (IOException e) {
            return new ResponseModel(null, "Что-то пошло не так!");
        }
    }
}
