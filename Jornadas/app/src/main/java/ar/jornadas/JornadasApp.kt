package ar.jornadas

import android.app.Application

/** Dueña de los servicios: vive lo que vive el proceso, no la Activity ni el ViewModel. */
class JornadasApp : Application() {
    val servicios by lazy { Servicios(this) }
}
