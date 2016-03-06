package com.example.alex.scheduleandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.example.alex.scheduleandroid.dto.GroupDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by alex on 02.03.16.
 */
public class ConnectedManager {

    public static final String GROUP_URL = "http://10.0.2.2/schedule/APIController/get_all_groups.php?faculty=";
    public static final String LESSON_URL = "http://127.0.0.1/schedule/APIController/get_all_lessons_by_grp.php";
    public static final String FACULTY_DKE = "1";
    public static final String FACULTY_DEE = "2";
    public static final String FACULTY_DPM = "3";
    public static final String MY_TAG = "myTag";

    DownloadPageTask MyTask;
    private ProgressDialog pDialog;
    private Context context;

    private String[] faculties;


    public ConnectedManager(Context context) {
        this.context = context;

        this.faculties = context.getResources().getStringArray(R.array.name_array_faculties);
    }

    public GroupDTO getGroupDTOByFaculty(String faculty) {
        GroupDTO grpDTO = null;

        // этот класс отвечает за проверку подключения
        ConnectivityManager myConnMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = myConnMgr.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected()) {
            //запускаем новый поток для подачи запроса на сервер
            MyTask =  new DownloadPageTask();
            MyTask.execute(GROUP_URL + faculty);

            Log.d(MY_TAG , "before downloading");
            String jsonStringGroups = null; //JSON который длжен вернуть сервер
            try {
                jsonStringGroups = MyTask.get();// получаем значения из AsyncTask
                pDialog.dismiss();// отменяем прогресс бар
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.d(MY_TAG , "after downloading");

            //парсим полученный JSON
            grpDTO = this.parseRespondJSONGroups(jsonStringGroups, faculty);
        } else {
            Toast.makeText(context, "Нет интернета", Toast.LENGTH_SHORT).show();
        }

        return grpDTO;

    }

    private String downloadOneUrl(String myurl) throws IOException {
        InputStream inputstream = null;
        String data = "";
        try {
            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();// открываем соединение
            connection.setRequestMethod("GET");

//            connection.setRequestProperty("faculty", "1");

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
            //return data;
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

    private GroupDTO parseRespondJSONGroups (String jsonString , String title) {
        // метод для парсинга JSON

        if(jsonString.equals("error")) {
            return null;
        }

        GroupDTO  grpDTO = new GroupDTO(this.getStringFaculty(title));
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

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String strMsg = context.getString(R.string.downloadingGroups);

            pDialog = new ProgressDialog(context);
            pDialog.setMessage(strMsg);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadOneUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private String getStringFaculty(String numFaculty) {
        String strFaculty = null;
        switch (numFaculty) {
            case "1":
                strFaculty = this.faculties[0];
                break;
            case "2":
                strFaculty = this.faculties[1];
                break;
            case "3":
                strFaculty = this.faculties[2];
                break;
        }

        return strFaculty;
    }


}
