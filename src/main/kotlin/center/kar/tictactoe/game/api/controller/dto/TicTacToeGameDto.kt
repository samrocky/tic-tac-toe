package center.kar.tictactoe.game.api.controller.dto

import center.kar.tictactoe.game.api.controller.GameStatus
import center.kar.tictactoe.game.api.controller.Player

data class TicTacToeGameDto(
    val gameId: Long,
    val board: Array<Array<Player?>>,
    val currentTurn: Player?,
    val isEnded: Boolean = false,
    val endType: GameStatus? = null,
    val winner: Player? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TicTacToeGameDto) return false

        if (gameId != other.gameId) return false
        if (!board.contentDeepEquals(other.board)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gameId.hashCode()
        result = 31 * result + board.contentDeepHashCode()
        return result
    }
}
