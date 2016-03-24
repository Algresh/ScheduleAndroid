package com.example.alex.scheduleandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


import com.example.alex.scheduleandroid.dto.Group;
import com.example.alex.scheduleandroid.dto.FacultyDTO;
import com.example.alex.scheduleandroid.dto.Lesson;
import com.example.alex.scheduleandroid.dto.WorkDayDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ConnectedManager {

    private Context context;

    private String[] faculties;


    public ConnectedManager(Context context) {
        this.context = context;

        this.faculties = context.getResources().getStringArray(R.array.name_array_faculties);
    }

    public int postNotification(String message, String group) {

        if (this.checkConnection()) {
            return postMessage(Constants.POST_NOTIFICATION_URL, message, group);
        }

        return -1;
    }

    public FacultyDTO getGroupDTOByFaculty(String faculty) {
        FacultyDTO grpDTO = null;


        if (this.checkConnection()) {
            String jsonStringGroups = null; //JSON который длжен вернуть сервер

            try {
                jsonStringGroups = downloadOneUrl(Constants.GROUP_URL + faculty);// получаем значения из AsyncTask
            } catch (IOException e) {
                e.printStackTrace();
            }

            //парсим полученный JSON
            grpDTO = this.parseRespondJSONGroups(jsonStringGroups, faculty);
        } else {
            Toast.makeText(context, "Нет интернета", Toast.LENGTH_SHORT).show();
        }

        return grpDTO;

    }

    public WorkDayDTO getWorkDTOByGroup(String group ,List date) {
        WorkDayDTO workDayDTO = null;

        if (this.checkConnection()) {

            String jsonStringLessons = null;
            try {
                jsonStringLessons = downloadOneUrl(Constants.LESSON_URL + group);
            } catch (IOException e) {
                e.printStackTrace();
            }

            workDayDTO = this.parseRespondJSONLessons(jsonStringLessons, date);
        } else {
            Toast.makeText(context, "Нет интернета", Toast.LENGTH_SHORT).show();
        }

        return workDayDTO;
    }

    public int getVersionGroup(String group) {
        int version = -1;

        if (this.checkConnection()) {

            String jsonStringVersion = null;

            try {
                jsonStringVersion = downloadOneUrl(Constants.VERSION_GROUP_URL + group);
            } catch (IOException e) {
                e.printStackTrace();
            }

            version = this.parseRespondJSONVersionGroup(jsonStringVersion);
        }

        return version;
    }

    private String downloadOneUrl(String myurl) throws IOException {
        InputStream inputstream = null;
        String data = "";
        try {
            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();// открываем соединение
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputstream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int read = 0;
                while ((read = inputstream.read()) != -1) { // получаем ответ через поток
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();

                data = new String(result);

            } else {
                data = connection.getResponseMessage() + " . Error Code : " + responseCode;
            }
            connection.disconnect();// закрываем соединение
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputstream != null) {
                inputstream.close();
            }
        }
        return data;
    }

    private int postMessage(String myUrl, String message, String grp) {
        int responseCode = -1;
        try {
            URL url = new URL(myUrl);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            String urlParameters = "message=" + message + "&group=" + grp;
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
            responseCode = connection.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return responseCode;
        }
    }

    private int parseRespondJSONVersionGroup (String jsonString) {

        if(jsonString.equals("error")) {
            return -1;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            int success = jsonObject.getInt("success");

            if (success == 1) {
                return jsonObject.getInt("version");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private FacultyDTO parseRespondJSONGroups (String jsonString , String title) {
        // метод для парсинга JSON

        if(jsonString.equals("error")) {
            return null;
        }

        FacultyDTO grpDTO = new FacultyDTO(this.getStringFaculty(title));
        int idGrp;
        String namGrp;
        int versionGrp;
        int numMessages;
        int course;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            int success = jsonObject.getInt("success");

            if(success == 1){
                JSONArray jsonGroups = jsonObject.getJSONArray("groups");

                for (int i = 0; i < jsonGroups.length(); i++) {

                    JSONObject arrayElement = jsonGroups.getJSONObject(i);
                    idGrp = arrayElement.getInt("id");
                    namGrp = arrayElement.getString("nam_grp");
                    versionGrp = arrayElement.getInt("version_grp");
                    numMessages = arrayElement.getInt("num_message");
                    course = arrayElement.getInt("course");

                    grpDTO.setGroup(new Group(namGrp , versionGrp , idGrp , numMessages , course));
                }
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return grpDTO;
    }

    private WorkDayDTO parseRespondJSONLessons (String jsonString , List dateOfLesson) {
        if(jsonString.equals("error")) {
            return null;
        }

        WorkDayDTO workDayDTO = new WorkDayDTO(dateOfLesson);
        workDayDTO.setDateOfWorkDay(dateOfLesson);

        int idLesson;
        String teacher;
        int subGrp;
        String classRoom;
        String place;
        int numberLesson;
        String typeLesson;
        String nameSubject;
        String[] date = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            int success = jsonObject.getInt("success");


            if(success == 1) {
                JSONArray jsonLessons = jsonObject.getJSONArray("lesson");

                for (int i = 0; i< jsonLessons.length(); i++) {

                    JSONObject arrayElement = jsonLessons.getJSONObject(i);

                    idLesson  = arrayElement.getInt("id");
                    teacher  = arrayElement.getString("teacher");
                    nameSubject  = arrayElement.getString("namSubj");
                    subGrp  = arrayElement.getInt("subGrp");
                    classRoom  = arrayElement.getString("classRoom");
                    place  = arrayElement.getString("place");
                    numberLesson  = arrayElement.getInt("numberLesson");
                    typeLesson  = arrayElement.getString("typeLesson");

                    date = null;

                    try{
                        JSONArray jsonDateLesson = arrayElement.getJSONArray("dateLesson");

                        date = new String[jsonDateLesson.length()];

                        for (int j = 0; j < jsonDateLesson.length(); j++) {
                            JSONObject arrayDateElement = jsonDateLesson.getJSONObject(j);
                            date[j] = arrayDateElement.getString("lesson_date");


                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


                    workDayDTO.setLesson(new Lesson(nameSubject , numberLesson , classRoom , typeLesson , teacher ,subGrp ,place ,date));
                }

            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  workDayDTO;

    }


    public String getStringFaculty(String numFaculty) {
        String strFaculty = null;
        switch (numFaculty) {
            case Constants.FACULTY_DKE:
                strFaculty = this.faculties[0];
                break;
            case Constants.FACULTY_DEE:
                strFaculty = this.faculties[1];
                break;
            case Constants.FACULTY_DPM:
                strFaculty = this.faculties[2];
                break;
        }

        return strFaculty;
    }

    public boolean checkConnection () {
        // этот класс отвечает за проверку подключения
        ConnectivityManager myConnMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = myConnMgr.getActiveNetworkInfo();

        return networkinfo != null && networkinfo.isConnected();
    }


}
