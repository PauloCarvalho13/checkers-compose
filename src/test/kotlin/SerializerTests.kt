import isel.leic.tds.checkers.model.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

private val sequence = listOf(
    Pair("3c".toSquare(), "4b".toSquare())
    )

class SerializerTests {
    private fun playSequence(moves: List<Pair<Square, Square>>): Board =
        moves.fold(BoardRun(Player.WHITE).init() as Board) { b, move -> b.play(move.first, move.second) }

    @Test
    fun `test BoardSerializer with BoardRun`() {
        // Criar um BoardWin
        val board = playSequence(sequence)
        val serialized = BoardSerializer.serialize(board)
        assertEquals("RUN BLACK | 8b:Pawn:BLACK 8d:Pawn:BLACK 8f:Pawn:BLACK 8h:Pawn:BLACK 6h:Pawn:BLACK 6f:Pawn:BLACK 6b:Pawn:BLACK 1g:Queen:BLACK 4d:Pawn:BLACK 3c:Pawn:WHITE", serialized)

        val deserializedBoard = BoardSerializer.deserialize(serialized)
        assertEquals(board, deserializedBoard)
    }

    @Test
    fun `test BoardSerializer with BoarRsun`() {
        val board = BoardRun(turn = Player.WHITE, squares = mapOf(
            "2a".toSquare() to Pawn(Player.BLACK),
            "5a".toSquare() to Pawn(Player.WHITE),
            "4b".toSquare() to Pawn(Player.BLACK),
        ))
        val text = BoardSerializer.serialize(board)
        assertEquals("RUN WHITE | 2a:P:BLACK 5a:P:WHITE 4b:P:BLACK", text)

        val otherBoard = BoardSerializer.deserialize(text)
        assertEquals(board, otherBoard)
    }
}