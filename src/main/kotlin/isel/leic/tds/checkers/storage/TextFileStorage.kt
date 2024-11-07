package isel.leic.tds.checkers.storage

import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

class TextFileStorage<Key, Data: Any>(
    baseFolderName: String,
    private val serializer: Serializer<Data>
): Storage<Key, Data> {
    private val basePath = Path(baseFolderName)
    // Create base folder if it does not exist
    init {
        with(basePath) {
            if (! exists()) createDirectory()
            else check(isDirectory()) { "$name is not a directory" }
        }
    }

    private fun <T> withPath(key: Key, fx: Path.()->T): T {
        return (basePath / "$key.txt").fx()
    }

    override fun create(key: Key, data: Data) = withPath(key){
        check(! exists()) { "File $key.txt already exists" }
        writeText( serializer.serialize(data) )
    }

    override fun read(key: Key): Data?  = withPath(key) {
        //if(!exists()) return@withPath null// to return null if the file does not exist
        try { serializer.deserialize(readText()) }
        catch (e: NoSuchFileException) { null }
    }

    override fun update(key: Key, data: Data) = withPath(key){
        check(exists()) { "File $key.txt not found" }
        writeText( serializer.serialize(data) )
    }

    override fun delete(key: Key) = withPath(key){
        check(deleteIfExists()) { "File $key.txt not found" }
    }
}