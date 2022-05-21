package com.backend.teamcenter.service;

import com.backend.teamcenter.model.ObjectModel;
import com.backend.teamcenter.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public ResponseModel executeSavedQueries(String session, String id, String name) {
        ObjectModel objectModel = new ObjectModel();
        Map<String, String> objects = new HashMap<>();
        Document doc;
        Node node;
        String value;
        String entries;
        if (name == null || id != null) {
            value = id;
            entries = "Item ID";
        } else {
            value = name;
            entries = "Name";
        }
        var xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sav=\"http://teamcenter.com/Schemas/Query/2007-09/SavedQuery\" xmlns:base=\"http://teamcenter.com/Schemas/Soa/2007-09/Base\"> \n" +
                "   <soapenv:Header/> \n" +
                "   <soapenv:Body> \n" +
                "      <sav:ExecuteSavedQueriesInput> \n" +
                "         <sav:input maxNumToReturn=\"200\"> \n" +
                "            <sav:query uid=\"w0XNwAOn4x3p9D\"> \n" +
                "               <base:properties modifiable=\"0\" name=\"query_name\" uiValue=\"Item_Name_or_ID\"> \n" +
                "                  <base:values value=\"Item_Name_or_ID\"/> \n" +
                "                  <base:uiValues></base:uiValues> \n" +
                "               </base:properties> \n" +
                "               <base:uiproperties> \n" +
                "                  <base:uiValues></base:uiValues> \n" +
                "               </base:uiproperties> \n" +
                "               <base:props> \n" +
                "                  <base:value> \n" +
                "                     <base:dbValues></base:dbValues> \n" +
                "                     <base:uiValues></base:uiValues> \n" +
                "                     <base:isNulls></base:isNulls> \n" +
                "                  </base:value> \n" +
                "               </base:props> \n" +
                "            </sav:query> \n" +
                "            <sav:entries>" + entries + "</sav:entries> \n" +
                "            <sav:values>" + value + "</sav:values> \n" +
                "            <sav:limitList> \n" +
                "               <base:properties> \n" +
                "                  <base:values/> \n" +
                "                  <base:uiValues></base:uiValues> \n" +
                "               </base:properties> \n" +
                "               <base:uiproperties> \n" +
                "                  <base:uiValues></base:uiValues> \n" +
                "               </base:uiproperties> \n" +
                "               <base:props> \n" +
                "                  <base:value> \n" +
                "                     <base:dbValues></base:dbValues> \n" +
                "                     <base:uiValues></base:uiValues> \n" +
                "                     <base:isNulls></base:isNulls> \n" +
                "                  </base:value> \n" +
                "               </base:props> \n" +
                "            </sav:limitList> \n" +
                "         </sav:input> \n" +
                "      </sav:ExecuteSavedQueriesInput> \n" +
                "   </soapenv:Body> \n" +
                "</soapenv:Envelope>";
        RequestBody formBody
                = RequestBody.create(xml, MediaType.parse("text/xml;charset=UTF-8"));

        Request request = new Request.Builder()
                .url("http://portal.kitp.vorstu.ru:8082/tc/services/Query-2007-09-SavedQuery")
                .addHeader("SOAPAction", "executeSavedQueries")
                .addHeader("Cookie", session)
                .post(formBody)
                .build();

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        try (Response responseQueries = httpClient.newCall(request).execute()) {

            doc = convertStringToDoc(new String(responseQueries.body().string().getBytes(StandardCharsets.UTF_8), "cp1251"));
            node = doc.getElementsByTagName("ns0:arrayOfResults").item(0);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                for (int i = 0; i < element.getElementsByTagName("ns0:objectUIDS").getLength(); i++) {
                    String loadObject = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://teamcenter.com/Schemas/Core/2007-09/DataManagement\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <dat:LoadObjectsInput>\n" +
                            "         <dat:uids>" + element.getElementsByTagName("ns0:objectUIDS").item(i).getTextContent() + "</dat:uids>\n" +
                            "      </dat:LoadObjectsInput>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
                    formBody
                            = RequestBody.create(loadObject, MediaType.parse("text/xml;charset=UTF-8"));
                    request = new Request.Builder()
                            .url("http://portal.kitp.vorstu.ru:8082/tc/services/Core-2007-09-DataManagement")
                            .addHeader("SOAPAction", "loadObjects")
                            .addHeader("Cookie", session)
                            .post(formBody)
                            .build();
                    httpClient = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Response responseObject = httpClient.newCall(request).execute();
                    doc = convertStringToDoc(new String(responseObject.body().string().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                    node = doc.getElementsByTagName("ns0:dataObjects").item(0);
                    Element newElement = (Element) node;
                    Element desc = (Element) newElement.getElementsByTagName("ns0:properties").item(2);
                    Element group = (Element) newElement.getElementsByTagName("ns0:properties").item(9);
                    Element idObject = (Element) newElement.getElementsByTagName("ns0:properties").item(7);
                    Element lastDate = (Element) newElement.getElementsByTagName("ns0:properties").item(6);
                    Element user = (Element) newElement.getElementsByTagName("ns0:properties").item(8);
                    String object = "Описание - " + desc.getAttribute("uiValue") + "\n" +
                            "Группа - " + group.getAttribute("uiValue") + "\n" +
                            "Последняя дата мод. - " + lastDate.getAttribute("uiValue") + "\n" +
                            "Пользователь - " + user.getAttribute("uiValue") + "\n";

                    objects.put(idObject.getAttribute("uiValue"), new String(object.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                }
            }
            objectModel.addAllObject(objects);
            HttpHeaders headers = new HttpHeaders();
            //headers.add("Content-Type", "text/plain;charset=cp1251");
            return new ResponseModel(headers, objectModel);
        } catch (IOException e) {
            return new ResponseModel(null, "Что-то пошло не так!");
        }
    }

    private Document convertStringToDoc(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
