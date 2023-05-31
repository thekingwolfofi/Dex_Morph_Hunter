package com.king.dexmorphhunter.model

import com.google.gson.Gson

class Test {
    fun testAllTypes(
        inteiro: Int = 42,
        longo: Long = 124L,
        Duplo: Double = 3.14,
        Flutuante: Float = 1.23f,
        boleavel: Boolean = true,
        texto: String = "exemplo",
        listaString: List<String> = listOf("valor1", "valor2"),
        arrayString: Array<String> = arrayOf("valor3", "valor4"),
        obj: MeuObjetoPersonalizado = MeuObjetoPersonalizado(
            propriedade1 = "valor5",
            propriedade2 = 6,
            propriedade3 = MeuObjetoPersonalizado2("valor6", 7)
        ),
        listaObj: List<MeuObjetoPersonalizado> = listOf(
            MeuObjetoPersonalizado(
                propriedade1 = "valor7",
                propriedade2 = 8,
                propriedade3 = MeuObjetoPersonalizado2("valor9", 10)
            ),
            MeuObjetoPersonalizado(
                propriedade1 = "valor11",
                propriedade2 = 12,
                propriedade3 = MeuObjetoPersonalizado2("valor13", 14)
            )
        ),
        argJson: Gson = Gson()
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
    val propriedade2: Int
)
