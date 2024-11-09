import isel.leic.tds.checkers.storage.Serializer
import isel.leic.tds.checkers.storage.TextFileStorage
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.test.Test
import kotlin.test.assertFailsWith

class TextFileTests {
    companion object {
        private const val FOLDER_NAME = "test"
        val storage = TextFileStorage<String,String>(FOLDER_NAME,
            object : Serializer<String> {
                override fun serialize(data: String) = data
                override fun deserialize(text: String) = text
            }
        )
        @BeforeAll
        @JvmStatic fun setup() {
            assertTrue(Path(FOLDER_NAME).exists())
        }
        @AfterAll
        @JvmStatic fun cleanup() {
            @OptIn(ExperimentalPathApi::class)
            Path(FOLDER_NAME).deleteRecursively()
        }
    }

    @Test fun `Create and Read an entry`() {
        val key = "game1"
        storage.create(key,"data")
        assertEquals("data", storage.read(key))
    }
    @Test fun `Create an entry that already exists`() {
        val key = "game2"
        storage.create(key,"data")
        assertFailsWith<IllegalStateException> {
            storage.create(key,"data")
        }
    }

    @Test fun `Read an entry that does not exist`() {
        val key = "game3"
        assertNull(storage.read(key))
    }

    @Test fun `Update existing entry`() {
        val key = "game4"
        storage.create(key, "old data")
        storage.update(key, "new data")
        assertEquals("new data", storage.read(key))
    }

    @Test fun `Update an entry that does not exist`() {
        assertFailsWith<IllegalStateException> {
            storage.update("game5","data")
        }
    }

    @Test fun `Delete existing entry`() {
        val key = "game6"
        storage.create(key, "data")
        storage.delete(key)
        assertNull(storage.read(key))
    }

    @Test fun `Delete an entry that does not exist`() {
        assertFailsWith<IllegalStateException> {
            storage.delete("game7")
        }
    }
}