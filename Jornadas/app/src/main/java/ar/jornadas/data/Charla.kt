package ar.jornadas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charlas")
data class Charla(@PrimaryKey val id: String, val titulo: String, val tema: String, val dia: Int,
    val hora: String, val sala: String, val ponente: String, val resumen: String,
    val favorita: Boolean = false)

/** Su fila existe si alguna vez se guardó una edición completa, aunque no tuviera charlas. */
@Entity(tableName = "edicion")
data class Edicion(@PrimaryKey val id: Int = 1)
