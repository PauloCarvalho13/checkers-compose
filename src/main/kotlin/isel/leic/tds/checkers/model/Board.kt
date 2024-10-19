package isel.leic.tds.checkers.model

data class Board(val playingPlaces: Map<Square,Piece?>) {
    fun updateBoard(from: Square, to: Square, piece: Piece): Board {
        val newPlayingPlaces = playingPlaces.toMutableMap()

        newPlayingPlaces[from] = null
        newPlayingPlaces[to] = piece

        val middleSquare = from.getMiddleSquare(to)
        if (middleSquare != null) {
            newPlayingPlaces[middleSquare] = null // Remove a peça capturada
        }

        return this.copy(playingPlaces = newPlayingPlaces)
    }
}