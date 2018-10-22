package edu.uw.fragmentdemo

/**
 * A class that represents information about a movie.
 */
class Movie(
        var title: String,
        var year: String,
        var description: String,
        var url: String
) {
    init {
        year = year.substring(0, 4) //handle year from string
    }

    override fun toString(): String {
        return this.title + " (" + this.year + ")"
    }
}