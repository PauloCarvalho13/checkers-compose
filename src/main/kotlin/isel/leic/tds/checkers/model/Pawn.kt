package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM
import isel.leic.tds.checkers.ui.showBoard
import kotlin.math.absoluteValue

enum class Direction(val row: Int, val column: Int) {
    UP_LEFT(-1, -1), //whiteMove
    UP_RIGHT(-1, 1), //whiteMove
    DOWN_LEFT(1, -1), //blackMove
    DOWN_RIGHT(1, 1), //blackMove
    UP_LEFT_CAP(2, -2),
    UP_RIGHT_CAP(2, 2),
    DOWN_LEFT_CAP(-2, -2),
    DOWN_RIGHT_CAP(-2, 2)
}

class Pawn(player: Player): Piece(player) {
    //PEAO -> Posição (4,d)
    //Pode jogar em (5,c) (5,e)
    override fun canMove(position: Square, board: Board): Board {
        val directions = if (player == Player.WHITE) {
            listOf(Direction.UP_LEFT, Direction.UP_RIGHT)
        } else {
            listOf(Direction.DOWN_LEFT, Direction.DOWN_RIGHT)
        }
        //println("Posição atual: ${position.row.digit}, ${position.column.symbol}")

        val newPlayingPlaces = directions.mapNotNull { direction ->
            position.move(direction)?.takeIf { newSquare ->
                board.playingPlaces[newSquare] == null
            }?.let { newSquare -> newSquare to this }
        }.toMap()

        return Board(newPlayingPlaces)
    }

    //PEAO -> Posição (4,d)
    //Pode jogar em (2,b) (6,f) (2,f) (6,b)
    override fun canCapture(position: Square, board: Board): Board {
        val captureDirections = listOf(
            Direction.UP_LEFT_CAP,
            Direction.UP_RIGHT_CAP,
            Direction.DOWN_LEFT_CAP,
            Direction.DOWN_RIGHT_CAP
        )

        //Valid Moves to Capture if there is a piece different then ours in the middle of our targetSquare
        val captureMoves = captureDirections.mapNotNull { direction ->
            val targetSquare = position.move(direction)
            val middleSquare = targetSquare?.let { position.getMiddleSquare(it) }

            // State of piece in MiddleSquare, if null there's no piece then we cant capture, but we can move
            // Needs to be different from the player who is playing
            // TargetSquare needs to be null, this means that the square is valid and is "empty" (no piece)
            if (middleSquare != null) {
                val middleSquarePiece = board.playingPlaces[middleSquare]

                // Check if the middle square contains an opponent's piece and the target square is empty
                if (middleSquarePiece != null
                    && middleSquarePiece.player != this.player
                    && targetSquare != null
                    && board.playingPlaces[targetSquare] == null) {
                    targetSquare to this
                } else {
                    null
                }
            } else {
                null
            }
        }.toMap()

        return Board(captureMoves)
    }

}

fun Square.move(direction: Direction): Square? {
    val newRow = this.row.index + direction.row
    val newColumn = this.column.index + direction.column

    return if (newRow in 0..<BOARD_DIM && newColumn in 0..<BOARD_DIM) {
        Square(Row(newRow), Column(newColumn))
    } else {
        null
    }
}
fun Square.getMiddleSquare(to: Square): Square? {
    val rowDiff = (this.row.index - to.row.index).absoluteValue
    val colDiff = (this.column.index - to.column.index).absoluteValue

    // Se a diferença for de exatamente duas casas, encontramos o quadrado do meio
    return if (rowDiff == 2 && colDiff == 2) {
        Square(
            Row((this.row.index + to.row.index) / 2),
            Column((this.column.index + to.column.index) / 2)
        )
    } else {
        null
    }
}


fun main() {
    val whitePlayer = Player.WHITE
    val blackPlayer = Player.BLACK

    val board = getInitialBoard()
    val game = Game(gameId = "1",board = board, turn = whitePlayer, initPlayer = blackPlayer)
    game.showBoard()

    val whitePawn = Pawn(whitePlayer)
    val blackPawn = Pawn(blackPlayer)

    val whiteSquareMov = "4d".toSquare() //index 3 0
    val blackSquareMov = "5e".toSquare() //index 2 1

    // Teste de movimentos
    val newBoardAfterMove = whitePawn.canMove(whiteSquareMov, board)
    val newBoardAfterMove1 = blackPawn.canMove(blackSquareMov, newBoardAfterMove)

    //println("Possíveis movimentos da whitePawn(${whiteSquareMov}): ${newBoardAfterMove.playingPlaces.keys} ")
    //println("Possíveis movimentos de blackPawn(${blackSquareMov}): ${newBoardAfterMove1.playingPlaces.keys}")

    // Teste de captura
    val boardWithBlackPawn = Board(mapOf(blackSquareMov to blackPawn))
    val newBoardAfterCapture = whitePawn.canCapture(whiteSquareMov, boardWithBlackPawn)
    //val newBoardAfterCapture1 = blackPawn.canCapture(blackSquareMov, board)

    println("Possíveis capturas de whitePawn: ${newBoardAfterCapture.playingPlaces.keys}")
    //println("Possíveis capturas de blackPawn: ${newBoardAfterCapture1.playingPlaces.keys}")

}