package gaur.himanshu.booksummary

import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

const val DIRECTORY = "Book Summery"
class FileManager(private val context: Context) {

    fun save(summary: Summary): List<Summary> {
        val fileName = summary.fileName.replace(".txt", "").plus(".txt") // just to avoid double extensions like notes.txt.txt.
        when (summary.type) {
            Type.INTERNAL -> {
                val directory = context.createDirectory()
                val file = File(directory, fileName)
                file.writeText(summary.summary)
            }
            Type.PRIVATE_EXTERNAL -> {
                val directory = context.createPrivateDir()
                val file = File(directory, fileName)
                file.writeText(summary.summary)
            }
            Type.SHARED -> {
                // todo shared
            }
        }

        return getSummaries()
    }

    fun delete(summary: Summary) {
        when(summary.type) {
            Type.INTERNAL -> {
                val directory = context.createDirectory()
                val file = File(directory, summary.fileName)
                if (file.exists()) file.delete()
            }
            Type.PRIVATE_EXTERNAL -> {
                val directory = context.createPrivateDir()
                val file = File(directory, summary.fileName)
                if (file.exists()) file.delete()
            }
            Type.SHARED -> {
                // todo shared
            }
        }
    }

    fun update(summary: Summary) {
        when(summary.type) {
            Type.INTERNAL -> {
                val directory = context.createDirectory()
                val file = File(directory, summary.fileName)
                if (file.exists()) {
                    file.writeText(summary.summary)
                }
            }
            Type.PRIVATE_EXTERNAL -> {
                val directory = context.createPrivateDir()
                val file = File(directory, summary.fileName)
                if (file.exists()) {
                    file.writeText(summary.summary)
                }
            }
            Type.SHARED -> {
                // todo shared
            }
        }
    }

    fun getSummariesAsFlow() = callbackFlow {
        trySend(getSummaries())
        awaitClose { close() }
    }
    fun getSummaries(): List<Summary> {
        val list = mutableListOf<Summary>()
        context.createDirectory().listFiles()?.map {
            Summary(
                fileName = it.name,
                summary = it.readText(),
                type = Type.INTERNAL
            )
        }?.let { summaries ->
            list.addAll(summaries)
        }

        context.createPrivateDir().listFiles()?.map {
            Summary(
                fileName = it.name,
                summary = it.readText(),
                type = Type.PRIVATE_EXTERNAL
            )
        }?.let { summaries ->
            list.addAll(summaries)
        }

        return list.toList()
    }

    // TO create a folder in our internal storage
    fun Context.createDirectory(): File {
        val directory = filesDir // root location
        val file = File(directory, DIRECTORY)
        if (file.exists().not()) file.mkdir()
        return file
    }

    fun Context.createPrivateDir(): File {
        val directory = getExternalFilesDir(null)
        val file = File(directory, DIRECTORY)
        if (file.exists().not()) file.mkdir()
        return file
    }
}























