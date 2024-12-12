package isel.leic.tds.checkers.model

import isel.leic.tds.checkers.storage.Storage

@JvmInline
value class Name(private val value: String) {
    init { require(isValid(value)) { "Invalid name" } }
    override fun toString() = value
    companion object {
        fun isValid(value: String) =
            value.isNotEmpty() && value.all { it.isLetterOrDigit() } && value.none { it==' ' }
    }
}

typealias GameStorage = Storage<Name, Game>

open class Clash(val st: GameStorage)

class ClashRun(
    st: GameStorage,
    val game: Game,
    val sidePlayer: Player,
    val id: Name,
) : Clash(st)

private fun Clash.removeIfStarted() {
    if (this is ClashRun && sidePlayer==Player.WHITE)
        st.delete(this.id)
}

fun Clash.start(id : Name): Clash {
    val gameCreated = st.read(id)
    if(gameCreated != null){
        return join(id)
    }else{
        val game = Game().new()
        st.create(id,game)
        return ClashRun(st, game, Player.WHITE, id)
    }
}

fun Clash.join(id: Name): Clash{
    val game = requireNotNull(st.read(id))
    return ClashRun(st, game, game.firstPlayer, id)
}

fun Clash.exit() {
    removeIfStarted()
}

private fun Clash.runOper( oper: ClashRun.()->Game ): Clash {
    check(this is ClashRun) { "Clash not started" }

    return ClashRun(st,oper(),sidePlayer,id)
}

class NoModificationException(name: Name): IllegalStateException("No modification on $name")
class GameDeletedException(name: Name): IllegalStateException("Game $name deleted")

fun Clash.refresh() = runOper {
    (st.read(id) as Game).also { check(game!=it) { "No modification" } }
}

fun Clash.newBoard() = runOper {
    game.new().also { st.update(id,it) }
}

fun Clash.play(initpos: Square, finalpos: Square) = runOper {
    check(sidePlayer==(game.board as BoardRun).turn) { "Not your turn" }
    game.play(initpos, finalpos).also {
        st.update(id,it)
    }
}

val Clash.isSideTurn: Boolean get() =
    (this is ClashRun) && sidePlayer == when(game.board) {
        is BoardRun -> game.board.turn
        else -> game.firstPlayer
    }

