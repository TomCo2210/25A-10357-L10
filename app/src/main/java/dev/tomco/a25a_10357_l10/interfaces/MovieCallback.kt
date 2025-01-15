package dev.tomco.a25a_10357_l10.interfaces

import dev.tomco.a25a_10357_l10.models.Movie

interface MovieCallback {
    fun favoriteButtonClicked(movie: Movie, position: Int)
}