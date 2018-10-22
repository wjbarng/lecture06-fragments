package edu.uw.fragmentdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var adapter: ArrayAdapter<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArrayAdapter(this, R.layout.list_item, R.id.txt_item, ArrayList())

        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val movie = parent.getItemAtPosition(position) as Movie
            Log.v(TAG, "You clicked on: $movie")
        }
    }

    //respond to search button clicking
    fun handleSearchClick(v: View) {
        val searchEdit = findViewById<EditText>(R.id.txt_search)
        val searchTerm = searchEdit.text.toString()

        downloadMovieData(searchTerm)
    }


    //download media information from iTunes
    private fun downloadMovieData(searchTerm: String) {
        var urlString = ""
        try {
            urlString = "https://itunes.apple.com/search?term=" + URLEncoder.encode(searchTerm, "UTF-8") + "&media=movie&entity=movie&limit=25"
            //Log.v(TAG, urlString);
        } catch (uee: UnsupportedEncodingException) {
            Log.e(TAG, uee.toString())
            return
        }

        val request = JsonObjectRequest(Request.Method.GET, urlString, null,
                Response.Listener { response ->
                    val movies = ArrayList<Movie>()

                    try {
                        //parse the JSON results
                        val results = response.getJSONArray("results") //get array from "search" key
                        for (i in 0 until results.length()) {
                            val track = results.getJSONObject(i)
                            if (track.getString("wrapperType") != "track")
                            //skip non-track results
                                continue
                            val title = track.getString("trackName")
                            val year = track.getString("releaseDate")
                            val description = track.getString("longDescription")
                            val url = track.getString("trackViewUrl")
                            val movie = Movie(title, year, description, url)
                            movies.add(movie)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    adapter.clear()
                    for (movie in movies) {
                        adapter.add(movie)
                    }
                }, Response.ErrorListener { error -> Log.e(TAG, error.toString()) })

        VolleyService.getInstance(this).add(request)
    }
}
