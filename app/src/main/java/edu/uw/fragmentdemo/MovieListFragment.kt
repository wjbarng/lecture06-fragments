package edu.uw.fragmentdemo


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MovieListFragment : Fragment() {

    private lateinit var adapter: ArrayAdapter<Movie>
    private var callback: OnMovieSelectedListener? = null

    //an interface for those who can respond to interactions with this Fragment
    internal interface OnMovieSelectedListener {
        fun onMovieSelected(movie: Movie)
    }

    companion object {
        private val TAG = "MovieListFragment"
        private val SEARCH_PARAM_KEY = "search_term"

        fun newInstance(searchTerm: String): MovieListFragment {
            val args = Bundle().apply {
                putString(SEARCH_PARAM_KEY, searchTerm)
            }
            val fragment = MovieListFragment().apply {
                arguments = args
            }
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        callback = context as? OnMovieSelectedListener
        if(callback == null){
            throw ClassCastException("$context must implement OnMovieSelectedListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie_list, container, false)

        adapter = ArrayAdapter(activity!!,
                R.layout.list_item, R.id.txt_item, ArrayList())

        val listView = rootView.findViewById<View>(R.id.list_view) as ListView
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val movie = parent.getItemAtPosition(position) as Movie
            Log.v(TAG, "You clicked on: $movie")
            callback!!.onMovieSelected(movie)
        }

        arguments?.let {
            val searchTerm = it.getString(SEARCH_PARAM_KEY)
            if (searchTerm != null)
                downloadMovieData(searchTerm)
        }

        return rootView
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

        VolleyService.getInstance(activity!!).add(request)
    }
}