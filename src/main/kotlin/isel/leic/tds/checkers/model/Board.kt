package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.BOARD_DIM

typealias Moves = Map<Square,Piece>

sealed class Board(val moves: Moves)
class BoardRun(val turn: Player, val squares: Moves = emptyMap()): Board(squares)
class BoardWin( val winner: Player, val squares: Moves): Board(squares)
class BoardDraw(val squares: Moves): Board(squares)


private val piecesPerPlayer = mapOf(8 to 12, 6 to 8,  4 to 2)

fun BoardRun.init(): Board {

    val piecesPerPlayer = piecesPerPlayer[BOARD_DIM]
    checkNotNull(piecesPerPlayer){"Invalid Board size"}

    val blackSquares = Square.values.filter { it.black }

    val squaresForPlayerWhite = blackSquares.take(piecesPerPlayer)
    val squaresForPlayerBlack = blackSquares.takeLast(piecesPerPlayer)

    val initialBoard =
        squaresForPlayerWhite.associateWith { Pawn(Player.WHITE) } + squaresForPlayerBlack.associateWith { Pawn(Player.BLACK) }
    return BoardRun(turn = turn, initialBoard)
}

operator fun Board.get(square: Square): Piece? = moves[square]

fun Board.isValidMove(piecePosition: Square, newPiecePosition: Square, turn: Player)
        =   moves.containsKey(piecePosition)
            && moves[piecePosition]?.player == turn
            && (newPiecePosition.black && !moves.containsKey(newPiecePosition))

fun Board.play(from: Square, to: Square, turn: Player): Board {
    check(isValidMove(from, to, turn)) { "Invalid move" }
    when (this) {
        is BoardRun -> {
            val movesAfter = this.makePlay(from , to)
            val winner = winner()
            return when {
                winner != null -> BoardWin(winner, movesAfter)
                draw() -> BoardDraw(movesAfter)
                else -> BoardRun(turn.other, movesAfter)
            }
        }
        is BoardWin, is BoardDraw -> error("Game over")
    }
}

fun BoardRun.makePlay(from: Square, to: Square): Moves {
    val piece = moves[from]

    when (piece){
        is Pawn -> {

        }
        is Queen ->{

        }
    }

    return this.moves
}

fun Board.winner(): Player? {
    return Player.BLACK
}

fun Board.draw(): Boolean
     = moves.isEmpty()  || moves.hasNoValidPlay()

fun Moves.hasNoValidPlay(): Boolean{
    return true
}









/*
    val fromPiece = board.playingPlaces[from]
    require(fromPiece != null) { "No piece at the from position" }

    val captureBoard = getAllCaptures(this.turn)

    if (captureBoard.playingPlaces.isNotEmpty()) {
        if (!captureBoard.playingPlaces.containsKey(from)) {
            println("Captures available in ${captureBoard.playingPlaces.keys}, you must capture!")
            return this
        }
    }

    if (isValidMove(from, to, this.turn)) {

        //Checking the possible Moves from can do
        val possibleMoves = fromPiece.canMove(from, board)

        // If there is no Moves check if there is Captures
        if (possibleMoves.playingPlaces.containsKey(to)) {
            //Checking if the piece is a Pawn or a Queen
            val checkPiece = checkPiece(to, fromPiece)
            // Updating the board and changing the turn
            return this.copy(board = board.updateBoard(from, to, checkPiece), turn = turn.other)
        }else{

            val possibleCaptures = fromPiece.canCapture(from, board)
            // if there is no Captures -> error
            if(possibleCaptures.playingPlaces.isEmpty()){
                println("Invalid Move")
                println("Play Again!")
                return this // still this turn because something went wrong
            }else{
                //Checking if the Square to its in possibleCaptures and update board
                if (possibleCaptures.playingPlaces.containsKey(to)){
                    //Checking if the piece is a Pawn or a Queen
                    val checkPiece = checkPiece(to, fromPiece)

                    val updatedGame = this.copy(board = board.updateBoard(from, to, checkPiece))

                    if (checkPiece.canCapture(to, updatedGame.board).playingPlaces.isNotEmpty()) {
                        println("You can play again!")
                        return updatedGame // Turn doesn't change
                    } else {
                        // If there is no more Captures, change turn
                        return updatedGame.copy(turn = turn.other)
                    }
                }
            }
        }
    } else {
        println("Invalid Position")
        return this
    }
    return this

    return this
}

fun Board.checkPiece(to: Square, piece: Piece): Piece{
    return when(piece){
        is Pawn -> {
            if(piece.player == Player.WHITE && to.row.digit == '8'){
                Queen(Player.WHITE)
            }
            else if (piece.player == Player.BLACK && to.row.digit == '1'){
                Queen(Player.BLACK)
            }
            else{
                piece // There's no condition to change pawn to queen so it remains the same piece
            }
        }
        else -> piece // if it's already a Queen stays the same
    }
}

fun Board.getAllCaptures(player: Player): Board {
    val captures = buildMap<Square, Piece?> {
        board.playingPlaces.forEach { (square, piece) ->
            if (piece != null && piece.player == player) {
                val possibleCaptures = piece.canCapture(square, board)
                if (possibleCaptures.playingPlaces.isNotEmpty()) {
                    put(square, piece) // Adiciona a peça que pode capturar
                }
            }
        }
    }
    return Board(playingPlaces = captures)
}

 */