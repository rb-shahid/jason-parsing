package com.byteshaft.jsonparsing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.byteshaft.jsonparsing.models.MovieModel;

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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HttpURLConnection connection = null;
    BufferedReader reader = null;
    ProgressBar progressBar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            String url = "http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt";
            String moviesUrl = "http://jsonparsing.parseapp.com/jsonData/moviesData.txt";
            new MyTask().execute(moviesUrl);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyTask extends AsyncTask<String, String, List<MovieModel>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieModel> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null ) {
                    buffer.append(line);
                }

                StringBuffer stringBuffer = new StringBuffer();

                String finalString = buffer.toString();
                JSONObject jsonObject = new JSONObject(finalString);
                JSONArray jsonArray = jsonObject.getJSONArray("movies");

                List<MovieModel> movieModelList = new ArrayList<>();

                for (int i = 0; i<jsonArray.length() ; i++) {
                    JSONObject jsonObjectFinal = jsonArray.getJSONObject(i);
                    MovieModel movieModel = new MovieModel();
                    movieModel.setMovie(jsonObjectFinal.getString("movie"));
                    movieModel.setYear(jsonObjectFinal.getInt("year"));
                    movieModel.setRating((float) jsonObjectFinal.getDouble("rating"));
                    movieModel.setDirector(jsonObjectFinal.getString("director"));
                    movieModel.setDuration(jsonObjectFinal.getString("duration"));
                    movieModel.setTagline(jsonObjectFinal.getString("tagline"));
                    movieModel.setImage(jsonObjectFinal.getString("image"));
                    movieModel.setStory(jsonObjectFinal.getString("story"));

                    List<MovieModel.Cast> castList = new ArrayList<>();
                    for (int j = 0; j < jsonObjectFinal.getJSONArray("cast").length(); j++) {
                        MovieModel.Cast cast = new MovieModel.Cast();
                        castList.add(cast);
                    }
                    movieModel.setCastList(castList);
                    movieModelList.add(movieModel);

                    movieModel.getMovie();
                }
                return movieModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieModel> result) {
            super.onPostExecute(result);
//            System.out.println(result == null);
            MovieAdaptor adaptor = new MovieAdaptor(getApplicationContext(), R.layout.row, result);
//            System.out.println(adaptor == null);
//            System.out.println(listView == null);
            listView.setAdapter(adaptor);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
