package org.sourcegrade.lab.model

import com.google.protobuf.GeneratedMessage
import com.google.protobuf.Parser
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.ContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.jvm.javaio.toInputStream
import io.ktor.utils.io.jvm.javaio.toOutputStream
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.staticFunctions

class ProtobufContentConverter : ContentConverter {
    private fun getParser(typeInfo: TypeInfo): Parser<*> {
        val parserFun = typeInfo.type.staticFunctions
            .firstOrNull { it.name == "parser" && it.returnType.isSubtypeOf(Parser::class.starProjectedType) }
            ?: error("Could not find required static parser method in ${typeInfo.type}")

        return parserFun.call() as Parser<*>
    }

    override suspend fun serializeNullable(contentType: ContentType, charset: Charset, typeInfo: TypeInfo, value: Any?): OutgoingContent? {
        if (value !is GeneratedMessage) return null
        return object : OutgoingContent.WriteChannelContent() {
            override val contentType: ContentType = contentType
            override suspend fun writeTo(channel: ByteWriteChannel) = value.writeTo(channel.toOutputStream())
        }
    }

    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? =
        getParser(typeInfo).parseFrom(content.toInputStream())
}
