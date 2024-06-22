/*
 * Copyright (c) 2024.
 * All rights reserved.
 * This code is the property of 'Kar.center' and may not be reproduced or distributed without permission.
 * Author: Sam Rocky
 * Contact: samrocky404@gmail.com
 */

package center.kar.tictactoe.game.api.model



import center.kar.tictactoe.game.api.controller.GameStatus
import center.kar.tictactoe.game.api.controller.Player
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanionBase
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

var random = java.util.Random()
@Entity
class TicTacToe(array: Array<Array<Player?>>) : PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "g_id", nullable = false)
    var gameId: Long? = null

    @Convert(converter = BoardConverter::class)
    val board: Array<Array<Player?>> = Array(3) { Array(3) { null } }

    @Column(name="currentTurn", nullable = true)
    var currentTurn: Player? = null

    @Column(name="game_status", nullable = true)
    var gameStatus: GameStatus? = null

    companion object : PanacheCompanionBase<TicTacToe, Long>

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as TicTacToe

        return gameId != null && gameId == other.gameId
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(gameId = $gameId, board = \n${board.map { "\n" + it.contentDeepToString() }}\n)"
    }

    @PrePersist
    fun gameCreated(){
        currentTurn = Player.X
    }

}

val objectMapper = ObjectMapper()

@Converter
class BoardConverter : AttributeConverter<Array<Array<Player?>>, String> {
    override fun convertToDatabaseColumn(attribute: Array<Array<Player?>>?): String? =
        attribute?.let {
            objectMapper.writeValueAsString(it)
        }

    override fun convertToEntityAttribute(dbData: String?): Array<Array<Player?>>? =
        dbData?.let {
            val w = objectMapper.readValue(it, List::class.java)
            w as List<List<String?>>
            w.map { it.map { it?.let { Player.valueOf(it) } }.toTypedArray() }.toTypedArray()
        }
}

fun <T : Enum<*>?> randomEnum(clazz: Class<T>): T {
    val x: Int = random.nextInt(clazz.enumConstants.size)
    return clazz.enumConstants[x]
}