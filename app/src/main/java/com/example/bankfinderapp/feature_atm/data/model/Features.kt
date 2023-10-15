package com.example.bankfinderapp.feature_atm.data.model

data class Features(
    val blind: Blind,
    val nfcForBankCards: NfcForBankCards,
    val qrRead: QrRead,
    val supportsChargeRub: SupportsChargeRub,
    val supportsEur: SupportsEur,
    val supportsRub: SupportsRub,
    val supportsUsd: SupportsUsd,
    val wheelchair: Wheelchair
)