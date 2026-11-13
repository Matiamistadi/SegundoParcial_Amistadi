package ar.jornadas.ui

import android.app.Application
import androidx.lifecycle.*
import ar.jornadas.JornadasApp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProgramaModel(app: Application) : AndroidViewModel(app) {
    private val servicios = (app as JornadasApp).servicios
    val mesa = servicios.mesa
    private val repo = servicios.agenda
    val charlas = repo.programa.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** null mientras se lee el almacenamiento; true si alguna vez se guardó una edición. */
    val hayEdicion = repo.hayEdicion.map<Boolean, Boolean?> { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val aviso = MutableStateFlow("")
    val cargando = MutableStateFlow(false)

    init {
        if (servicios.consumirCargaAutomatica()) actualizar()
    }

    /** Pedido explícito o automático; se ignora si ya hay una petición en curso. */
    fun actualizar() {
        if (!cargando.compareAndSet(false, true)) return
        aviso.value = ""
        viewModelScope.launch {
            try {
                repo.sincronizar()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                aviso.value = "No se pudo consultar el programa"
            } finally {
                cargando.value = false
            }
        }
    }

    fun marcar(id: String) { viewModelScope.launch { repo.cambiarFavorita(id) } }
}
