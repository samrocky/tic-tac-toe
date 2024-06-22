package center.kar.tictactoe.game.api.controller


import center.kar.tictactoe.game.api.controller.dto.TicTacToeGameDto
import center.kar.tictactoe.game.api.model.TicTacToe
import center.kar.tictactoe.game.api.service.TicTacToeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


enum class Player {
    X, O
}

enum class GameStatus {
    DRAW,
    X_WIN,
    O_WIN
}

@RestController
@RequestMapping("/api/tic-tac-toe/")
class TicTacToeGameController(
    private val ticTacToeService: TicTacToeService
) {
    @GetMapping("/new")
    fun createNewGame(): TicTacToeGameDto =
        ticTacToeService.createNewGame()

    @PostMapping("/move/{gameId}")
    fun makeMove(@PathVariable gameId: Long, @RequestParam sell: Int, @RequestParam player: Player): TicTacToeGameDto {
        val row = (sell - 1) / 3
        val col = (sell - 1) % 3
        return ticTacToeService.makeMove(gameId, row, col, player)
    }


    @GetMapping("/{gameId}")
    fun getGameById(@PathVariable gameId: Long): TicTacToe =
        ticTacToeService.getGameById((gameId))


    @GetMapping("/list")
    fun allGames(@PathVariable gameId: Long): List<TicTacToe> =
        ticTacToeService.getAllGames().toList()
}