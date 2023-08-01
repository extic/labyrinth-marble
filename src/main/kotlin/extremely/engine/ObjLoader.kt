package extremely.engine

import extremely.core.models.RawModel
import org.joml.Vector2f
import org.joml.Vector3f
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

class ObjLoader {

    fun loadObjModel(fileName: String, loader: Loader): RawModel {
        val vertices = ArrayList<Vector3f>()
        val textures = ArrayList<Vector2f>()
        val normals = ArrayList<Vector3f>()
        val faces = ArrayList<List<Triple<Int, Int, Int>>>()
        val indices = ArrayList<Int>()

        Files.lines(Paths.get(fileName)).asSequence()
            .filterNot { it.startsWith("#") }
            .map { it.split(" ") }
            .forEach { parts ->
                when (parts[0]) {
                    "v" -> Vector3f(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat())
                        .also { vertices.add(it) }

                    "vt" -> Vector2f(parts[1].toFloat(), 1 - parts[2].toFloat())
                        .also { textures.add(it) }

                    "vn" -> Vector3f(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat())
                        .also { normals.add(it) }

                    "f" -> parts
                        .drop(1)
                        .map { vertexData ->
                            vertexData
                                .split("/")
                                .map { it.toInt() }
                                .let { Triple(it[0], it[1], it[2]) }
                        }
                        .let { faces.add(it) }
                }
            }

        val textureArray = FloatArray(vertices.size * 2)
        val normalArray = FloatArray(vertices.size * 3)

        faces.forEach { face ->
            processVertex(face[0], indices, textures, normals, textureArray, normalArray)
            processVertex(face[1], indices, textures, normals, textureArray, normalArray)
            processVertex(face[2], indices, textures, normals, textureArray, normalArray)
        }

        val verticesArray = FloatArray(vertices.size * 3)
        vertices.forEachIndexed { i, vertex ->
            verticesArray[i * 3] = vertex.x
            verticesArray[i * 3 + 1] = vertex.y
            verticesArray[i * 3 + 2] = vertex.z
        }

        val indicesArray = IntArray(indices.size)
        indices.forEachIndexed { i, index ->
            indicesArray[i] = index
        }

        return loader.loadToVao(verticesArray, textureArray, indicesArray)
    }

    private fun processVertex(
        faceData: Triple<Int, Int, Int>,
        indices: java.util.ArrayList<Int>,
        textures: java.util.ArrayList<Vector2f>,
        normals: java.util.ArrayList<Vector3f>,
        textureArray: FloatArray,
        normalArray: FloatArray
    ) {
        val currentVertexPointer = faceData.first - 1
        indices.add(currentVertexPointer)

        val currentTexture = textures[faceData.second - 1]
        textureArray[currentVertexPointer * 2] = currentTexture.x
        textureArray[currentVertexPointer * 2 + 1] = currentTexture.y

        val currentNormal = normals[faceData.third - 1]
        normalArray[currentVertexPointer * 2] = currentNormal.x
        normalArray[currentVertexPointer * 2 + 1] = currentNormal.y
        normalArray[currentVertexPointer * 2 + 2] = currentNormal.z
    }
}