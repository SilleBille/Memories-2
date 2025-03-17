/*
 * Copyright 2025 SilleBille
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.mkd.memories.network.models

import com.google.gson.Gson
import com.mkd.memories.network.data.NetworkPreviewResponse
import java.io.InputStream
import javax.inject.Inject
import kotlin.jvm.java

/**
 * Parser for the custom binary format used by the /multipreview endpoint
 */
class MultiPreviewProcessor @Inject constructor(
    val gson: Gson,
) {
    /**
     * Parse a multipreview response from an InputStream into a list of NetworkPreviewResponse objects
     */
    fun parse(inputStream: InputStream): List<NetworkPreviewResponse> {
        val networkPreviewResponses = mutableListOf<NetworkPreviewResponse>()

        try {
            while (true) {
                // Read the length of the JSON header (as a single byte)
                val jsonLengthByte = inputStream.read()
                if (jsonLengthByte == -1) break // End of stream

                val jsonLength = jsonLengthByte.toInt() and 0xFF

                // Read the JSON string
                val jsonBytes = ByteArray(jsonLength)
                val bytesRead = inputStream.read(jsonBytes)
                if (bytesRead != jsonLength) break // Error reading

                // Parse the JSON header
                val jsonStr = String(jsonBytes)
                val headerMap = gson.fromJson(jsonStr, NetworkPreviewResponse::class.java)


                // Read the image data
                val imageData = ByteArray(headerMap.contentLength)
                var totalRead = 0
                while (totalRead < headerMap.contentLength) {
                    val read =
                        inputStream.read(imageData, totalRead, headerMap.contentLength - totalRead)
                    if (read == -1) break // End of stream
                    totalRead += read
                }

                if (totalRead != headerMap.contentLength) break // Error reading

                // Add the preview to the list
                networkPreviewResponses.add(
                    NetworkPreviewResponse(
                        headerMap.requestId,
                        headerMap.contentLength,
                        headerMap.mimeType,
                        imageData
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return networkPreviewResponses
    }
}
