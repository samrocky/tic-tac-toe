package center.kar.expense_managment_service

import center.kar.logger
import center.kar.tictactoe.game.api.controller.GameStatus
import center.kar.tictactoe.game.api.controller.Player.*
import center.kar.tictactoe.game.api.controller.Player
import center.kar.tictactoe.game.api.controller.TicTacToeGameController
import center.kar.tictactoe.game.api.model.TicTacToe
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
//import center.kar.logger
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test

@QuarkusTest
class TicTacToeTest {

    @Test
    @Transactional
    fun `tic tac toe entity test`() {
        val entity = TicTacToe(
            Array(3) {
                Array(3) {
                    null
                }
            }
        )

        entity.persistAndFlush()

        logger.info(TicTacToe.findById(entity.gameId!!).toString())

        entity.board[0][0] = Player.X
        entity.persistAndFlush()

        logger.info(TicTacToe.findById(entity.gameId!!).toString())

    }


    @Inject
    lateinit var controller: TicTacToeGameController

    @Test
    fun `end game rules`() {

        val gameId = controller.createNewGame().gameId


        // game mode
        /*
        | X1 | X2 | X3 |
        | O1 | N | N |
        | O2 | N | N |
         */

        controller.makeMove(gameId, 1, Player.X)
        controller.makeMove(gameId, 4, Player.O)
        controller.makeMove(gameId, 2, Player.X)
        controller.makeMove(gameId, 7, Player.O)
        val res = controller.makeMove(gameId, 3, Player.X)


        res.winner shouldBe X
        res.endType shouldBe GameStatus.X_WIN
        res.board shouldBe arrayOf(
            arrayOf(X, X, X),
            arrayOf(O, null, null),
            arrayOf(O, null, null)
        )
        res.isEnded.shouldBeTrue()
        res.gameId shouldBe gameId
        res.currentTurn shouldBe null
    }
}