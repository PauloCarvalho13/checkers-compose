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

private fun Clash.remove() {
    if (this is ClashRun)
        try { st.delete(this.id) }
        catch (e:IllegalStateException) {throw GameDeletedException(id) }
}

fun Clash.start(id : Name): Clash {
    require(st.read(id) == null) { "There is a game with this $id name already" }
    val game = Game().new()
    st.create(id,game)
    return ClashRun(st, game, Player.WHITE, id)
}

fun Clash.join(id: Name): Clash{
    val game = requireNotNull(st.read(id)) { "No game found with $id name" }
    return ClashRun(st, game, game.firstPlayer, id)
}

fun Clash.exit() {
    remove()
}

private fun Clash.runOper( oper: ClashRun.()->Game ): Clash {
    check(this is ClashRun) { "Clash not started" }

    return ClashRun(st,oper(),sidePlayer,id)
}

class NoModificationException(name: Name): IllegalStateException("No modification on $name")
class GameDeletedException(name: Name): IllegalStateException("Game $name deleted")

fun Clash.refresh() = runOper {
    (st.read(id) ?: throw GameDeletedException(id))
        .also { if (game == it) throw NoModificationException(id) }
}


fun Clash.newBoard() = runOper {
    game.new().also { st.update(id,it) }
}

fun Clash.play(initpos: Square, finalpos: Square) = runOper {
    check(sidePlayer==(game.board as BoardRun).turn) { "Not your turn" }
    game.play(initpos, finalpos).also {
        try { st.update(id,it) }
        catch (e:IllegalStateException){ throw GameDeletedException(id) }
    }
}

val Clash.isSideTurn: Boolean get() =
    (this is ClashRun) && sidePlayer == when(game.board) {
        is BoardRun -> game.board.turn
        else -> game.firstPlayer
    }

