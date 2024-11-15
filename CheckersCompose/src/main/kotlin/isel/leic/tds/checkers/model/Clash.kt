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

private fun Clash.join(id: Name): Clash{
    val game = requireNotNull(st.read(id))
    return ClashRun(st, game, game.firstPlayer, id)
}

private fun Clash.runOper( oper: ClashRun.()->Game ): Clash {
    check(this is ClashRun) { "Clash not started" }

    return ClashRun(st,oper(),sidePlayer,id)
}

fun Clash.refresh() = runOper {
    (st.read(id) as Game).also { check(game!=it) { "No modification" } }
}

fun Clash.play(initpos: Square, finalpos: Square) = runOper {
    check(sidePlayer==(game.board as BoardRun).turn) { "Not your turn" }
    game.play(initpos, finalpos).also {
        st.update(id,it)
    }
}

