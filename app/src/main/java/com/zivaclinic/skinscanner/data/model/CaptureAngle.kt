package com.zivaclinic.skinscanner.data.model

enum class CaptureAngle(val displayName: String, val instruction: String) {
    FRONT("Front", "Face the camera directly"),
    LEFT("Left Profile", "Turn your head 45° to the left"),
    RIGHT("Right Profile", "Turn your head 45° to the right")
}
