package edu.uw.fragmentdemo


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : DialogFragment() {

    companion object {

        val MOVIE_PARCEL_KEY = "movie_parcel"

        fun newInstance(movie: Movie): DetailFragment {

            val args = Bundle().apply {
                putParcelable(MOVIE_PARCEL_KEY, movie)
            }
            val fragment = DetailFragment().apply {
                arguments = args
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.movie_detail, container, false)

        arguments?.let {
            val movie = it.getParcelable<Movie>(MOVIE_PARCEL_KEY)
            movie?.let {
                val titleView = rootView.findViewById<TextView>(R.id.txt_movie_title)
                titleView.text = it.toString()

                val urlView = rootView.findViewById<TextView>(R.id.txt_movie_url)
                urlView.text = it.url

                val descView = rootView.findViewById<TextView>(R.id.txt_movie_description)
                descView.text = it.description
            }
        }

        return rootView
    }

}// Required empty public constructor
