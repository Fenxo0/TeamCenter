package com.backend.teamcenter.service;

import com.backend.teamcenter.model.ObjectModel;
import com.backend.teamcenter.model.ResponseModel;
import com.backend.teamcenter.model.UserTC;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2007_01.DataManagement;
import com.teamcenter.soa.client.model.strong.*;
import com.teamcenter.soa.internal.client.FCCWrapper;
import com.teamcenter.soa.internal.client.FMSWrapper;
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
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TeamcenterServiceV1 implements TeamcenterService {

    private OkHttpClient httpClient;

    @Override
    public ResponseModel login(String username, String password) {
        Document doc;
        Node node;
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

            doc = convertStringToDoc(new String(response.body().string().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            node = doc.getElementsByTagName("ns1:dataObjects").item(1);
            Element newElement = (Element) node;
            String name = null;
            for (int j = 0; j < newElement.getElementsByTagName("ns1:properties").getLength(); j++) {
                Element objectElement = (Element) newElement.getElementsByTagName("ns1:properties").item(j);
                if (objectElement.getAttribute("name").equals("user")) {
                    name = objectElement.getAttribute("uiValue");
                }
            }
            node = doc.getElementsByTagName("ns1:dataObjects").item(3);
            newElement = (Element) node;
            UserTC user = new UserTC();
            for (int j = 0; j < newElement.getElementsByTagName("ns1:properties").getLength(); j++) {
                Element objectElement = (Element) newElement.getElementsByTagName("ns1:properties").item(j);
                switch (objectElement.getAttribute("name")) {
                    case "person":
                        user.setName(objectElement.getAttribute("uiValue"));
                        break;
                    case "license_level":
                        user.setLicenseLevel(objectElement.getAttribute("uiValue"));
                        break;
                    case "default_group":
                        user.setGroup(objectElement.getAttribute("uiValue"));
                        break;
                    case "home_folder":
                        user.setHomeFolder(objectElement.getAttribute("uiValue"));
                        break;
                    case "user_projects":
                        user.setProject(objectElement.getAttribute("uiValue"));
                        break;
                    case "user_id":
                        user.setId(objectElement.getAttribute("uiValue"));
                        break;
                }
            }

            List<String> Cookielist = response.headers().values("Set-Cookie");
            String jsessionid = (Cookielist.get(0).split(";"))[0];
            HttpHeaders headers = new HttpHeaders();
            headers.add("Session", jsessionid);
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
            headers.add("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
            headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");

            return new ResponseModel(headers, user);
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
        List<String> users = new ArrayList<>();
        List<String> groups = new ArrayList<>();
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
                    String desc = null, group = null, idObject = "", lastDate = null, user = null;
                    for (int j = 0; j < newElement.getElementsByTagName("ns0:properties").getLength(); j++) {
                        Element objectElement = (Element) newElement.getElementsByTagName("ns0:properties").item(j);
                        switch (objectElement.getAttribute("name")) {
                            case "object_desc":
                                desc = objectElement.getAttribute("uiValue");
                                break;
                            case "owning_group":
                                group = objectElement.getAttribute("uiValue");
                                Element groupElement = (Element) newElement.getElementsByTagName("ns0:values").item(j);
                                String uuidGroup = groupElement.getAttribute("value");
                                if (!uuidGroup.equals("SPB5_Doc") && !uuidGroup.equals("P181_СР-P181 Спецификация распределения")) {
                                    groups.addAll(gettingGroup(groupElement.getAttribute("value"), session));
                                } else {
                                    group = null;
                                }
                                break;
                            case "object_string":
                                idObject = objectElement.getAttribute("uiValue");
                                break;
                            case "last_mod_date":
                                lastDate = objectElement.getAttribute("uiValue");
                                break;
                            case "owning_user":
                                user = objectElement.getAttribute("uiValue");
                                Element userElement = (Element) newElement.getElementsByTagName("ns0:values").item(j);
                                String uuidUser = userElement.getAttribute("value");
                                if (!uuidUser.equals("SPB5_Doc") && !uuidUser.equals("P181_СР-P181 Спецификация распределения")) {
                                    users.addAll(gettingUser(uuidUser, session));
                                } else {
                                    user = null;
                                }
                                break;
                        }
                    }
                    String object = "Описание - " + desc + ";" +
                            "Группа - " + group + ";" +
                            "Последняя дата мод. - " + lastDate + ";" +
                            "Пользователь - " + user + ";";

                    objects.put(idObject, new String(object.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                }
            }
            objectModel.addAllObject(objects);
            objectModel.addAllUsers(users);
            objectModel.addAllGroups(groups);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseModel(headers, objectModel);
        } catch (IOException e) {
            return new ResponseModel(null, "Что-то пошло не так!");
        }
    }

    private Document convertStringToDoc(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> gettingUser(String uidUser, String session) throws IOException {
        List<String> users = new ArrayList<>();
        String loadUser = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://teamcenter.com/Schemas/Core/2007-09/DataManagement\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <dat:LoadObjectsInput>\n" +
                "         <dat:uids>" + uidUser + "</dat:uids>\n" +
                "      </dat:LoadObjectsInput>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        RequestBody formBody
                = RequestBody.create(loadUser, MediaType.parse("text/xml;charset=UTF-8"));
        Request request = new Request.Builder()
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

        Document doc = convertStringToDoc(new String(responseObject.body().string().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        Node node = doc.getElementsByTagName("ns0:dataObjects").item(0);
        Element newElement = (Element) node;
        String username = null, userId = null, group = "", userProjects = null, person = null;
        System.out.println(uidUser);
        if (newElement != null) {
            for (int j = 0; j < newElement.getElementsByTagName("ns0:properties").getLength(); j++) {
                Element objectElement = (Element) newElement.getElementsByTagName("ns0:properties").item(j);
                switch (objectElement.getAttribute("name")) {
                    case "user_name":
                        username = objectElement.getAttribute("uiValue");
                        break;
                    case "user_id":
                        userId = objectElement.getAttribute("uiValue");
                        break;
                    case "default_group":
                        group = objectElement.getAttribute("uiValue");
                        break;
                    case "user_projects":
                        userProjects = objectElement.getAttribute("uiValue");
                        break;
                    case "person":
                        person = objectElement.getAttribute("uiValue");
                        break;
                }
            }
            String object = "Группа по умолчанию - " + group + ";" +
                    "Группа регистрации - " + group + ";" +
                    "Идентификатор - " + userId + ";" +
                    "Имя - " + person + ";" +
                    "Имя в ОС - " + userId + ";" +
                    "Объект - " + username + " (" + userId + ")" + ";" +
                    "Персона - " + person + ";" +
                    "Идентификатор - " + userId + ";";

            users.add(object);
        }
        return users;
    }

    private List<String> gettingGroup(String uidGroup, String session) throws IOException {
        List<String> groups = new ArrayList<>();
        String loadUser = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://teamcenter.com/Schemas/Core/2007-09/DataManagement\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <dat:LoadObjectsInput>\n" +
                "         <dat:uids>" + uidGroup + "</dat:uids>\n" +
                "      </dat:LoadObjectsInput>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        RequestBody formBody
                = RequestBody.create(loadUser, MediaType.parse("text/xml;charset=UTF-8"));
        Request request = new Request.Builder()
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

        Document doc = convertStringToDoc(new String(responseObject.body().string().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        Node node = doc.getElementsByTagName("ns0:dataObjects").item(0);
        Element newElement = (Element) node;
        String fullName = null, name = null, description = "", userProjects = null, person = null;
        System.out.println(uidGroup);
        if (newElement != null) {
            for (int j = 0; j < newElement.getElementsByTagName("ns0:properties").getLength(); j++) {
                Element objectElement = (Element) newElement.getElementsByTagName("ns0:properties").item(j);
                switch (objectElement.getAttribute("name")) {
                    case "object_full_name":
                        fullName = objectElement.getAttribute("uiValue");
                        break;
                    case "name":
                        name = objectElement.getAttribute("uiValue");
                        break;
                    case "description":
                        description = objectElement.getAttribute("uiValue");
                        break;
                }
            }
            String object = "Имя - " + name + ";" +
                    "Описание - " + description + ";" +
                    "Полное имя  - " + fullName + ";";

            groups.add(object);
        }
        return groups;
    }

    public Dataset[] getDatasets(String itemId, String revisionId) throws Exception {
        DataManagement.GetItemFromIdInfo idInfo = new DataManagement.GetItemFromIdInfo();
        DataManagement.GetItemFromIdPref idPref = new DataManagement.GetItemFromIdPref();
        DataManagement.RelationFilter filter = new DataManagement.RelationFilter();

        idInfo.itemId = itemId;
        idInfo.revIds = new String[]{revisionId};

        filter.relationTypeName = "IMAN_Specification";
        FMSWrapper fmsWrapper = new FCCWrapper();
        String[] var = fmsWrapper.RegisterTickets(new String[] {"3c8c703aa168e854a9a917434ba0c705393fe7a3ff40a0a680917a5f7d493344v1004T0000000000001718553450b987c77adf2022%2f06%2f20+02%3a54%3a12-2016970017+++++++++++++++++++++suhochev++++++++++++++++++++++++00e4530309bd87c77adf++++++++++++%5cdba_5302df93%5cdemo_96_pdf_fp303cs5wq5jo.pdf"});
        String[] downLoadFilesFromPLM = fmsWrapper.DownLoadFilesFromPLM("IMD", null, null, var);
        File[] files = new File[downLoadFilesFromPLM.length];
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(downLoadFilesFromPLM[i]);
        }
        filter.objectTypeNames = new String[]{"PDF"};

        idPref.prefs = new DataManagement.RelationFilter[]{filter};
        TCSession tcSession = new TCSession();

        DataManagement.GetItemFromIdResponse response = DataManagementService.getService(tcSession.getSoaConnection()).getItemFromId(new DataManagement.GetItemFromIdInfo[]{idInfo}, 0, idPref);
        return response.output[0].itemRevOutput[0].datasets;
    }
}
