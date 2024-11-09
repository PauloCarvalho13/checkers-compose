import isel.leic.tds.checkers.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val gettingBlackQueenSequence = listOf(
    Pair("3e".toSquare(), "4f".toSquare()),
    Pair("6f".toSquare(), "5g".toSquare()),
    Pair("2f".toSquare(), "3e".toSquare()),
    Pair("7g".toSquare(), "6f".toSquare()),
    Pair("3c".toSquare(), "4b".toSquare()),
    Pair("6b".toSquare(), "5c".toSquare()),
    Pair("2d".toSquare(), "3c".toSquare()),
    Pair("7a".toSquare(), "6b".toSquare()),
    Pair("4f".toSquare(), "5e".toSquare()),
    Pair("6d".toSquare(), "4f".toSquare()),
    Pair("4f".toSquare(), "2d".toSquare()),
    Pair("1c".toSquare(), "3e".toSquare()),
    Pair("5g".toSquare(), "4f".toSquare()),
    Pair("4b".toSquare(), "6d".toSquare()),
    Pair("4f".toSquare(), "2d".toSquare()),
    Pair("3g".toSquare(), "4f".toSquare()),
    Pair("7e".toSquare(), "5c".toSquare()),
    Pair("2h".toSquare(), "3g".toSquare()),
    Pair("2d".toSquare(), "1c".toSquare()),
)

private val blackWinningSequence = listOf(
    Pair("3e".toSquare(), "4f".toSquare()),
    Pair("6f".toSquare(), "5g".toSquare()),
    Pair("2f".toSquare(), "3e".toSquare()),
    Pair("7g".toSquare(), "6f".toSquare()),
    Pair("3c".toSquare(), "4b".toSquare()),
    Pair("6b".toSquare(), "5c".toSquare()),
    Pair("2d".toSquare(), "3c".toSquare()),
    Pair("7a".toSquare(), "6b".toSquare()),
    Pair("4f".toSquare(), "5e".toSquare()),
    Pair("6d".toSquare(), "4f".toSquare()),
    Pair("4f".toSquare(), "2d".toSquare()),
    Pair("1c".toSquare(), "3e".toSquare()),
    Pair("5g".toSquare(), "4f".toSquare()),
    Pair("4b".toSquare(), "6d".toSquare()),
    Pair("4f".toSquare(), "2d".toSquare()),
    Pair("3g".toSquare(), "4f".toSquare()),
    Pair("7e".toSquare(), "5c".toSquare()),
    Pair("2h".toSquare(), "3g".toSquare()),
    Pair("2d".toSquare(), "1c".toSquare()),
    Pair("3c".toSquare(), "4b".toSquare()),
    Pair("1c".toSquare(), "5g".toSquare()),
    Pair("4b".toSquare(), "6d".toSquare()),
    Pair("7c".toSquare(), "5e".toSquare()),
    Pair("3g".toSquare(), "4f".toSquare()),
    Pair("5g".toSquare(), "3e".toSquare()),
    Pair("1e".toSquare(), "2d".toSquare()),
    Pair("3e".toSquare(), "1c".toSquare()),
    Pair("3a".toSquare(), "4b".toSquare()),
    Pair("1c".toSquare(), "3a".toSquare()),
    Pair("3a".toSquare(), "5c".toSquare()),
    Pair("1g".toSquare(), "2f".toSquare()),
    Pair("5c".toSquare(), "1g".toSquare()),
    Pair("1a".toSquare(), "2b".toSquare()),
    Pair("5e".toSquare(), "4d".toSquare()),
    Pair("2b".toSquare(), "3c".toSquare()),
    Pair("4d".toSquare(), "2b".toSquare()),
)

fun playSequence(moves: List<Pair<Square, Square>>): Board =
    moves.fold(BoardRun(Player.WHITE).init() as Board) { b, move -> b.play(move.first, move.second) }

class BoardTests {
    @Test
    fun testInitialBoard() {
        val board = BoardRun(turn = Player.WHITE)
        assertTrue(board.moves.isEmpty())
        assertEquals(Player.WHITE, board.turn)
    }

    // All tests below assume BOARD_DIM is 8
    @Test
    fun `test getting a Queen as BLACK`() {

        val board = playSequence(gettingBlackQueenSequence)
        assertTrue(board.moves["1c".toSquare()] is Queen)
    }

    @Test
    fun `test invalid move`() {
        val boardBefore = playSequence(
            listOf(
                Pair("3e".toSquare(), "4f".toSquare()),
                Pair("6f".toSquare(), "5g".toSquare()),
                Pair("2f".toSquare(), "3e".toSquare())
            )
        )
        // try to move back with a Pawn
        val boardAfter = boardBefore.play("4f".toSquare(), "3e".toSquare())

        assertEquals(boardBefore, boardAfter)
    }
    @Test
    fun `test win game`() {
        val board = playSequence(blackWinningSequence)
        assertTrue(board is BoardWin)
    }

    @Test
    fun `try make a move when you have available captures`(){
        val board = playSequence(
            listOf(
                Pair("3c".toSquare() , "4d".toSquare()),
                Pair("6f".toSquare(), "5e".toSquare()),
                Pair("4d".toSquare(), "5c".toSquare())
            )
        )
        val expectBoard = playSequence(
            listOf(
                Pair("3c".toSquare() , "4d".toSquare()),
                Pair("6f".toSquare(), "5e".toSquare()),
            )
        )
        assertEquals(expectBoard, board)
    }

    @Test
    fun `test queen capture`(){
        val board = playSequence(gettingBlackQueenSequence)
            .play("3c".toSquare(), "4b".toSquare())
            .play("1c".toSquare(), "5g".toSquare())
        assertEquals(Queen(Player.BLACK), board.moves["5g".toSquare()])
    }

    @Test
    fun `try queen multiple capture`(){
        val board = playSequence(gettingBlackQueenSequence)
            .play("1e".toSquare(), "2d".toSquare())

        val nextBoard = board.play("1c".toSquare(), "5g".toSquare())

        //Board are the same because the capture is invalid so the board isn´t updated
        assertEquals(nextBoard, board)

        val boardAfterQueenCaptures = board.play("1c".toSquare(), "3e".toSquare())
            .play("3e".toSquare(), "5g".toSquare())
        assertEquals(Queen(Player.BLACK), boardAfterQueenCaptures["5g".toSquare()])

    }

    @Test
    fun `queen move through the board `(){
        val board = playSequence(blackWinningSequence.dropLast(3))
            .play("1g".toSquare(), "5c".toSquare())
        assertEquals(Queen(Player.BLACK), board["5c".toSquare()])
    }
}