package center.kar.tictactoe.game.api.model

import center.kar.tictactoe.game.api.controller.Player
import center.kar.tictactoe.game.api.controller.GameStatus
import org.springframework.stereotype.Service


@Service
class TicTacToeLogic {

    fun makeMove(game: TicTacToe, row: Int, col: Int, player: Player) {

        if (player != game.currentTurn) throw Exception("not your turn")
        if (row > 2 || col > 2 || game.board[row][col] != null) throw Exception("invalid move")

        // gooogoolie
        game.board[row][col] = player
        game.gameStatus = whoWins(game.board) ?: if (isDraw(game.board)) GameStatus.DRAW else null

        game.currentTurn = if(isEnded(game.board)) null else if (player == Player.X) Player.O else Player.X

        game.persistAndFlush()
    }

    fun isEnded(board: Array<Array<Player?>>): Boolean {
        return if (board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][2] != null) true
        else if (board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][2] != null) true
        else if (board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][2] != null) true
        else if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[2][0] != null) true
        else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[2][1] != null) true
        else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[2][2] != null) true
        else if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != null) true
        else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != null) true
        else false
    }

    private fun whoWins(board: Array<Array<Player?>>): GameStatus? {
        val player = (if (board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][2] != null) board[0][0]
        else if (board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][2] != null) board[1][0]
        else if (board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][2] != null) board[2][0]
        else if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[2][0] != null) board[0][0]
        else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[2][1] != null) board[0][1]
        else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[2][2] != null) board[0][2]
        else if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != null) board[0][0]
        else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != null) board[0][2]
        else return null) ?: throw NoWhenBranchMatchedException("this could not happen - no concurrency")

        return when (player) {
            Player.X -> GameStatus.X_WIN
            Player.O -> GameStatus.O_WIN
        }
    }

    private fun isDraw(board: Array<Array<Player?>>): Boolean {
        return isEnded(board) && whoWins(board) == null
    }
}