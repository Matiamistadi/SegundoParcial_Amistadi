package ar.jornadas.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CarteleraDao {
    @Query("SELECT * FROM charlas ORDER BY dia, hora, sala")
    abstract fun observar(): Flow<List<Charla>>

    @Query("SELECT EXISTS(SELECT 1 FROM edicion)")
    abstract fun hayEdicion(): Flow<Boolean>

    @Query("UPDATE charlas SET favorita = NOT favorita WHERE id = :id")
    abstract suspend fun alternar(id: String)

    @Query("SELECT id FROM charlas WHERE favorita = 1")
    protected abstract suspend fun idsFavoritas(): List<String>

    @Query("DELETE FROM charlas")
    protected abstract suspend fun vaciar()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertar(fichas: List<Charla>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun registrarEdicion(edicion: Edicion)

    /** Sustituye todo el programa por la instantánea, conservando las marcas de las charlas vigentes. */
    @Transaction
    open suspend fun reemplazarPrograma(fichas: List<Charla>) {
        val marcadas = idsFavoritas().toSet()
        vaciar()
        insertar(fichas.map { it.copy(favorita = it.id in marcadas) })
        registrarEdicion(Edicion())
    }
}
