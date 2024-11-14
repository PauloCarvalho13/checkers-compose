import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.storage.TextFileStorage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

private val sequence = listOf(
    Pair("3c".toSquare(), "4b".toSquare())
    )

class SerializerTests {
    private fun playSequence(moves: List<Pair<Square, Square>>): Board =
        moves.fold(BoardRun(Player.WHITE).init() as Board) { b, move -> b.play(move.first, move.second) }

    @Test
    fun `test BoardSerializer with BoardRun1`() {
        val board = playSequence(sequence)
        val serialized = BoardSerializer.serialize(board)

        val deserializedBoard = BoardSerializer.deserialize(serialized)
        assertEquals(board, deserializedBoard)
    }

    @Test
    fun `test BoardSerializer with BoarRun2`() {
        val board = BoardRun(turn = Player.WHITE, squares = mapOf(
            "2a".toSquare() to Pawn(Player.BLACK),
            "5a".toSquare() to Pawn(Player.WHITE),
            "4b".toSquare() to Pawn(Player.BLACK),
        ))
        val text = BoardSerializer.serialize(board)
        assertEquals("RUN WHITE | 2a:P:BLACK 5a:P:WHITE 4b:P:BLACK", text)

        val deserializedBoard = BoardSerializer.deserialize(text)
        assertEquals(board, deserializedBoard)
    }

    @Test
    fun `test BoardSerializer with BoardWin`(){
        val board = BoardWin(winner = Player.BLACK, squares = mapOf(
            "2a".toSquare() to Queen(Player.BLACK)
        ))
        val text = BoardSerializer.serialize(board)
        assertEquals("WIN BLACK | 2a:Q:BLACK", text)

        val deserializedBoard = BoardSerializer.deserialize(text)
        assertEquals(board, deserializedBoard)
    }

    @Test
    fun `test GameSerializer with board init`() {
        val game = Game().new()
        val text = GameSerializer.serialize(game)

        val deserializedGame = GameSerializer.deserialize(text)
        assertEquals(game, deserializedGame)
    }

    @Test
    fun `test GameSerializer with board null`() {
        val game = Game(gameId = "2", board = null, score = (Player.entries).associateWith { 0 }, firstPlayer = Player.entries.first())
        val text = GameSerializer.serialize(game)
        println(text)
        val deserializedGame = GameSerializer.deserialize(text)
        assertEquals(game, deserializedGame)
    }
}