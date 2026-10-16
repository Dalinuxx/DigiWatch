package com.digiwatch.companion.nfc

import android.app.Activity
import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import com.digiwatch.core.codec.CommCodeCodec

object NfcDuelManager {

    const val MIME_TYPE_BATTLE = "application/vnd.com.digiwatch.battle"

    fun isNfcAvailable(context: Context): Boolean {
        return NfcAdapter.getDefaultAdapter(context) != null
    }

    fun isNfcEnabled(context: Context): Boolean {
        val adapter = NfcAdapter.getDefaultAdapter(context)
        return adapter != null && adapter.isEnabled
    }

    fun extractCommCodeFromTag(tag: Tag): String {
        // 1. Attempt reading stored NDEF records
        try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                val message = ndef.ndefMessage ?: ndef.cachedNdefMessage
                ndef.close()
                if (message != null) {
                    for (record in message.records) {
                        val payload = record.payload
                        if (payload != null && payload.isNotEmpty()) {
                            val text = String(payload, Charsets.UTF_8).trim()
                            if (text.startsWith("V1-") || text.length >= 16) {
                                return text
                            }
                            // Check text record format with status byte
                            val status = payload[0].toInt() and 0xFF
                            val langLen = status and 0x3F
                            if (payload.size > 1 + langLen) {
                                val textCandidate = String(payload, 1 + langLen, payload.size - (1 + langLen), Charsets.UTF_8).trim()
                                if (textCandidate.startsWith("V1-") || textCandidate.length >= 16) {
                                    return textCandidate
                                }
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {}

        // 2. Deterministic derivation from hardware UID if no explicit DigiROM/CommCode payload
        val uid = tag.id
        return CommCodeCodec.deriveDigiRomFromBytes(uid ?: ByteArray(0))
    }

    fun createDuelNdefMessage(commCodeOrDigiRom: String): NdefMessage {
        val mimeBytes = MIME_TYPE_BATTLE.toByteArray(Charsets.US_ASCII)
        val payloadBytes = commCodeOrDigiRom.toByteArray(Charsets.UTF_8)
        val mimeRecord = NdefRecord(
            NdefRecord.TNF_MIME_MEDIA,
            mimeBytes,
            ByteArray(0),
            payloadBytes
        )
        return NdefMessage(arrayOf(mimeRecord))
    }

    fun writeDuelCodeToTag(tag: Tag, code: String): Boolean {
        return try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                if (!ndef.isWritable) {
                    ndef.close()
                    return false
                }
                val message = createDuelNdefMessage(code)
                ndef.writeNdefMessage(message)
                ndef.close()
                true
            } else {
                val format = NdefFormatable.get(tag)
                if (format != null) {
                    format.connect()
                    val message = createDuelNdefMessage(code)
                    format.format(message)
                    format.close()
                    true
                } else false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun enableReaderMode(
        activity: Activity,
        onTagDiscovered: (code: String, rawTag: Tag) -> Unit
    ) {
        val adapter = NfcAdapter.getDefaultAdapter(activity) ?: return
        if (!adapter.isEnabled) return
        val flags = NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V or
                NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS

        adapter.enableReaderMode(activity, { tag ->
            val code = extractCommCodeFromTag(tag)
            activity.runOnUiThread {
                onTagDiscovered(code, tag)
            }
        }, flags, null)
    }

    fun disableReaderMode(activity: Activity) {
        val adapter = NfcAdapter.getDefaultAdapter(activity) ?: return
        adapter.disableReaderMode(activity)
    }
}
