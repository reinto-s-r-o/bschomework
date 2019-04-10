package cz.reinto.bschomework.model

import java.io.Serializable

data class Note(
    val id: Int,
    val title: String
) : Serializable