package com.king.dexmorphhunter.model

import com.google.gson.Gson

class Test() {
    fun testAllTypes(
        inteiro: Int,
        longo: Long,
        Duplo: Double,
        Flutuante: Float,
        boleavel: Boolean,
        texto: String,
        listaString: List<String>,
        arrayString: Array<String>,
        obj: MeuObjetoPersonalizado,
        listaObj: List<MeuObjetoPersonalizado>,
        arg1: Gson
    ) {
        // Faça algo com os parâmetros aqui
    }
}

data class MeuObjetoPersonalizado(
    val propriedade1: String,
    val propriedade2: Int,
    val propriedade3: MeuObjetoPersonalizado2
)

data class MeuObjetoPersonalizado2(
    val propriedade1: String,
    val propriedade2: Int,
)