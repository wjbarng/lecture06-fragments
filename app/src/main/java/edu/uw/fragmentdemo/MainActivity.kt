package edu.uw.fragmentdemo

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText



class MainActivity : AppCompatActivity(), MovieListFragment.OnMovieSelectedListener {

    private val TAG = "MainActivity"
    private val MOVIE_LIST_FRAGMENT_TAG = "MoviesListFragment"
    private val MOVIE_DETAIL_FRAGMENT_TAG = "DetailFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //respond to search button clicking
    fun handleSearchClick(v: View) {
        val searchEdit = findViewById<EditText>(R.id.txt_search)
        val searchTerm = searchEdit.text.toString()

        val fragment = MovieListFragment.newInstance(searchTerm)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment, MOVIE_LIST_FRAGMENT_TAG)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onMovieSelected(movie: Movie) {
        val fragment = DetailFragment.newInstance(movie)

        supportFragmentManager.beginTransaction().run {
            replace(R.id.container, fragment, MOVIE_DETAIL_FRAGMENT_TAG)
            addToBackStack(null) //remember for the back button
            commit()
        }

        //showHelloDialog();
        //HelloDialogFragment.newInstance().show(supportFragmentManager, null)
        //DetailFragment.newInstance(movie).show(supportFragmentManager, null)
    }

    fun showHelloDialog() {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Alert!")
            setMessage("Danger Will Robinson!")
            setPositiveButton("I see it!") { dialog, id -> Log.v(TAG, "You clicked okay! Good times :)") }
            setNegativeButton("Noooo...") { dialog, which -> Log.v(TAG, "You clicked cancel! Sad times :(") }
        }

        val dialog = builder.create()
        dialog.show()
    }

    class HelloDialogFragment : DialogFragment() {

        private val TAG = "HelloDialogFragment"

        companion object {
            fun newInstance(): HelloDialogFragment {
                val args = Bundle()
                val fragment = HelloDialogFragment()
                fragment.arguments = args
                return fragment
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Alert!")
                        .setMessage("Danger Will Robinson!") //note chaining
                builder.setPositiveButton("I see it!") { dialog, id -> Log.v(TAG, "You clicked okay! Good times :)") }
                builder.setNegativeButton("Noooo...") { dialog, which -> Log.v(TAG, "You clicked cancel! Sad times :(") }

                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}
