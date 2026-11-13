package ar.jornadas.data

class AgendaRepository(private val api: ProgramaApi, private val dao: CarteleraDao) {
    val programa = dao.observar()
    val hayEdicion = dao.hayEdicion()

    /** Si la consulta falla lanza la excepción sin tocar lo guardado. */
    suspend fun sincronizar() {
        val fichas = api.consultar().map {
            Charla(it.id, it.title, it.track, it.day, it.time, it.room, it.speaker, it.summary)
        }
        dao.reemplazarPrograma(fichas)
    }

    suspend fun cambiarFavorita(id: String) = dao.alternar(id)
}
