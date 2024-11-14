package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.storage.Serializer

// gameId: 1
// BLACK:1 WHITE:0
// BLACK
// ...board
object GameSerializer: Serializer<Game>{
    override fun serialize(data: Game)= buildString {
        appendLine(data.gameId)
        appendLine(data.score.entries.joinToString(" "){ (player,points) -> "$player:$points"})
        appendLine(data.firstPlayer)
        data.board?.let{ appendLine(BoardSerializer.serialize(it)) }
    }

    override fun deserialize(text: String): Game {
        val parts = text.split("\n")
        return Game(
            board = if (parts.size==4) null else BoardSerializer.deserialize(parts[3]),
            firstPlayer =  Player.valueOf(parts[2]),
            score = parts[1].split(" ")
                .map { it.split(":") }
                .associate { (player,points) ->
                    Player.entries.firstOrNull { it.name==player } to points.toInt()
                },
            gameId = parts[0]

        )
    }

}

//RUN WHITE | 2a:P:WHITE 8b:Q:BLACK
object BoardSerializer: Serializer<Board> {
    override fun serialize(data: Board): String =
        when(data) {
            is BoardRun -> "RUN ${data.turn}"
            is BoardWin -> "WIN ${data.winner}"
        } + " | " + data.moves.entries.joinToString(" ") { (pos,piece) -> "$pos:${piece.type}:${piece.player}" }

    override fun deserialize(text: String): Board {
        val (left,right) = text.split(" | ")
        val moves = if (right.isEmpty()) mapOf() else right
            .split(" ")
            .map { it.split(":") }
            .associate { (pos,type,player) ->
                pos.toSquare() to getPiece(type, Player.valueOf(player))
            }
        val (type,player) = left.split(" ")
        return when(type) {
            "RUN" -> BoardRun(Player.valueOf(player), moves)
            "WIN" -> BoardWin(Player.valueOf(player), moves)
            else -> error("Illegal board type $type")
        }
    }

    private fun getPiece(type: String, player: Player): Piece {
        return when (type) {
            "P" -> Pawn(player)
            "Q" -> Queen(player)
            else -> error("Unknown piece type $type")
        }
    }

}