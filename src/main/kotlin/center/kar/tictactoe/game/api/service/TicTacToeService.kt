package center.kar.tictactoe.game.api.service

import center.kar.tictactoe.game.api.controller.GameStatus
import center.kar.tictactoe.game.api.controller.Player
import center.kar.tictactoe.game.api.controller.dto.TicTacToeGameDto
import center.kar.tictactoe.game.api.model.TicTacToe
import center.kar.tictactoe.game.api.model.TicTacToeLogic
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Service

@Service
class TicTacToeService(
    private val ticTacToeLogic: TicTacToeLogic
) {
    @Transactional
    fun createNewGame(): TicTacToeGameDto {
        val ticTacToe = with(TicTacToe(Array(3) {
            Array(3) {
                null
            }
        })) {
            this.persistAndFlush()
            this
        }

        return TicTacToeGameDto(ticTacToe.gameId!!, ticTacToe.board, ticTacToe.currentTurn)
    }


    @Transactional
    fun makeMove(gameId: Long, row: Int, col: Int, player: Player): TicTacToeGameDto {
        if (getGameById(gameId).gameStatus == null) {
            val game = getGameById(gameId)
            ticTacToeLogic.makeMove(game, row, col, player)
            return TicTacToeGameDto(
                game.gameId!!,
                game.board,
                isEnded = game.gameStatus != null,
                currentTurn = game.currentTurn,
                endType = game.gameStatus,
                winner = when (game.gameStatus) {
                    GameStatus.X_WIN -> Player.X
                    GameStatus.O_WIN -> Player.O
                    else -> null
                }
            )
        } else throw Exception("your game status is ${getGameById(gameId).gameStatus}")
    }

    fun getGameById(id: Long) = TicTacToe.findById(id) ?: throw NotFoundException("no such game $id")

    fun getAllGames(): List<TicTacToe> {
        return TicTacToe.findAll().list()
    }

}

